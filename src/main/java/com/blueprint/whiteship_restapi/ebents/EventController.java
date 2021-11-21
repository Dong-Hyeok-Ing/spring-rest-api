package com.blueprint.whiteship_restapi.ebents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    //@Autowired 이벤트 Repository 10:40
    // 생성자로 사용 할 때는 생성자가 하나만 있고 생성자로 받아올 파라메터가 이미 빈으로 등록이 되어 있다면
    // 오토와이어드 어노테이션을 주지 안하도 된다. ! Spring 4.3 부터
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

//    @Autowired
//    EventRepository eventRepository;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        Event newEvent = this.eventRepository.save(event);

        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }
    

}
