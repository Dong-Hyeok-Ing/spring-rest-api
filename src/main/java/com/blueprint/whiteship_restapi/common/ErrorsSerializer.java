package com.blueprint.whiteship_restapi.common;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

/**
 * ErrorsSerializer를 objectmapper에 등록을 해야하는데 스프링에서 제공하는 JsonComponent 어노테이션을 사용하면 등록이 가능하다
 * 이렇게 ErrorsSerializer를 구현하게 되면 objectMapper가 ErrorsSerializer를 사용한다.
 * 언제 사용하냐면 Errors객체를 serialization할 때 사용한다.!!!!!!
 * */
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        errors.getFieldErrors().stream().forEach(e -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("field", e.getField());
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null) {
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
                }
                gen.writeEndObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        errors.getGlobalErrors().stream().forEach(e ->{
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        gen.writeEndArray();
    }
}
