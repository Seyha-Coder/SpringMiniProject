package org.example.springminiproject.Service.AppUserService;

import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.AppUserModel.AppUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface AppUserService extends UserDetailsService {
    AppUserDTO createUser(AppUserRequest appUserRequest);

    AppUserDTO findUserByEmail(String email);

    AppUserDTO updatePassword(String password, UUID userId);
    public AppUserDTO getById(UUID id);
}
