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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PhysicianScheduleServiceTest {

    private static final String WRONG_START_END_TIME_MSG = "Wrong start or/and end time";
    private static final String SHIFTS_OVERLAPS_MSG = "Shifts cannot overlaps";
    private static final String FULL_HALF_HOUR_MSG = "You can only select full or half hours";
    @Mock
    PhysicianScheduleRepository physicianScheduleRepository;
    @Mock
    AppointmentService appointmentService;
    @InjectMocks
    PhysicianScheduleService physicianScheduleService;
    private List<PhysicianSchedule> physicianScheduleList;
    private List<Appointment> physicianAppointmentsList;
    private ZoneId zone = ZoneId.of("Europe/Warsaw");
    private ZonedDateTime startTime = ZonedDateTime.now(zone).plusMinutes(10);
    private User physician;
    private User patient;
    private Appointment appointment;


    @BeforeEach
    void setUp() {
        physicianScheduleList = preparePhysicianScheduleWith14Slots();
        physicianAppointmentsList = prepareAppointmentListWith3Appointments();
    }

    @Test
    void should_calculate_11_available_slots() {
        given(physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(physicianScheduleList);
        given(appointmentService.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(physicianAppointmentsList);
        List<ZonedDateTime> availableSlots = physicianScheduleService.calculateAvailableSlots(physician.getId());
        assertEquals(11, availableSlots.size());
    }

    @Test
    void should_calculate_14_available_slots() {
        given(physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(physicianScheduleList);
        given(appointmentService.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(new ArrayList<>());
        List<ZonedDateTime> availableSlots = physicianScheduleService.calculateAvailableSlots(physician.getId());
        assertEquals(14, availableSlots.size());
    }

    @Test
    void should_calculate_0_available_slots() {
        given(physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(new ArrayList<>());
        given(appointmentService.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(new ArrayList<>());
        List<ZonedDateTime> availableSlots = physicianScheduleService.calculateAvailableSlots(physician.getId());
        assertNotNull(availableSlots);
        assertEquals(0, availableSlots.size());
    }

    @Test
    void should_throw_illegal_argument_exception(){
        given(physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(eq(physician.getId()), any(), any())).willReturn(physicianScheduleList);
        IllegalArgumentException exception;

        LocalTime startTestTime = LocalTime.now();
        exception = assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime.plusHours(1), startTestTime, physician));
        assertEquals(WRONG_START_END_TIME_MSG, exception.getMessage());
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime, startTestTime, physician));
        exception = assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime, startTestTime.plusMinutes(30), physician));
        assertEquals(SHIFTS_OVERLAPS_MSG, exception.getMessage());
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime, startTestTime.plusHours(5), physician));
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime.plusMinutes(10), startTestTime.plusHours(5), physician));
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime.plusMinutes(15), startTestTime.plusHours(5), physician));
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime.plusMinutes(15), startTestTime.plusMinutes(10).plusHours(4), physician));
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime.plusMinutes(10), startTestTime.plusMinutes(15), physician));
        assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), startTestTime.plusMinutes(15), startTestTime.plusMinutes(15), physician));
        exception = assertThrows(IllegalArgumentException.class, () -> physicianScheduleService.addNewShift(LocalDate.now(), LocalTime.of(10,13,0), LocalTime.of(10,43,0), physician));
        assertEquals(FULL_HALF_HOUR_MSG, exception.getMessage());
    }

    private List<PhysicianSchedule> preparePhysicianScheduleWith14Slots() {
        physicianScheduleList = new ArrayList<>();
        physician = new User();
        physician.setId(1L);
        ZonedDateTime testStartTime = startTime;
        for (int i = 0; i < 2; i++) {
            PhysicianSchedule physicianSchedule = new PhysicianSchedule();
            physicianSchedule.setId((long) i);
            physicianSchedule.setPhysician(physician);
            physicianSchedule.setStartTime(testStartTime);
            physicianSchedule.setEndTime(testStartTime.plusHours(4));
            physicianScheduleList.add(physicianSchedule);
            testStartTime = startTime.plusDays(i + 1);
        }
        return physicianScheduleList;
    }

    private List<Appointment> prepareAppointmentListWith3Appointments() {
        physicianAppointmentsList = new ArrayList<>();
        patient = new User();
        patient.setId(1L);
        ZonedDateTime testStartTime = startTime;
        for (int i = 0; i < 3; i++) {
            appointment = new Appointment();
            appointment.setId((long) i);
            appointment.setPhysician(physician);
            appointment.setDone(false);
            appointment.setPatient(patient);
            appointment.setStartTime(testStartTime);
            appointment.setEndTime(testStartTime.plusMinutes(30));
            appointment.setSelectedService(null);
            appointment.setSelectedSpec(null);
            physicianAppointmentsList.add(appointment);
            testStartTime = startTime.plusHours(i + 1);
        }
        return physicianAppointmentsList;
    }
}