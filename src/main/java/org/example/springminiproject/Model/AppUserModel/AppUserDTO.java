package org.example.springminiproject.Model.AppUserModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO{
    private UUID userId;
    private String email;
    private String password;
    private String profileImage;
}
