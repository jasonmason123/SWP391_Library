package com.springdemo.library.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springdemo.library.model.dto.CartItemDto;

import java.util.Map;
import java.util.regex.Pattern;

public class Constants {
    public static final String JWT_COOKIE_NAME = "saved_user_session";
    public static final String JWT_SECRET = "0b2628d79427e6bce5d7313e7219bc9b8b98c2e5cb74e79135cf68fe7a18e9b1";
    public static final long JWT_EXPIRATION = 604800000L; //1 week
    public static final String CONTEXT_PATH = "localhost:8080/Library";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_SOCCCD_REGEX =
            Pattern.compile("^(?=.{9,12}$)\\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_SODIENTHOAI_REGEX =
            Pattern.compile("^(?=.{10}$)\\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^(?=.{5,20}$)[a-zA-Z0-9_-]+$", Pattern.CASE_INSENSITIVE);
    public static final String DATA_EXISTED = "existed";
    public static final String DATA_NOT_EXIST = "notExist";
    public static final String DATA_PATTERN_UNMATCHED = "unmatched";
    public static final String TOKEN_USER_ID_CLAIM = "id";
    public static final String TOKEN_USERNAME_CLAIM = "name";
    public static final int MAXIMUM_NUMBER_OF_BOOKS_BORROWED = 5;
}
