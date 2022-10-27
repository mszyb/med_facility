package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ConfirmationToken;
import pl.mszyb.med_facility.entity.User;

import java.util.Locale;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final MessageSource messageSource;

    @Async
    public void sendMail(SimpleMailMessage mail){
        javaMailSender.send(mail);
    }

    public SimpleMailMessage createRegistrationMail(User user){
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationTokenService.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(messageSource.getMessage("mail.subject", null, Locale.ENGLISH));
        mailMessage.setFrom(messageSource.getMessage("mail.from", null, Locale.ENGLISH));
        mailMessage.setText(messageSource.getMessage("mail.text", null, Locale.ENGLISH) + confirmationToken.getConfirmationToken());
        return mailMessage;
    }

}
