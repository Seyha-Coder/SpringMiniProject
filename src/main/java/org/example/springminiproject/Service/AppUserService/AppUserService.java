package org.example.springminiproject.Service.AppUserService;

import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.AppUserModel.AppUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


public interface AppUserService extends UserDetailsService {
    AppUserDTO createUser(AppUserRequest appUserRequest);
}
