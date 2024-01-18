package com.gamecrew.gamecrew_project.domain.user.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRatingResultDto {
    private Long evaluator; //평가를 한 사람
    private int manner;
    private int participation;
    private int gamingSkill;
    private int enjoyable;
    private int sociability;

    public UserRatingResultDto(Long evaluator,
                               int manner,
                               int participation,
                               int gamingSkill,
                               int enjoyable,
                               int sociability){
        this.evaluator = evaluator;
        this.manner = manner;
        this.participation = participation;
        this.gamingSkill = gamingSkill;
        this.enjoyable = enjoyable;
        this.sociability = sociability;

    }
}
