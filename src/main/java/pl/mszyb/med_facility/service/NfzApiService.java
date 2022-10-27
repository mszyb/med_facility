package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import pl.mszyb.med_facility.DTO.JsonActiveSubstancesResponseDto;

@Service
public class NfzApiService {

    private static final String URI_LIMIT_STRING = "&limit=10&format=json&name=";
    RestTemplate restTemplate;

    public NfzApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void callNFZApi(String searchValue, String pageNum, Model model, String baseUri) {
        if (Integer.parseInt(pageNum) < 1) {
            pageNum = "1";
        }
        String uri = baseUri + pageNum + URI_LIMIT_STRING + searchValue;
        JsonActiveSubstancesResponseDto jsonString = restTemplate.getForObject(uri, JsonActiveSubstancesResponseDto.class);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("apiResponse", jsonString);
    }
}
