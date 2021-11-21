package com.blueprint.whiteship_restapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WhiteshipRestapiApplication {

    //주입 받은 의존성을 빈으로 등록해서 사용
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(WhiteshipRestapiApplication.class, args);
        System.out.println(" 테스트 입니다. = ");
    }

}
