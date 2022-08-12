package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.UserSpecializationService;
import pl.mszyb.med_facility.repository.SpecializationRepository;
import pl.mszyb.med_facility.repository.UserSpecializationServiceRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSpecServ_Service {

    private final UserSpecializationServiceRepository userSpecializationServiceRepository;
    private final SpecializationRepository specializationRepository;

    public UserSpecServ_Service(UserSpecializationServiceRepository userSpecializationServiceRepository, SpecializationRepository specializationRepository) {
        this.userSpecializationServiceRepository = userSpecializationServiceRepository;
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> findSpecializationsForUserId(long id) {
        List<Long> identifiers = userSpecializationServiceRepository.findSpecializationForUserId(id);
        List<Specialization> userSpecializations = new ArrayList<>();
        for (Long specId : identifiers) {
            Optional<Specialization> us = specializationRepository.findById(specId);
            us.ifPresent(userSpecializations::add);
        }
        return userSpecializations;
    }
}
