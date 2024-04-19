package org.example.springminiproject.Service.AppUserService;


import org.example.springminiproject.Model.AppUserModel.AppUser;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.AppUserModel.AppUserRequest;
import org.example.springminiproject.Model.AppUserModel.CustomUserDetails;
import org.example.springminiproject.Repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUserDTO appUser = appUserRepository.findByEmail(email);
        System.out.println(appUser);
        return new CustomUserDetails(appUser);
    }

    @Override
    public AppUserDTO createUser(AppUserRequest appUserRequest) {
        return appUserRepository.saveUser(appUserRequest);
    }

    @Override
    public AppUserDTO findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUserDTO updatePassword(String password, UUID userId) {
        return appUserRepository.updatePassword(password, userId);
    }

}