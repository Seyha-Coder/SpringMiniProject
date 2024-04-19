package org.example.springminiproject.Controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.example.springminiproject.Exception.AllNotfoundException;
import org.example.springminiproject.Exception.BadRequestException;
import org.example.springminiproject.Model.ApiResponse.ApiResponse;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.AppUserModel.AppUserRequest;
import org.example.springminiproject.Model.Auth.AuthRequest;
import org.example.springminiproject.Model.Auth.AuthResponse;
import org.example.springminiproject.Model.Auth.PasswordRequest;
import org.example.springminiproject.Model.OPT.OptsDTO;
import org.example.springminiproject.Security.JwtService;
import org.example.springminiproject.Service.AppUserService.AppUserService;
import org.example.springminiproject.Service.MailService.EmailService;
import org.example.springminiproject.Service.MailService.OptGenerator;
import org.example.springminiproject.Service.OptService.OptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.*;

@RequestMapping("/api/v1/auths")
@RestController
@AllArgsConstructor
public class AuthController {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final OptService optService;
    private final EmailService emailService;


    @PutMapping("/verify")
    public ResponseEntity<?> verify(String OptCode) {
        Optional<OptsDTO> optsDTO = optService.findByCode(OptCode);
        ApiResponse<String> apiResponse = null;
        if (optsDTO != null) {
            apiResponse = ApiResponse.<String>builder()
                    .status(HttpStatus.OK)
                    .code(201)
                    .message("Your opt has been sent to your email")
                    .payload("Successfully verified")
                    .build();
            optService.verify(OptCode);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/forget")
    public ResponseEntity<?> forget(
            @NotNull @NotBlank @Email(message = "Invalid email") @RequestParam String email,
            @Valid @RequestBody PasswordRequest passwordRequest
    ) {
        AppUserDTO appUserDTO = appUserService.findUserByEmail(email);
        if (appUserDTO == null){
            throw new AllNotfoundException("Invalid email");
        } else if (!Objects.equals(passwordRequest.getPassword(), passwordRequest.getConfirmPassword())) {
            throw new BadRequestException("Your confirm password does not match with your password");
        }
        String encodedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        AppUserDTO result = appUserService.updatePassword(encodedPassword, appUserDTO.getUserId());
        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .message("New password was updated successfully!")
                .code(200)
                .status(HttpStatus.CREATED)
                .payload(appUserDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<AppUserDTO>> resend(
            @NotNull @NotBlank @Email(message = "Invalid email") @RequestParam String email
    ) throws MessagingException, IOException {
        AppUserDTO appUserDTO = appUserService.findUserByEmail(email);
        if (appUserDTO == null){
            throw new AllNotfoundException("Invalid email");
        }
        OptsDTO optsDTO = OptGenerator.generateOTP(6,appUserDTO.getUserId());
        optService.resendOpt(optsDTO.getOptCode(), appUserDTO.getUserId());
        emailService.sendEmail(appUserDTO.getEmail(),optsDTO.getOptCode());
        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .message("New OTP was send successfully!")
                .code(200)
                .status(HttpStatus.CREATED)
                .payload(appUserDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody AppUserRequest appUserRequest) throws MessagingException, IOException {
        List<String> VALID_IMAGE_FORMATS = Arrays.asList("jpeg", "jpg", "png", "gif");
        String imageEx = appUserRequest.getProfileImage().substring(appUserRequest.getProfileImage().lastIndexOf(".") + 1);
        if (!VALID_IMAGE_FORMATS.contains(imageEx)){
            throw new BadRequestException("profile must be contain file extension such as jpg, png, gif and bmp only");
        }
        String encodedPassword = passwordEncoder.encode(appUserRequest.getPassword());
        appUserRequest.setPassword(encodedPassword);
        AppUserDTO appUserDTO = appUserService.createUser(appUserRequest);
        System.out.println(appUserDTO);
        OptsDTO optsDTO = OptGenerator.generateOTP(6,appUserDTO.getUserId());
        optService.save(optsDTO);
        String optsGenerated = emailService.sendEmail(appUserRequest.getEmail(),optsDTO.getOptCode());
        System.out.println(optsGenerated);
        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .message("You have already registered successfully!")
                .code(201)
                .status(HttpStatus.CREATED)
                .payload(appUserDTO)
                .build();
        System.out.println(appUserDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        AppUserDTO user = appUserService.findUserByEmail(authRequest.getEmail());
        if(user == null){
            throw new AllNotfoundException("Invalid email");
        }
        authenticate(authRequest.getEmail(), authRequest.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(authResponse);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            UserDetails userDetails = appUserService.loadUserByUsername(email);
            if (userDetails == null) {
                throw new BadRequestException("User not found");
            }
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new AllNotfoundException("Invalid Password");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User has disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }
}

