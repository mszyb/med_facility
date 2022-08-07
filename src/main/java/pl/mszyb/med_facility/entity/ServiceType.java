package pl.mszyb.med_facility.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceType;
    @ManyToMany(mappedBy = "servicetypes")
    private List<Specialization> specializations;
    @ManyToMany(mappedBy = "services")
    private List<User> users;
}
