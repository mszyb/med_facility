package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.entity.User;
import pl.mszyb.med_facility.repository.PhysicianScheduleRepository;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class PhysicianScheduleService {
    private final PhysicianScheduleRepository physicianScheduleRepository;
    private final AppointmentService appointmentService;

    public void save(PhysicianSchedule physicianSchedule) {
        physicianScheduleRepository.save(physicianSchedule);
    }

    public List<PhysicianSchedule> findAllByPhysicianIdForSelectedPeriod(long physicianId) {
        ZonedDateTime scheduleInterval = ZonedDateTime.now().plusDays(14);
        return physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(physicianId, scheduleInterval, ZonedDateTime.now());
    }

    public List<ZonedDateTime> calculateAvailableSlots(Long physicianId) {
        Duration visitLength = Duration.ofMinutes(30);
        ZonedDateTime scheduleInterval = ZonedDateTime.now().plusDays(14);
        List<PhysicianSchedule> physicianSchedule = physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(physicianId, scheduleInterval, ZonedDateTime.now());
        List<ZonedDateTime> availableSlots = new ArrayList<>();
        List<Appointment> appointments = appointmentService.findAllByPhysicianIdForSelectedPeriod(physicianId, scheduleInterval, ZonedDateTime.now().minusDays(7));
        List<ZonedDateTime> alreadyOccupiedSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            alreadyOccupiedSlots.add(appointment.getStartTime());
        }
        for (PhysicianSchedule schedule : physicianSchedule) {
            ZonedDateTime slot = schedule.getStartTime();
            while (slot.plusMinutes(30).isBefore(schedule.getEndTime())) {
                if (!alreadyOccupiedSlots.contains(slot) && slot.isAfter(ZonedDateTime.now())) {
                    availableSlots.add(slot);
                }
                slot = slot.plus(visitLength);
            }
        }
        availableSlots.sort(Comparator.naturalOrder());
        return availableSlots;
    }

    public void deleteById(long id) {
        physicianScheduleRepository.deleteById(id);
    }

    public PhysicianSchedule findById(long id) {
        return physicianScheduleRepository.findById(id);
    }

    public void addNewShift(LocalDate date, LocalTime startTime, LocalTime endTime, User currentUser) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Wrong start or/and end time");
        }
        ZonedDateTime startDateTime = startTime.atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
        ZonedDateTime endDateTime = endTime.atDate(date).atZone(ZoneId.of("Europe/Warsaw"));
        List<PhysicianSchedule> currentUserSchedule = findAllByPhysicianIdForSelectedPeriod(currentUser.getId());
        for (PhysicianSchedule schedule : currentUserSchedule) {
            if (date.equals(schedule.getStartTime().toLocalDate())) {
                ZonedDateTime shiftStart = schedule.getStartTime();
                ZonedDateTime shiftEnd = schedule.getEndTime();
                if (startDateTime.isBefore(shiftStart) && endDateTime.isBefore(shiftEnd) && endDateTime.isAfter(shiftStart)
                        || startDateTime.isBefore(shiftStart) && endDateTime.isAfter(shiftEnd)
                        || (startDateTime.isAfter(shiftStart) || startDateTime.equals(shiftStart)) && startDateTime.isBefore(shiftEnd) && (endDateTime.isAfter(shiftEnd) || endDateTime.equals(shiftEnd))
                        || (startDateTime.isAfter(shiftStart) || startDateTime.equals(shiftStart)) && (endDateTime.isBefore(shiftEnd) || endDateTime.equals(shiftEnd))) {
                    throw new IllegalArgumentException("Shifts cannot overlaps");
                }
            }
        }
        if ((!(startTime.getMinute() == 30 || startTime.getMinute() == 0)) || (!(endTime.getMinute() == 30 || endTime.getMinute() == 0))) {
            throw new IllegalArgumentException("You can only select full or half hours");
        }
        PhysicianSchedule physicianSchedule = new PhysicianSchedule();
        physicianSchedule.setPhysician(currentUser);
        physicianSchedule.setStartTime(startDateTime);
        physicianSchedule.setEndTime(endDateTime);
        save(physicianSchedule);
    }
}
