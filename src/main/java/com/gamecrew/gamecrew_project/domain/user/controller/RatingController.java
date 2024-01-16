package com.gamecrew.gamecrew_project.domain.user.controller;

import com.gamecrew.gamecrew_project.domain.user.dto.request.UserRatingRequestDto;
import com.gamecrew.gamecrew_project.domain.user.dto.response.UserRatingsResponseDto;
import com.gamecrew.gamecrew_project.domain.user.entity.User;
import com.gamecrew.gamecrew_project.domain.user.repository.UserRepository;
import com.gamecrew.gamecrew_project.domain.user.service.RatingService;
import com.gamecrew.gamecrew_project.global.exception.CustomException;
import com.gamecrew.gamecrew_project.global.exception.constant.ErrorMessage;
import com.gamecrew.gamecrew_project.global.response.MessageResponseDto;
import com.gamecrew.gamecrew_project.global.response.constant.Message;
import com.gamecrew.gamecrew_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, UserRatingsResponseDto> redisTemplate;

    //유저의 평점을 DB에 등록하는 API
    @PostMapping("/rating/{evaluated_user}")
    public MessageResponseDto registrationOfRatings(@RequestBody UserRatingRequestDto userRatingRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable Long evaluated_user){
        if (userRepository.findById(evaluated_user).isEmpty()) throw new CustomException(ErrorMessage.NON_EXISTENT_USER, HttpStatus.BAD_REQUEST);

        User evaluator = userDetails.getUser();
        ratingService.registrationOfRatings(userRatingRequestDto, evaluator, evaluated_user);

        return new MessageResponseDto(Message.REGISTRATION_COMPLETED, HttpStatus.OK);
    }

    //유저의 평가들을 가져오는 API
    // key  = evaluatedUser
    // value = userRatingResponseDto
    // 캐시에 있으면 그대로 보여주기
    // 없으면 기존 로직대로
    @GetMapping("/rating/{evaluated_user}")
    public UserRatingsResponseDto getUserRatings(
            @PathVariable Long evaluated_user,
            @RequestParam int page,
            @RequestParam int size
            ){
        String cacheKey = "evaluatedUser:" + evaluated_user + ":page:" + page + ":size:" + size;

        // 캐시에서 평가 정보 조회
        UserRatingsResponseDto cachedResponse = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        // 캐시에 없는 경우 기존 로직으로 평가 정보 조회
        UserRatingsResponseDto response = ratingService.getUserRatings(evaluated_user, page - 1, size);

        // 조회한 평가 정보를 캐시에 저장
        redisTemplate.opsForValue().set(cacheKey, response);

        return response;
    }
}
