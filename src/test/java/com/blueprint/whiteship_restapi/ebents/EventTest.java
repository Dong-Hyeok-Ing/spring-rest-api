package com.blueprint.whiteship_restapi.ebents;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder() {
        Event event = Event.builder()
                .name("Spring REST API")
                .description("development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree() {
        //Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isTrue();
        //Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isFalse();
        //Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    void testOffline(){
        //Given
        Event event = Event.builder()
                .location("강남역 1번 출구")
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isOffline()).isTrue();

        //Given
        event = Event.builder()
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isOffline()).isFalse();
    }
}