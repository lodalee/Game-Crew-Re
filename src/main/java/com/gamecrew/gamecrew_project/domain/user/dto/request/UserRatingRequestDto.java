package com.gamecrew.gamecrew_project.domain.user.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRatingRequestDto {
    @Min(0) @Max(10)
    private int manner;

    @Min(0) @Max(10)
    private int participation;

    @Min(0) @Max(10)
    private int gamingSkill;

    @Min(0) @Max(10)
    private int enjoyable;

    @Min(0) @Max(10)
    private int sociability;

    public UserRatingRequestDto(int manner, int participation, int gamingSkill, int enjoyable, int sociability) {
        this.manner = manner;
        this.participation =  participation;
        this.gamingSkill = gamingSkill;
        this.enjoyable = enjoyable;
        this.sociability = sociability;
    }
}

