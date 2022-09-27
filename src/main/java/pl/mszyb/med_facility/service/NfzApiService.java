package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import pl.mszyb.med_facility.DTO.JsonActiveSubstancesResponseDto;

@Service
public class NfzApiService {

    public void callNFZApi(String searchValue, String pageNum, Model model, String baseUri) {
        if (Integer.parseInt(pageNum) < 1) {
            pageNum = "1";
        }
        String uri = baseUri + pageNum + "&limit=10&format=json&name=" + searchValue;
        RestTemplate restTemplate = new RestTemplate();
        JsonActiveSubstancesResponseDto jsonString = restTemplate.getForObject(uri, JsonActiveSubstancesResponseDto.class);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("apiResponse", jsonString);
    }
}
