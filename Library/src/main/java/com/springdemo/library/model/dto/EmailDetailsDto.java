package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class EmailDetailsDto {
    private String recipient;
    private String messageBody;
    private String subject;
    private String attachmentPath; //path
}
