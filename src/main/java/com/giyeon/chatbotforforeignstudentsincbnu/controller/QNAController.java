package com.giyeon.chatbotforforeignstudentsincbnu.controller;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.QNA;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.QNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.RateQNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.SuccessfulResponse;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.SuccessfulResponseWithQuestionID;
import com.giyeon.chatbotforforeignstudentsincbnu.service.QNAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QNAController {
    private final QNAService qnaService;

    @PostMapping("/qna")
    private ResponseEntity<?> saveQuestion(@RequestBody QNADto qnaDto) {
        QNA savedQna = qnaService.saveQuestion(qnaDto);
        return ResponseEntity.ok(new SuccessfulResponseWithQuestionID(savedQna.getId(), 200));
    }

    @PostMapping("/rate")
    private ResponseEntity<?> rateQuestion(@RequestBody RateQNADto rateQnaDto) {
        qnaService.updateRate(rateQnaDto);
        return ResponseEntity.ok(new SuccessfulResponse(200));
    }
}
