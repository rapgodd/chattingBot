package com.giyeon.chatbotforforeignstudentsincbnu.controller;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.QNA;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.QNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.SuccessfulResponseWithQuestionID;
import com.giyeon.chatbotforforeignstudentsincbnu.service.QNAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QNAController {
    private final QNAService qnaService;

    /**
     * GPT
     * 순서(1)
     */
    @PostMapping("/qna")
    private ResponseEntity<?> saveQuestion(@RequestBody QNADto qnaDto) {
        QNA savedQna = qnaService.saveQuestion(qnaDto);
        return ResponseEntity.ok(new SuccessfulResponseWithQuestionID(savedQna.getId(), 200));
    }

    /**
     * 처음 질문했을 때
     * 별점 4.0 이상 질문 다
     * 가져오기
     * 순서(2)
     */
    @GetMapping("/qna")
    private List<QNA> getQuestions() {
        List<QNA> allQuestionAboveFour = qnaService.getAllQuestionAboveFour();
        return allQuestionAboveFour;
    }

}
