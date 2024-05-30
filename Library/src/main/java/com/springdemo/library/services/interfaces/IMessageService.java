package com.springdemo.library.services.interfaces;

public interface IMessageService<ContentDto> {
    boolean sendToUser(ContentDto contentDto);
}
