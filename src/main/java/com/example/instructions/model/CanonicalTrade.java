package com.example.instructions.model;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;

@Data
public class CanonicalTrade {
    private String platform_id;
    private ArrayList<Trade> trades;

    @Data
    public static class Trade {
        private String account;
        private String security;
        private String type;
        private double amount;
        private Instant timestamp;

        public Trade(String account, String security, String type, double amount, Instant timestamp) {
            this.account = account;
            this.security = security;
            this.type = type;
            this.amount = amount;
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return timestamp.toString();
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }
    }

    public CanonicalTrade(String platform_id, ArrayList<Trade> trades) {
        this.platform_id = platform_id;
        this.trades = trades;
    }
}

