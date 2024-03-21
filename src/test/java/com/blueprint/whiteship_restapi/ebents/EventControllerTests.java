package com.blueprint.whiteship_restapi.ebents;

import com.blueprint.whiteship_restapi.common.RestDocsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 요청생성 응답을 검증할 수 있음. 웹서버를 띄우지 않는다. 단위 테스트 보다는 빠르지 않다. !

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
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
        /**
         * 아래 목킹한 save(event) event 오브젝트가 서로 다르다.! 그래서 null 포인트가 떨어진다. !
         * controller에서 저장한 객체는 메서드 안에서 새로생성한 객체이기 때문에 테스트 코드에 작성한
         * 객체와 같지 않다. ?
         * 그래서 목킹한게 적용이 되지 않아서 목 객체가 리턴한 Null이 리턴이 됐고 그래서 널포인트익셉션이 발생했다.
         * 목킹했을 때 파라메터에 event오브젝트가 있어야 이벤트의 event 오브젝트를 리턴하는데 이 경우가 아니다. !
         * 목킹을 안한다. !
         * */

        mockMvc.perform(post("/api/events/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-Event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin Enrollment of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollment of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("date time of location of new event"),
                                fieldWithPath("basePrice").description("date time of BasePrice of new event"),
                                fieldWithPath("maxPrice").description("date time of maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("response content type")
                        ),
                        /*relaxedResponseFields( // relaxed 문서일부만 테스트 할 수 있다. 단 정확한 문서를 생성하지 못한다.
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin Enrollment of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollment of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("date time of location of new event"),
                                fieldWithPath("basePrice").description("date time of BasePrice of new event"),
                                fieldWithPath("maxPrice").description("date time of maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it tells if this event is free"),
                                fieldWithPath("offline").description("it tells if this event is offline"),
                                fieldWithPath("eventStatus").description("it tells if this event is eventStatus")
                        )*/
                        responseFields( // relaxed 문서일부만 테스트 할 수 있다. 단 정확한 문서를 생성하지 못한다.
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin Enrollment of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollment of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("date time of location of new event"),
                                fieldWithPath("basePrice").description("date time of BasePrice of new event"),
                                fieldWithPath("maxPrice").description("date time of maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it tells if this event is free"),
                                fieldWithPath("offline").description("it tells if this event is offline"),
                                fieldWithPath("eventStatus").description("it tells if this event is eventStatus"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query"),
                                fieldWithPath("_links.update-event.href").description("link to update")
                        )
                ));
//                .andExpect(header().exists("Location")) 주석처리 된 것을 아래 처럼 타입 세이프 하게
//                .andExpect(header().string("Content-Type","application/hal+json" ))
/**
 *  Type Safe 하게 아래와 같이 소스를 변경했는데 정확하게 어떤게 타입 세이프한건지 잘 이해가 가지 않는다.!!
 *  그냥 HttpHeaders 제공하는 컨텐츠 타입이 잘 못 됐을 때 컴파일 시점에서 미리 알수 있기 때문에 안정성을 확보했다고
 *  보는 것을 타입 세이프 하다라고 하는 것인가.. ?
 */
    }

    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우 에러 발생하는 테스트")
    void createEvent_Bad_req() throws Exception {
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
                .eventStatus(EventStatus.PUBLISHED)
                .build();
        /**
         * 아래 목킹한 save(event) event 오브젝트가 서로 다르다.! 그래서 null 포인트가 떨어진다. !
         * controller에서 저장한 객체는 메서드 안에서 새로생성한 객체이기 때문에 테스트 코드에 작성한
         * 객체와 같지 않다. ?
         * 그래서 목킹한게 적용이 되지 않아서 목 객체가 리턴한 Null이 리턴이 됐고 그래서 널포인트익셉션이 발생했다.
         * 목킹했을 때 파라메터에 event오브젝트가 있어야 이벤트의 event 오브젝트를 리턴하는데 이 경우가 아니다. !
         * 목킹을 안한다. !
         * */

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 비어있는 경우 발생하는 에러 테스트")
    void createEvent_Bad_req_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        );
    }


    @Test
    @DisplayName("입력 값이 잘 못된 경우 발생하는 테스트")
    void createEvent_Bad_req_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development whth spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 21, 13, 15))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 22, 13, 15))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 30, 13, 15))
                .endEventDateTime(LocalDateTime.of(2018, 11, 29, 13, 15))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 1번 출구")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print()) // 응답 바디가 어떻게 찍히는지 확인해 보기 위해 찍어 보았음.
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0].field").exists()) // 필드에러가 없을 경우 테스트가 깨지기 때문에 주석
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }
}
