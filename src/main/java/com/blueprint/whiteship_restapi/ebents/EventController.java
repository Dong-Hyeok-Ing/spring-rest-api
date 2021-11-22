package com.blueprint.whiteship_restapi.ebents;

import org.modelmapper.ModelMapper;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository; // interface는 전부 빈으로 등록되나. ?
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    //@Autowired 이벤트 Repository 10:40
    // 생성자로 사용 할 때는 생성자가 하나만 있고 생성자로 받아올 파라메터가 이미 빈으로 등록이 되어 있다면
    // 오토와이어드 어노테이션을 주지 안하도 된다. ! Spring 4.3 부터
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

//    @Autowired
//    EventRepository eventRepository;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
//            응답에 에러값을 보내주고 싶을 경우
//            return ResponseEntity.badRequest().build();
//            이렇게 하면 될꺼 같지만 안된다. ! 이유: Errors는 자바빈 스팩을 준수하지 않기 때문이다.!
//TODO ->> objectMapper에 여러 serializer가 등록이 되어있는데 객체를 json으로 변환할 때
// objectMapper가 Beanserializer를 사용해서 json으로 변환을 한다.
// Event 객체가 json으로 변환이 가능 했던건 자바빈 스팩을 준수 했기 때문이고
// Errors 는 자바빈 스팩을 준수하지 않았기 때문에 변환을 시도하면 에러가 발생한다. 문제 해결방법.! ErrorsSerializer 생성
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }
}
