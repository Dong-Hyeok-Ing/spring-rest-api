package com.blueprint.whiteship_restapi.ebents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
                .id(100)
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
                .free(true) //이런 값들은 계산해서 들어오는 값들 입력되는 값이 들어오면 안된다.
                .offline(false)
                .build();
        /**
         * 아래 목킹한 save(event) event 오브젝트가 서로 다르다.! 그래서 null 포인트가 떨어진다. !
         * controller에서 저장한 객체는 메서드 안에서 새로생성한 객체이기 때문에 테스트 코드에 작성한
         * 객체와 같지 않다. ?
         * 그래서 목킹한게 적용이 되지 않아서 목 객체가 리턴한 Null이 리턴이 됐고 그래서 널포인트익셉션이 발생했다.
         * 목킹했을 때 파라메터에 event오브젝트가 있어야 이벤트의 event 오브젝트를 리턴하는데 이 경우가 아니다. !
         * */
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
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
        ;
    }

}
