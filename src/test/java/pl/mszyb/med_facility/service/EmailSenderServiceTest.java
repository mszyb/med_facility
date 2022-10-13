package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import pl.mszyb.med_facility.entity.User;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    ConfirmationTokenService confirmationTokenService;

    @InjectMocks
    EmailSenderService emailSenderService;

    User someUser;
    MessageSource messageSource;

    EmailSenderServiceTest(MessageSource messageSource){
        this.messageSource = messageSource;
    }
    @Test
    void should_create_registration_message() {
        someUser.setEmail("some-email");
        SimpleMailMessage returnedMessage = emailSenderService.createRegistrationMail(someUser);
        assertEquals(messageSource.getMessage("mail.subject", null, Locale.ENGLISH), returnedMessage.getSubject());
    }
}