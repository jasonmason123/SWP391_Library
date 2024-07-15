package com.springdemo.library.model.dto;

import com.springdemo.library.model.Sach;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MissingSach {
    private Sach missingSach;
    private int soLuongDaMat;
}
