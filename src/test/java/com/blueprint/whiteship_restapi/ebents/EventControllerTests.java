package com.blueprint.whiteship_restapi.ebents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 요청생성 응답을 검증할 수 있음. 웹서버를 띄우지 않는다. 단위 테스트 보다는 빠르지 않다. !

    @Autowired
    ObjectMapper objectMapper;

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

        mockMvc.perform(post("/api/events/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }



}
