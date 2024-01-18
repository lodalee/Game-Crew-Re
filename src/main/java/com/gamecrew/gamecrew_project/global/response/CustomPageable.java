package com.gamecrew.gamecrew_project.global.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomPageable {
    private int totalPages;
    private long totalElements;
    private int size;

    public CustomPageable(int totalPages, long totalElements, int size) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
    }
}

