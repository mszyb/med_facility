package pl.mszyb.med_facility.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.*;
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
    private final SpecializationService specializationService;
    private final ServiceTypeService serviceTypeService;


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

    public void specializationToUserAssociation(String specializationName, Long userId, List<String> servicesNames, User user) throws ServiceTypeAlreadyAssignedException {
        Specialization specialization = specializationService.findByName(specializationName);
        Map<Specialization, List<ServiceType>> servicesBySpecializations = findSpecializationsAndServicesForUserId(userId);
        for (String name : servicesNames) {
            ServiceType serv = serviceTypeService.findByName(name);
            if (!servicesBySpecializations.containsKey(specialization) || !servicesBySpecializations.get(specialization).contains(serv)) {
                UserServicesSpecializations uss = new UserServicesSpecializations();
                uss.setUser(user);
                uss.setSpecialization(specialization);
                uss.setService(serv);
                save(uss);
            } else {
                throw new ServiceTypeAlreadyAssignedException("Sorry, but this service is already assigned for selected specialization");
            }
        }
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
