package com.gamecrew.gamecrew_project.domain.user.service;

import com.gamecrew.gamecrew_project.domain.user.dto.request.SignupRequestDto;
import com.gamecrew.gamecrew_project.domain.user.entity.User;
import com.gamecrew.gamecrew_project.domain.user.repository.UserRepository;
import com.gamecrew.gamecrew_project.global.exception.CustomException;
import com.gamecrew.gamecrew_project.global.exception.constant.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto requestDto){
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new CustomException(ErrorMessage.DUPLICATE_NICKNAME_EXISTS, HttpStatus.CONFLICT, true);
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorMessage.DUPLICATE_EMAIL_EXISTS, HttpStatus.CONFLICT, true);
        }

        //사용자 등록
        User user = new User(email, nickname, password);
        userRepository.save(user);
    }

    public void checkNickname(String nickname) {
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new CustomException(ErrorMessage.DUPLICATE_NICKNAME_EXISTS, HttpStatus.CONFLICT, true);
        }
    }

    public void checkEmail(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorMessage.DUPLICATE_EMAIL_EXISTS, HttpStatus.CONFLICT, true);
        }
    }
}
