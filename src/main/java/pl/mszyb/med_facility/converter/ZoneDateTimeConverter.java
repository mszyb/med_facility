package pl.mszyb.med_facility.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ZoneDateTimeConverter implements Converter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(String source) {
        LocalDateTime localDateTime = LocalDateTime.parse(source);
        return localDateTime.atZone(ZoneId.of("Europe/Warsaw"));
    }
}
