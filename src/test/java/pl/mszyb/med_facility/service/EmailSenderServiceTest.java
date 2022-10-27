package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import pl.mszyb.med_facility.entity.User;

import java.util.Locale;
import java.util.Objects;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    private static final String SUBJECT = "some_subject";
    private static final String FROM = "some_from";
    private static final String TEXT = "some_text";

    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private EmailSenderService emailSenderService;
    private User someUser;


    @BeforeEach
    void setUp(){
        given(messageSource.getMessage("mail.subject", null, Locale.ENGLISH)).willReturn(SUBJECT);
        given(messageSource.getMessage("mail.from", null, Locale.ENGLISH)).willReturn(FROM);
        given(messageSource.getMessage("mail.text", null, Locale.ENGLISH)).willReturn(TEXT);
    }
    @Test
    void should_create_registration_message() {
        someUser = new User();
        someUser.setEmail("some-email");
        SimpleMailMessage returnedMessage = emailSenderService.createRegistrationMail(someUser);
        assertNotNull(returnedMessage);
        assertEquals(SUBJECT, returnedMessage.getSubject());
        assertEquals(FROM, returnedMessage.getFrom());
        assertNotEquals(TEXT, returnedMessage.getText());
        assertTrue(Objects.requireNonNull(returnedMessage.getText()).contains(TEXT));
    }
}