package pl.mszyb.med_facility.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links {
    @JsonProperty("first")
    private String first;
    @JsonProperty("self")
    private String self;
    @JsonProperty("last")
    private String last;
}
