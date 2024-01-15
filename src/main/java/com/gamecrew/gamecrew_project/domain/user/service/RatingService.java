package com.gamecrew.gamecrew_project.domain.user.service;

import com.gamecrew.gamecrew_project.domain.user.dto.request.UserRatingRequestDto;
import com.gamecrew.gamecrew_project.domain.user.dto.response.UserRatingResultDto;
import com.gamecrew.gamecrew_project.domain.user.dto.response.UserRatingsResponseDto;
import com.gamecrew.gamecrew_project.domain.user.entity.RecordOfRatings;
import com.gamecrew.gamecrew_project.domain.user.entity.TotalRating;
import com.gamecrew.gamecrew_project.domain.user.entity.User;
import com.gamecrew.gamecrew_project.domain.user.repository.RecordOfRatingsRepository;
import com.gamecrew.gamecrew_project.domain.user.repository.TotalRatingRepository;
import com.gamecrew.gamecrew_project.domain.user.repository.UserRepository;
import com.gamecrew.gamecrew_project.global.exception.CustomException;
import com.gamecrew.gamecrew_project.global.exception.constant.ErrorMessage;
import com.gamecrew.gamecrew_project.global.response.constant.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final TotalRatingRepository totalRatingRepository;
    private final RecordOfRatingsRepository recordOfRatingsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registrationOfRatings(UserRatingRequestDto userRatingRequestDto, User evaluator, Long evaluated_user) {
        int manner = userRatingRequestDto.getManner();
        int participation = userRatingRequestDto.getParticipation();
        int gamingSkill = userRatingRequestDto.getGamingSkill();
        int enjoyable = userRatingRequestDto.getEnjoyable();
        int sociability = userRatingRequestDto.getSociability();
        Long evaluatorId =evaluator.getUserId();

        Optional<TotalRating> checkRating = totalRatingRepository.findByUserId(evaluated_user);
        LocalDateTime now = LocalDateTime.now();

        if (checkRating.isEmpty()){
            double totalRating = (double)(manner + participation + gamingSkill + enjoyable + sociability) / 5 ;


            RecordOfRatings ratings = new RecordOfRatings(evaluated_user ,evaluatorId, manner, participation, gamingSkill, enjoyable, sociability, totalRating, now);
            recordOfRatingsRepository.save(ratings);

            TotalRating totalrating = new TotalRating(evaluated_user, manner, participation, gamingSkill, enjoyable, sociability, totalRating);
            totalRatingRepository.save(totalrating);

        } else if (checkRating.isPresent()) {
            PageRequest pageable = PageRequest.of(0, 9);
            List<RecordOfRatings> beforeRatings = recordOfRatingsRepository.findTop9ByUserIdOrderByRecordedAtDesc(evaluated_user, pageable);

            double sumManner = manner;
            double sumParticipation = participation;
            double sumGamingSkill = gamingSkill;
            double sumEnjoyable = enjoyable;
            double sumSociability = sociability;

            for (RecordOfRatings rating : beforeRatings) {
                sumManner += rating.getManner();
                sumParticipation += rating.getParticipation();
                sumGamingSkill += rating.getGamingSkill();
                sumEnjoyable += rating.getEnjoyable();
                sumSociability += rating.getSociability();
            }

            double totalManner = sumManner / (1.0 + beforeRatings.size());
            double totalParticipation = sumParticipation / (1.0 + beforeRatings.size());
            double totalGamingSkill = sumGamingSkill / (1.0 + beforeRatings.size());
            double totalEnjoyable = sumEnjoyable / (1.0 + beforeRatings.size());
            double totalSociability = sumSociability / (1.0 + beforeRatings.size());
            double totalRating = (totalManner + totalParticipation + totalGamingSkill + totalEnjoyable + totalSociability) / 5;

            RecordOfRatings ratings = new RecordOfRatings(evaluated_user ,evaluatorId, manner, participation, gamingSkill, enjoyable, sociability, totalRating, now);
            recordOfRatingsRepository.save(ratings);

            // Update TotalRating
            TotalRating totalRatingEntity = checkRating.get();
            totalRatingEntity.setTotalManner(totalManner);
            totalRatingEntity.setTotalParticipation(totalParticipation);
            totalRatingEntity.setTotalGamingSkill(totalGamingSkill);
            totalRatingEntity.setTotalEnjoyable(totalEnjoyable);
            totalRatingEntity.setTotalSociability(totalSociability);
            totalRatingEntity.setTotalRating(totalRating);
            totalRatingRepository.save(totalRatingEntity);
        }
    }


    public UserRatingsResponseDto getUserRatings(Long evaluatedUser, int page, int size) {
        userRepository.findByUserId(evaluatedUser).orElseThrow(
                ()-> new CustomException(ErrorMessage.NON_EXISTENT_USER, HttpStatus.BAD_REQUEST));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordedAt"));
        Page<RecordOfRatings> recordPage = recordOfRatingsRepository.findByUserId(pageable, evaluatedUser);

        List<UserRatingResultDto> userRatingResultDtoList = recordPage.getContent().stream()
                .map(record -> new UserRatingResultDto(
                        record.getEvaluator(),
                        record.getManner(),
                        record.getParticipation(),
                        record.getGamingSkill(),
                        record.getEnjoyable(),
                        record.getSociability()
                )).collect(Collectors.toList());

        return new UserRatingsResponseDto(
                Message.GET_RATINGS_SUCCESSFUL,
                recordPage.getTotalPages(),
                recordPage.getTotalElements(),
                size,
                userRatingResultDtoList
        );
    }

//    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void populateTotalRatingWithRecordOfRatings() {
        List<RecordOfRatings> allRatings = recordOfRatingsRepository.findAll();

        for (RecordOfRatings rating : allRatings) {
            UserRatingRequestDto requestDto = new UserRatingRequestDto(rating.getManner(), rating.getParticipation(), rating.getGamingSkill(), rating.getEnjoyable(), rating.getSociability());
            User evaluator = userRepository.findById(rating.getEvaluator()).orElseThrow(() -> new IllegalArgumentException("Evaluator Not Found")); // 평가자 정보를 가져옵니다.
            Long evaluated_user = rating.getUserId();
            registrationOfRatings(requestDto, evaluator, evaluated_user);
        }
    }
}
