package com.example.demo.dtos.requestDTOs;

import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = ErrorMsgs.REGISTER_FIRSTNAME_REQUIRED)
    private String firstname;
    @NotBlank(message = ErrorMsgs.REGISTER_LASTNAME_REQUIRED)
    private String lastname;
    @NotBlank(message = ErrorMsgs.REGISTER_EMAIL_REQUIRED)
    @Email(message = ErrorMsgs.REGISTER_EMAIL_INVALID)
    private String email;
    @NotBlank(message = ErrorMsgs.REGISTER_PASSWORD_REQUIRED)
    @Length(min = 8, message = ErrorMsgs.REGISTER_PASSWORD_MIN_LENGTH)
    private String password;
}
