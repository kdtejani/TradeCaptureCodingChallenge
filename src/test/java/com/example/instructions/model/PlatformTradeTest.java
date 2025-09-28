package com.example.instructions.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PlatformTradeTest {

    //{"platFormId": "ACCT999", "account": "999-975-456789", "security": "BK", "type": "BUY", "amount": 500000, "timestamp": "2025-09-28T10:10:10Z"}

    @Test
    void testValidTrade() {
        PlatformTrade trade = new PlatformTrade();
        trade.setPlatFormId("ACCT999");
        trade.setAccount("999-975-456789");
        trade.setSecurity("BK");
        trade.setType("Buy");
        trade.setAmount("500000");
        trade.setTimestamp("2025-09-28T10:10:10Z");

        assertEquals("ACCT999", trade.getPlatFormId());
        assertEquals("999-975-456789", trade.getAccount());
        assertEquals("BK", trade.getSecurity());
        assertEquals("Buy", trade.getType());
        assertEquals("500000", trade.getAmount());
        assertEquals("2025-09-28T10:10:10Z", trade.getTimestamp());
    }
}