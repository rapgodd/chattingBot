package com.giyeon.chatbotforforeignstudentsincbnu.controller;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.QNA;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.QNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.SuccessfulResponseWithQuestionID;
import com.giyeon.chatbotforforeignstudentsincbnu.service.PdfService;
import com.giyeon.chatbotforforeignstudentsincbnu.service.QNAService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QNAController {
    private final QNAService qnaService;
    private final ChatClient chatClient;
    private final PdfService pdfService;


//    /**
//     * GPT 응답 생성 이후
//     * 저장하는 API
//     * 순서(1)
//     */
//    @PostMapping("/qna")
//    private ResponseEntity<?> saveQuestion(@RequestBody QNADto qnaDto) {
//        QNA savedQna = qnaService.saveQuestion(qnaDto);
//        return ResponseEntity.ok(new SuccessfulResponseWithQuestionID(savedQna.getId(), 200));
//    }
//
//    /**
//     * 처음 질문했을 때
//     * 별점 4.0 이상 질문 다
//     * 가져오기
//     * 순서(2)
//     */
//    @GetMapping("/qna")
//    private List<QNA> getQuestions() {
//        List<QNA> allQuestionAboveFour = qnaService.getAllQuestionAboveFour();
//        return allQuestionAboveFour;
//    }


    @GetMapping("/chat-gpt-v1")
    public ResponseEntity<String> chatGptV1(@RequestParam String msg) throws IOException {

        String pdfChunkSentences = pdfService.extractRelevantContent(1L, msg);

        String answer = chatClient
                .prompt()
                .system(pdfChunkSentences +
                        "너의 역할 : 너는 너의 의견이 아니라 위의 글을 기반으로 질문의 답변을 찾아 전달만 하는 chat bot이야" +
                        "주의점 : 질문자는 모두 충북대 유학생이야. 그리고 충북대학교 유학생에게 답변을 제공할 때는 반드시 질문의 내용을 정확히 이해하고, 위의 컨텍스트를 너가 읽을때 마침표가 없이 긴 문장이 존재한다면 너가 보수적으로 문장을 나눠. " +
                        "즉 아주 짧은 문장들로 너가 마침표가 없는 컨텍스트를 나눠. 그리고 두 문장을 합쳐서 한 문장으로 묶지마. " +
                        "그리고 나눈 문장들을 완전히 분리시켜 다른 문장들로 생각해. 서로 연관되어 있다고 생각하지 마. " +
                        "답변을 생성할 때, 먼저 질문과 명확히 100% 일치하는 답변에 해당하는 규정, 조건, 혹은 지침을 찾고 그것을 답변으로 생성해.")
                .user(msg)
                .call()
                .content();

//        HashMap<String, String> response = new HashMap<>();
//        response.put("answer", answer);
//        response.put("pdfChunkSentences", pdfChunkSentences);
        return ResponseEntity.ok(answer);

    }



}
