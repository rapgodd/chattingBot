package com.giyeon.chatbotforforeignstudentsincbnu.controller;

import com.giyeon.chatbotforforeignstudentsincbnu.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatClient chatClient;
    private final PdfService pdfService;

    @GetMapping
    public String chatForm() {
        return "chat"; // templates/chat.html을 렌더링
    }

    @PostMapping
    public String chatSubmit(@RequestParam("message") String msg, Model model) throws IOException {
        String pdfChunkSentences = pdfService.extractRelevantContent(1L, msg);
        String userMessage = "컨텍스트:\n" + pdfChunkSentences + "\n\n질문:\n" + msg;


        String answer = chatClient
                .prompt()
                .system("주의점 : 질문자는 모두 충북대 유학생이야. 그리고 충북대학교 유학생에게 답변을 제공할 때는 반드시 질문의 내용을 정확히 이해하고, 컨텍스트를 너가 읽을때 마침표가 없이 긴 문장이 존재한다면 너가 보수적으로 문장을 나누고 주어진 컨텍스트도 완벽히 이해해. " +
                        "즉 아주 짧은 문장들로 너가 마침표가 없는 컨텍스트를 나눠. 그리고 두 문장을 합쳐서 한 문장으로 묶지마. " +
                        "그리고 나눈 문장들을 완전히 분리시켜 다른 문장들로 생각해. 서로 연관되어 있다고 생각하지 마. " +
                        "답변을 생성할 때, 먼저 질문과 명확히 100% 일치하는 답변에 해당하는 규정, 조건, 혹은 지침을 찾고 그것을 기반으로만(다른거 참고 하지마) 답변을 생성해."+ "\n컨텍스트:"+pdfChunkSentences)
                .user(msg)
                .call()
                .content();

        model.addAttribute("question", msg);
        model.addAttribute("answer", answer);

        return "chat"; // 동일한 뷰로 반환하여 채팅 내용을 표시
    }

}