package pl.mszyb.med_facility.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JsonActiveSubstancesResponseDto {
    @JsonProperty("meta")
    Meta meta;
    @JsonProperty("links")
    Links links;
    @JsonProperty("data")
    List<String> substances;
}
