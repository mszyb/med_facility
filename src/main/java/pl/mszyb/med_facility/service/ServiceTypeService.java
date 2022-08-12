package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.repository.ServiceTypeRepository;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService (ServiceTypeRepository serviceTypeRepository){
        this.serviceTypeRepository = serviceTypeRepository;
    }

}
