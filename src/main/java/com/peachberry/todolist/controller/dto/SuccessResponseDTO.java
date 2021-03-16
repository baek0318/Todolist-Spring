package com.peachberry.todolist.controller.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
public class SuccessResponseDTO {

    private String response;

    public SuccessResponseDTO(String response) {
        this.response = response;
    }

}
