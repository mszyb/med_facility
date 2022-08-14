package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.PhysicianSchedule;
import pl.mszyb.med_facility.repository.PhysicianScheduleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhysicianScheduleService {
    private final PhysicianScheduleRepository physicianScheduleRepository;

    public PhysicianScheduleService(PhysicianScheduleRepository physicianScheduleRepository) {
        this.physicianScheduleRepository = physicianScheduleRepository;
    }

    public void save(PhysicianSchedule physicianSchedule) {
        physicianScheduleRepository.save(physicianSchedule);
    }

    public List<PhysicianSchedule> findAllByPhysicianIdForSelectedPeriod(long physicianId) {
        LocalDateTime scheduleInterval = LocalDateTime.now().plusDays(7);
        return physicianScheduleRepository.findAllByPhysicianIdForSelectedPeriod(physicianId, scheduleInterval, LocalDateTime.now());
    }
}
