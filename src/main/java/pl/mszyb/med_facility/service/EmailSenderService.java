package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ConfirmationToken;
import pl.mszyb.med_facility.entity.User;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final ConfirmationTokenService confirmationTokenService;

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
        mailMessage.setSubject("MedFacility registration process");
        mailMessage.setFrom("meedfaacility@gmail.com");
        mailMessage.setText("You are almost there! Please click this link to confirm your email address and finish registration process "
                + " http://localhost:8080/sign_in/confirmation?token=" + confirmationToken.getConfirmationToken());
        return mailMessage;
    }

}
