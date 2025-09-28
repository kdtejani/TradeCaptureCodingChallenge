package com.example.instructions.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CanonicalTradeTest {

    @Test
    void testValidTrade() {
        Instant instance = Instant.parse("2025-09-28T10:10:10Z");
        CanonicalTrade.Trade trade = new CanonicalTrade.Trade("999-975-456789", "BK", "Buy", 500000, instance);
        //assertTrue(trade.isValid());
        assertEquals("999-975-456789", trade.getAccount());
        assertEquals("BK", trade.getSecurity());
        assertEquals("Buy", trade.getType());
        assertEquals(500000, trade.getAmount());

        assertTrue("2025-09-28T10:10:10Z".equals(trade.getTimestamp()));
    }
}