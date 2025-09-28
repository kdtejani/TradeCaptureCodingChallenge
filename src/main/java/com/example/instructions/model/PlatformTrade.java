package com.example.instructions.model;
import lombok.Data;

@Data
public class PlatformTrade {
    private String platFormId;
    private String account;
    private String security;
    private String type;
    private String amount;
    private String timestamp;

}
