package ru.megafon.request.handler.dto;

import lombok.experimental.Accessors;
import ru.megafon.request.handler.annotation.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@PasswordMatches
public class UserDto {

    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String matchingPassword;
}
