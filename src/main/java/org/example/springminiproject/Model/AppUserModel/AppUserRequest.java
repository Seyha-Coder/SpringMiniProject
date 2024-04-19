package org.example.springminiproject.Model.AppUserModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequest {
    @Email(message = "must be a well-formed email address")
    private String email;
    @Pattern(message = "password must be at least 8 characters long and include both letters and numbers", regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;
    @Pattern(message = "confirm password must be at least 8 characters long and include both letters and numbers", regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$")
    private String confirmPassword;
    private String profileImage;
}
