package com.peachberry.todolist.dto;

import lombok.Getter;

@Getter
public class SuccessResponseDTO {

    private String response;

    public SuccessResponseDTO(String response) {
        this.response = response;
    }
}
