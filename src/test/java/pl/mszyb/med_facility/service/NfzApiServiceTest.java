package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import pl.mszyb.med_facility.DTO.JsonActiveSubstancesResponseDto;
import pl.mszyb.med_facility.DTO.Links;
import pl.mszyb.med_facility.DTO.Meta;

import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NfzApiServiceTest {

    private static final String SEARCH_VALUE = "some_value";
    private static final String PAGE_NUM = "10";
    private static final String BASE_URI = "https://some_uri";
    private JsonActiveSubstancesResponseDto dto;
    @Mock
    RestTemplate restTemplate;
    @Mock
    Model model;
    @InjectMocks
    NfzApiService nfzApiService;

    @BeforeEach
    void setUp(){
        Meta meta = new Meta();
        meta.setCount(10);
        meta.setLanguage("pl");
        meta.setLimit(20);
        meta.setPage(2);
        meta.setProvider("some_provider");
        Links link = new Links();
        link.setFirst("first_link");
        link.setLast("last_link");
        link.setSelf("self_link");
        List<String> apiData = Arrays.asList("first entry", "second entry");
        dto = new JsonActiveSubstancesResponseDto();
        dto.setLinks(link);
        dto.setMeta(meta);
        dto.setApiData(apiData);
    }

    @Test
    void callNFZApi() {
        given(restTemplate.getForObject(any(String.class), eq(JsonActiveSubstancesResponseDto.class))).willReturn(dto);
        nfzApiService.callNFZApi(SEARCH_VALUE, PAGE_NUM, model, BASE_URI);
        verify(model, times(1)).addAttribute("pageNum", PAGE_NUM);
        verify(model, times(1)).addAttribute("searchValue", SEARCH_VALUE);
        verify(model, times(1)).addAttribute("apiResponse", dto);
    }
}