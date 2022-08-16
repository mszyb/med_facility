package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.repository.ServiceTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> findAll() {
        return serviceTypeRepository.findAll();
    }

    public void save(ServiceType serviceType) {
        serviceTypeRepository.save(serviceType);
    }

    public void remove(long id) {
        serviceTypeRepository.deleteById(id);
    }

    public ServiceType findByName(String name) {
        return serviceTypeRepository.findByName(name);
    }

    public Optional<ServiceType> findById(long id){
        return serviceTypeRepository.findById(id);
    }
}
