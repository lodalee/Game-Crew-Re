package com.gamecrew.gamecrew_project.domain.user.dto.response;

import com.gamecrew.gamecrew_project.global.response.CustomPageable;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRatingsResponseDto {
    private String msg;
    private CustomPageable pageable;
    private List<UserRatingResultDto> result;

    public UserRatingsResponseDto(
            String msg,
            int totalPages,
            long totalElements,
            int size,
            List<UserRatingResultDto> userRatingResultDto
    ) {
        this.msg = msg;
        this.pageable = new CustomPageable(totalPages, totalElements, size);
        this.result = userRatingResultDto;
    }
}
