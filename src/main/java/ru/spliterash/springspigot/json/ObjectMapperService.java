package ru.spliterash.springspigot.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Log4j2
@Component
@RequiredArgsConstructor
public class ObjectMapperService {
    private final Collection<JacksonModuleMarker> modules;

    public void configureObjectMapper(ObjectMapper mapper) {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        for (JacksonModuleMarker module : modules) {
            if (!(module instanceof Module)) {
                log.warn("Module " + module.getClass().getName() + " not extends from Module");
                continue;
            }
            mapper.registerModule((Module) module);
        }

    }
}
