package com.blueprint.whiteship_restapi.ebents;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Entity
//@Data Entity에다가는 데이터 에노테이션을 만들지 않는다. !  "상호참조가 일어난다." 스텍오버 플로어가 발생할 수 있다.
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        //Update free
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        }else{
            this.free = false;
        }
        //Update offline
        if(this.location == null || this.location.isBlank()) { //이거 왜 안되지.. ?
            this.offline = false;
        }else{
            this.offline = true;
        }
    }
}
