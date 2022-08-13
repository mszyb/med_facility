package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.repository.SpecializationRepository;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    public void remove(long id) {
        specializationRepository.deleteById(id);
    }

    public void save(Specialization specialization) {
        specializationRepository.save(specialization);
    }

    public Specialization findByName(String name) {
        return specializationRepository.findByName(name);
    }
}
