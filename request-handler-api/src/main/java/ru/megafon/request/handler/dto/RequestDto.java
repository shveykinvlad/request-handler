package ru.megafon.request.handler.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class RequestDto {

    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String text;
}
