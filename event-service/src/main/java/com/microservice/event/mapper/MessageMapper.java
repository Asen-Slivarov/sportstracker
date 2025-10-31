package com.microservice.event.mapper;
import com.microservice.event.dto.StatsMessageDTO;
import com.client.statsclient.dto.StatsApiResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.Instant;
@Mapper(componentModel = "spring", imports = {Instant.class})
public interface MessageMapper {

    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    StatsMessageDTO toMessage(StatsApiResponseDTO response);

}
