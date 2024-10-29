package com.giyeon.chatbotforforeignstudentsincbnu.controller;

import com.giyeon.chatbotforforeignstudentsincbnu.dto.RateQNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.SuccessfulResponse;
import com.giyeon.chatbotforforeignstudentsincbnu.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RateController {
    private final RateService rateService;

    /**
     * 별점 업데이트
     * 순서(3)
     */
    @PostMapping("/rate")
    private ResponseEntity<?> rateQuestion(@RequestBody RateQNADto rateQnaDto) {
        rateService.updateRate(rateQnaDto);
        return ResponseEntity.ok(new SuccessfulResponse(200));
    }

}
