package pl.mszyb.med_facility.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private List<ServiceType> servicetypes;
}
