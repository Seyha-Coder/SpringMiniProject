package org.example.springminiproject.Service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String Path_To_Image = "classpath:static/images/";

    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine, ResourceLoader resourceLoader) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.resourceLoader = resourceLoader;
    }

    public String sendEmail(String toEmail, String otp) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String ImageBase64 = readImageToBase64("springLogo.png");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Email Verification");

        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("image",ImageBase64);

        String htmlContent = templateEngine.process("otp-mail", context);

        helper.setText(htmlContent, true);
        javaMailSender.send(message);
        return otp;
    }
    public String readImageToBase64(String imagePath) throws IOException {
        Resource resource = resourceLoader.getResource(Path_To_Image.concat(imagePath));
        File imageFile = new File(String.valueOf(resource.getFile()));
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.encodeBase64String(imageBytes);
    }
}
