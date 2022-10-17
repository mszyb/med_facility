package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.repository.PhysicianScheduleRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PhysicianScheduleServiceTest {

    @Mock
    PhysicianScheduleRepository physicianScheduleRepository;
    @Mock
    AppointmentService appointmentService;
    @InjectMocks
    PhysicianScheduleService physicianScheduleService;
    List<PhysicianSchedule> physicianScheduleList;
    List<Appointment> physicianAppointmentsList;
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    ZonedDateTime startTime = ZonedDateTime.now(zone);
    User physician;
    User patient;
    Appointment appointment;



    @BeforeEach
    void setUp() {
        physicianScheduleList = preparePhysicianSchedule();
        physicianAppointmentsList = prepareAppointmentList();
    }

    @Test
    void should_calculate_13_available_slots() {
        given(physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(physicianScheduleList);
        given(appointmentService.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(physicianAppointmentsList);
        List<ZonedDateTime> availableSlots = physicianScheduleService.calculateAvailableSlots(physician.getId());
        assertEquals(13, availableSlots.size());
    }

    private List<PhysicianSchedule> preparePhysicianSchedule() {
        physicianScheduleList = new ArrayList<>();
        physician = new User();
        physician.setId(1L);
        ZonedDateTime testStartTime = startTime;
        for (int i = 0; i < 2; i++) {
            PhysicianSchedule physicianSchedule = new PhysicianSchedule();
            physicianSchedule.setId((long)i);
            physicianSchedule.setPhysician(physician);
            physicianSchedule.setStartTime(testStartTime);
            physicianSchedule.setEndTime(testStartTime.plusHours(4));
            physicianScheduleList.add(physicianSchedule);
            testStartTime = startTime.plusDays(i+1);
        }
        return physicianScheduleList;
    }

    private List<Appointment> prepareAppointmentList() {
        physicianAppointmentsList = new ArrayList<>();
        patient = new User();
        patient.setId(1L);
        ZonedDateTime testStartTime = startTime;
        for(int i=0; i<3; i++){
            appointment = new Appointment();
            appointment.setId((long)i);
            appointment.setPhysician(physician);
            appointment.setDone(false);
            appointment.setPatient(patient);
            appointment.setStartTime(testStartTime);
            appointment.setEndTime(testStartTime.plusMinutes(30));
            appointment.setSelectedService(null);
            appointment.setSelectedSpec(null);
            physicianAppointmentsList.add(appointment);
            testStartTime=startTime.plusHours(i+1);
        }
    return physicianAppointmentsList;
    }
}