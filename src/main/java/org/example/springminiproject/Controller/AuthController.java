package org.example.springminiproject.Controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.springminiproject.Model.ApiResponse.ApiResponse;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.AppUserModel.AppUserRequest;
import org.example.springminiproject.Model.Auth.AuthRequest;
import org.example.springminiproject.Model.Auth.AuthResponse;
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
import java.util.Date;
import java.util.Optional;

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
    public String verify(@RequestBody String OptCode) {
        Optional<OptsDTO> optional = optService.findByCode(OptCode);
        optional.ifPresent(c -> {
            String verificationResult;
            if (c.getExpiration().before(new Date())) {
                verificationResult = "failed";
            } else {
                verificationResult = "ok";
                c.setVerify(true);

                optService.uploadOpt(OptCode);
            }
        });
        return "verified successfully!";
    }

    @PutMapping("/forget")
    public String forget() {
        return "forgot password";
    }

    @PostMapping("resend")
    public String resend() {
        return "resent password";
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody AppUserRequest appUserRequest) throws MessagingException, IOException {
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
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws Exception {
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

            System.out.println(passwordEncoder.matches(password, userDetails.getPassword()));
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User has disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }


}

