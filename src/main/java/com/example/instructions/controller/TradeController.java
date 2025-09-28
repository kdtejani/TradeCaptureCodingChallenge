package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.service.KafkaListenerService;
import com.example.instructions.service.KafkaPublisher;
import com.example.instructions.service.TradeService;
import com.example.instructions.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tradecapture")
public class TradeController {
    @Autowired
    KafkaPublisher publisher;

//    @PostMapping(value="/post")
//    public void sendMessage(@RequestParam("msg") String msg) {
//        publisher.publishToTopic(msg);
//    }
    public static final Logger logger = Logger.getLogger(TradeController.class.getName());

    @Autowired
    TradeService conversionService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyResponse> upload(@RequestParam("file") MultipartFile file) throws Exception {
        logger.info(file.getOriginalFilename());
        int dotIndex = file.getOriginalFilename().lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == 0) {
            MyResponse response = new MyResponse("This service only supprots .csv and .json file types ", 500);
            return ResponseEntity.badRequest().body(response);
        }
        String extension = file.getOriginalFilename().substring(dotIndex + 1);
        if ( extension.equals("csv")) {
            List<CanonicalTrade> canonicalTradesrades = conversionService.convertFileToCanonical(file);
            publisher.publishToTopic(canonicalTradesrades);
            MyResponse response = new MyResponse("File Uploaded successfully", 200);
            return ResponseEntity.ok().body(response);
        }
        else if ( extension.equals("json")) {
            List<CanonicalTrade> canonicalTradesrades = conversionService.convertJsonFileToCanonical(file);
            publisher.publishToTopic(canonicalTradesrades);
            MyResponse response = new MyResponse("Json File Uploaded successfully", 200);
            return ResponseEntity.ok().body(response);
        }

        MyResponse response = new MyResponse("This service only supprots .csv and .json file types ", 500);
        return ResponseEntity.ok().body(response);

    }

}
