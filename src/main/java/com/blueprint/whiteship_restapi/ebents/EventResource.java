package com.blueprint.whiteship_restapi.ebents;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// TODO -> 스프링 HATEOAS 버전이 올라가면서 스프링 HATEOAS의 API가 바뀌었다

/**
 * Resource -> EntityModel
 * Resources -> CollectionModel
 * PagedResrouces -> PagedModel
 * ResourceSupport -> RepresentationModel
 * assembler.toResource -> assembler.toModel
 * org.springframework.hateoas.mvc.ControllerLinkBuilder -> org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
 * MediaTypes 중에 (UTF8)인코딩이 들어간 상수 제거.
 */
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
    }
}
