package com.example.instructions.service;

import com.example.instructions.controller.TradeController;
import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.util.TradeTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TradeService {


    private final StringHttpMessageConverter stringHttpMessageConverter;
    private final ConcurrentHashMap<String, CanonicalTrade> CanonicalTradeMap = new ConcurrentHashMap<>();
    public static final Logger logger = Logger.getLogger(TradeController.class.getName());

    public TradeService(StringHttpMessageConverter stringHttpMessageConverter) {
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }

    public ArrayList<CanonicalTrade> convertFileToCanonical(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) filename = "unknown";
        String lower = filename.toLowerCase();
        ArrayList<CanonicalTrade> trades = this.parseCsv(file);
        return trades;
    }

    private ArrayList<CanonicalTrade> parseCsv(MultipartFile file) throws Exception {
        ArrayList<CanonicalTrade> out = new ArrayList<>();
        try (CSVParser parser = CSVParser.parse(file.getInputStream(), StandardCharsets.UTF_8, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord rec : parser) {
                PlatformTrade pt = new PlatformTrade();
                pt.setPlatFormId(getValue(rec, "PlatformID"));
                pt.setAccount(getValue(rec, "Account"));
                pt.setSecurity(getValue(rec, "Security"));
                pt.setType(getValue(rec, "BuySell"));
                pt.setAmount(getValue(rec, "Amount"));
                pt.setTimestamp(getValue(rec, "TimeStamp"));
                CanonicalTrade ct = this.transformToCanonicalTrade(pt);
                out.add(ct);
                CanonicalTradeMap.putIfAbsent(ct.getPlatform_id(), ct);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return out;
    }

    private CanonicalTrade transformToCanonicalTrade(PlatformTrade pt)
    {
        //validate and transform the data element before putting into canonicalTrade object
        String account = TradeTransformer.maskPII(pt.getAccount());
        String security = pt.getSecurity().toUpperCase();
        String type = TradeTransformer.normalizeType(pt.getType());
        double amount = TradeTransformer.convertToDouble(pt.getAmount());
        Instant timestamp = TradeTransformer.DateTimeConverter(pt.getTimestamp());

        CanonicalTrade.Trade trade = new CanonicalTrade.Trade(account, security, type, amount, timestamp);
        ArrayList<CanonicalTrade.Trade> trades = new ArrayList<CanonicalTrade.Trade>();
        trades.add(trade);
        CanonicalTrade ct = new CanonicalTrade(pt.getPlatFormId(), trades);
        logger.info(ct.toString());
        return ct;

    }

    private String getValue(CSVRecord rec, String key) {
        if (rec.isMapped(key) && rec.get(key) != null && !rec.get(key).isBlank())
            return rec.get(key);
        else
            return "";
    }

    public ArrayList<CanonicalTrade> convertJsonFileToCanonical(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) filename = "unknown";
        String lower = filename.toLowerCase();
        ArrayList<CanonicalTrade> trades = this.parseJson(file);
        return trades;
    }

    private ArrayList<CanonicalTrade> parseJson(MultipartFile file) throws Exception {
        ArrayList<CanonicalTrade> out = new ArrayList<>();
        InputStream inputStream = file.getInputStream();  //   resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        PlatformTrade[] platformTradeArray = objectMapper.readValue(inputStream, PlatformTrade[].class);
        for ( PlatformTrade pt : platformTradeArray )
        {
            CanonicalTrade ct = this.transformToCanonicalTrade(pt);
            out.add(ct);
            CanonicalTradeMap.putIfAbsent(ct.getPlatform_id(), ct);
        }
        return out;
    }

    public List<CanonicalTrade> parseKafkaMessage(String msg) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PlatformTrade platformTrade = objectMapper.readValue(msg, PlatformTrade.class);
        ArrayList<CanonicalTrade> ct = new ArrayList<CanonicalTrade>();
        CanonicalTrade canonicalTrade = this.transformToCanonicalTrade(platformTrade);
        ct.add(canonicalTrade);
        CanonicalTradeMap.putIfAbsent(canonicalTrade.getPlatform_id(), canonicalTrade);
        return ct;
    }
}
