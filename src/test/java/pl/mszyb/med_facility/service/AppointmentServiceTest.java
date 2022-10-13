package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.mszyb.med_facility.entity.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.BDDMockito.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    AppointmentService appointmentService;

    @Test
    void should_find_physician_appointments_throw_not_found_exception() {
        User user = new User();
        user.setEmail("email");
        given(userService.findByEmail(user.getEmail())).willReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> appointmentService.findPhysicianAppointments(user.getEmail(), LocalDate.now()));
    }

    @Test
    void should_find_patient_appointments_throw_not_found_exception() {
        User user = new User();
        user.setEmail("email");
        given(userService.findByEmail(user.getEmail())).willReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> appointmentService.findPatientAppointments(user.getEmail(), LocalDate.now()));
    }
}