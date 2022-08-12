package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.UserServicesSpecializations;
import pl.mszyb.med_facility.repository.SpecializationRepository;
import pl.mszyb.med_facility.repository.UserServicesSpecializationsRepository;

import java.util.*;

@Service
public class UserSpecServ_Service {

    private final UserServicesSpecializationsRepository userServicesSpecializationsRepository;
    private final SpecializationRepository specializationRepository;

    public UserSpecServ_Service(UserServicesSpecializationsRepository userServicesSpecializationsRepository, SpecializationRepository specializationRepository) {
        this.userServicesSpecializationsRepository = userServicesSpecializationsRepository;
        this.specializationRepository = specializationRepository;
    }

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
                ArrayList<ServiceType> services = new ArrayList<>();
                services.add(uss.getService());
                servicesMap.put(uss.getSpecialization(), services);
            }
        }
        return servicesMap;
    }

    public void save(UserServicesSpecializations uss){
        userServicesSpecializationsRepository.save(uss);
    }
}
