package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Appointment;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.repository.PhysicianScheduleRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
            while (slot.isBefore(schedule.getEndTime())) {
                if (!alreadyOccupiedSlots.contains(slot) && slot.isAfter(ZonedDateTime.now())) {
                    availableSlots.add(slot);
                }
                slot = slot.plus(visitLength);
            }
        }
        availableSlots.sort(Comparator.naturalOrder());
        return availableSlots;
    }

    public void deleteById(long id){
        physicianScheduleRepository.deleteById(id);
    }

    public PhysicianSchedule findById(long id){
        return physicianScheduleRepository.findById(id);
    }
}
