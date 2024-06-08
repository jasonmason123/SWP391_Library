package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmailDetailsDto {
    private String recipient;
    private String messageBody;
    private String subject;
    private String attachmentPath; //path
}
