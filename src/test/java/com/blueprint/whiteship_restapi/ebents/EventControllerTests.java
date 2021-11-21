package com.blueprint.whiteship_restapi.ebents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;


import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 요청생성 응답을 검증할 수 있음. 웹서버를 띄우지 않는다. 단위 테스트 보다는 빠르지 않다. !

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development whth spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 21, 13, 15))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 22, 13, 15))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 22, 13, 15))
                .endEventDateTime(LocalDateTime.of(2018, 11, 22, 13, 15))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 1번 출구")
                .build();
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
//                .andExpect(header().exists("Location")) 주석처리 된 것을 아래 처럼 타입 세이프 하게
//                .andExpect(header().string("Content-Type","application/hal+json" ))
/**
 *  Type Safe 하게 아래와 같이 소스를 변경했는데 정확하게 어떤게 타입 세이프한건지 잘 이해가 가지 않는다.!!
 *  그냥 HttpHeaders 제공하는 컨텐츠 타입이 잘 못 됐을 때 컴파일 시점에서 미리 알수 있기 때문에 안정성을 확보했다고
 *  보는 것을 타입 세이프 하다라고 하는 것인가.. ?
 */
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        ;
    }

}
