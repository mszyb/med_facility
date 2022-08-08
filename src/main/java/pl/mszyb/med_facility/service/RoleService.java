package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Role;
import pl.mszyb.med_facility.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService (RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String roleName){
        return roleRepository.findByRoleName(roleName);
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }
}
