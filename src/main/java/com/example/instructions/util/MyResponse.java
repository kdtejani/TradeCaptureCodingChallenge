package com.example.instructions.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyResponse {
    private String message;
    private int code;

    public MyResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
