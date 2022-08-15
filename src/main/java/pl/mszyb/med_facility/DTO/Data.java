package pl.mszyb.med_facility.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    List<String> substances;
}
