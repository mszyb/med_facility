package pl.mszyb.med_facility.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class ZoneDateTimeConverter implements Converter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(String source) {
        return ZonedDateTime.parse(source);
    }
}
