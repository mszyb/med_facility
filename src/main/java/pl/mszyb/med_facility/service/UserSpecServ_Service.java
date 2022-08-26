package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.UserServicesSpecializations;
import pl.mszyb.med_facility.repository.SpecializationRepository;
import pl.mszyb.med_facility.repository.UserServicesSpecializationsRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class UserSpecServ_Service {

    private final UserServicesSpecializationsRepository userServicesSpecializationsRepository;
    private final SpecializationRepository specializationRepository;
    private final PhysicianScheduleService physicianScheduleService;


    public List<Specialization> findSpecializationsForUserId(long id) {
        List<Long> identifiers = userServicesSpecializationsRepository.findSpecializationForUserId(id);
        List<Specialization> userSpecializations = new ArrayList<>();
        for (Long specId : identifiers) {
            Optional<Specialization> us = specializationRepository.findById(specId);
            us.ifPresent(userSpecializations::add);
        }
        return userSpecializations;
    }

    public Map<Specialization, List<ServiceType>> findSpecializationsAndServicesForUserId(Long id) {
        List<UserServicesSpecializations> servicesAndSpecializations = userServicesSpecializationsRepository.findServicesBySpecializationsForUserId(id);
        Map<Specialization, List<ServiceType>> servicesMap = new HashMap<>();
        for (UserServicesSpecializations uss : servicesAndSpecializations) {
            if (servicesMap.containsKey(uss.getSpecialization())) {
                servicesMap.get(uss.getSpecialization()).add(uss.getService());
            } else {
                List<ServiceType> services = new ArrayList<>();
                services.add(uss.getService());
                servicesMap.put(uss.getSpecialization(), services);
            }
        }
        return servicesMap;
    }

    public void save(UserServicesSpecializations uss) {
        userServicesSpecializationsRepository.save(uss);
    }

    public void remove(long id) {
        userServicesSpecializationsRepository.removeById(id);
    }

    public UserServicesSpecializations findByServiceAndSpec(long serviceId, long specId) {
        return userServicesSpecializationsRepository.findByServiceIdAndSpecializationId(serviceId, specId);
    }

    public List<ServiceType> findAllActiveServicesForSelectedSpecialization(Specialization spec) {
        return userServicesSpecializationsRepository.findAllActiveServicesForSelectedSpecialization(spec);
    }

    public List<UserServicesSpecializations> findAllForSelectedServiceAndSpecialization(Specialization spec, ServiceType serv) {
        return userServicesSpecializationsRepository.findAllForSelectedServiceAndSpecialization(spec, serv);
    }

    public Map<Long, List<ZonedDateTime>> calculateAvailableUserSlotsMap(List<UserServicesSpecializations> filteredUserSpecServ) {
        Map<Long, List<ZonedDateTime>> availableUsersSlotsMap = new HashMap<>();
        for (UserServicesSpecializations uss : filteredUserSpecServ) {
            long currentPhysicianId = uss.getUser().getId();
            availableUsersSlotsMap.put(currentPhysicianId, physicianScheduleService.calculateAvailableSlots(currentPhysicianId));
        }
        return availableUsersSlotsMap;
    }
}
