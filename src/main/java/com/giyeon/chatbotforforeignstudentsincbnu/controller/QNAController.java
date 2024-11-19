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


    /**
     * GPT 응답 생성 이후
     * 저장하는 API
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


    @GetMapping("/chat-gpt-v1")
    public ResponseEntity<Map<String, String>> chatGptV1(@RequestParam String msg) throws IOException {

        String pdfChunkSentences = pdfService.extractRelevantContent(1L, msg);

        String answer = chatClient
                .prompt()
                .system(pdfChunkSentences +
                        "주의점 : 질문자는 모두 충북대 유학생이야. 그리고 충북대학교 유학생에게 답변을 제공할 때는 반드시 질문의 내용을 정확히 이해하고, 컨텍스트를 너가 읽을때 마침표가 없이 긴 문장이 존재한다면 너가 보수적으로 문장을 나눠. " +
                        "즉 아주 짧은 문장들로 너가 마침표가 없는 컨텍스트를 나눠. 그리고 두 문장을 합쳐서 한 문장으로 묶지마. " +
                        "그리고 나눈 문장들을 완전히 분리시켜 다른 문장들로 생각해. 서로 연관되어 있다고 생각하지 마. " +
                        "답변을 생성할 때, 먼저 질문과 명확히 100% 일치하는 답변에 해당하는 규정, 조건, 혹은 지침을 찾고 그것을 답변으로 생성해.")
                .user(msg)
                .call()
                .content();


        HashMap<String, String> response = new HashMap<>();
        response.put("answer", answer);
        response.put("pdfChunkSentences", pdfChunkSentences);
        return ResponseEntity.ok(response);

    }




//    @GetMapping("/chat-gpt-v2")
//    public String chatGptV2(@RequestParam String msg){
//        return chatClient
//                .prompt()
//                .system("시간제취업(아르바이트) ○ 허용대상: 신청일 기준 직전학기 평균 성적이 C학점(2.0) 이상인 경우 ○ 학생 유형별 아르바이트 허용시간 - 학부 1학년 및 2학년 학생의 경우, 한국어 능력시험 3급을 보유하고 있으면 주중에는 30시간 동안 활동이 허용되며, 주말과 방학 중에는 무제한으로 활동이 허용됩니다. 만약 3급을 보유하지 않은 경우, 주중에 총 10시간까지만 활동이 허용됩니다. - 학부 3학년 및 4학년 학생의 경우, 한국어 능력시험 4급을 보유하고 있으면 주중에 30시간 동안 활동이 허용되며, 주말과 방학 중에는 무제한으로 활동이 허용됩니다. 4급을 보유하지 않은 경우에는 주중에 총 10시간까지만 활동이 허용됩니다. - 석사 과정이나 박사 과정에 있는 학생의 경우, 한국어 능력시험 4급을 보유하고 있으면 주중에 35시간 동안 활동이 허용되며, 주말과 방학 중에는 무제한으로 활동이 허용됩니다. 4급을 보유하지 않은 경우에는 주중에 총 15시간까지만 활동이 허용됩니다. ○ 제출서류 : 시간제취업확인서(고용인 및 학교 유학생담당자 확인 포함), 외국인등록증, 여권, 성적증명서, 표준근로계약서 사본(시급 및 근무내용, 시간 포함), TOPIK (또는 KIIP) 성적표, 사업자등록증 사본 ○ 신청방법 ① 시간제취업확인서 작성(대상자 인적사항 및 취업예정 근무처) ② 제출 서류 준비하여 국제교류본부 유학생담당자 방문 및 담당자 확인 ③ 출입국관리사무소 신청 (전자민원) www.hikorea.go.kr -전자민원–유학생(D-2) 및 어학연수생(D-4-1) 시간제취업 하가 (방문신고) www.hikorea.go.kr -방문예약–예약한 날짜에 출입국관리사무소 방문 ○ 허가기간: 체류기간 내에서 1년 이내, 동시에 취업할 수 있는 장소는 2곳으로 한정함 ※ 여권 등에 체류자격외 활동 허가인 날인(취업 장소 및 허가 기간 명시) ○ 허가제한 - 전문분야 활동(E-1교수, E-2회화지도, E-3연구, E-4기술지도, E-5전문직업, E-6예술흥행, E-7특정활동), 비전문취업(E-9), 선원취업(E-10) - 사용자와 직접 고용계약을 체결하지 않은 유학생(고용주와 사용자가 일치하여야 함) - 원거리 근무자(거주, 대학 소재 기준 최대 수도권 1시간 30분, 지역은 1시간 이내 거리를 적정 거리로 간주) - 연구과정(D-2-5)에 유학중인 사람 - 시간제 취업허가를 받지 않았거나 허가 조건을 위반한 경력이 있는 사람 - 다음에 해당하는 고용주 및 업종에 대해서는 허가를 제한: 과거 불법고용 등 처벌 경력으로 사증발급이 제한되는 업체 및 고용주, 제조업, 건설업(사업자등록 기준). 단, 제조업의 경우 TOPIK 4급(KIIP 4단계)이상을 소지한 경우 예외적 허용 ○ 전문분야에 대한 예외적인 허용(예시) - 통역/번역, 음식업 보조, 일반 사무보조 등 - 영어캠프(타 외국어 관련 캠프 등도 준용) 등에서 가게 판매원, 식당점원, 행사보조요원 등 활동 - 관광안내 보조 및 면세점 판매 보조 등 ○ 취업 장소 변경: 취업기관 변경 시 변경일로부터 15일 이내에 직접 방문 또는 전 자민원을 통해 취업 장소 변경 신고 ○ 시간제취업 허가 위반자 처리 기준 (거짓이나 부정한 방법으로 허가 취득 또는 허가 조건 위반) - 1차 적발 시: 법제89조에 따라 향후 1년간 시간제 취업 불허 - 2차 적발 시: 법제89조에 따라 유학기간 중 시간제 취업 불허 - 3차 적발 시: 법제89조에 따라 유학자격 취소 ※ 건설업 분야의 경우 적발횟수와 관계없이 적발 시 예외 없이 출국 명령 ○ 시간제취업허가를 받지 않고 취업한 경우 - 유학생 및 고용주 모두 법제20조 위반으로 3년 이하의 징역이나 2000만원 이하의 벌금 대상 ○ 시간제취업 허가제외 대상 및 예시 - 대상: 유학자격의 본질적 사항을 침해하지 않는 범위 내에서 일시적 사례금, 상 금, 기타 일상 생활에 수반되는 보수를 받고 행하는 활동은 허가대상에서 제외 - 예시 교내에서 유학생이 학점 취득 등을 위해 행하는 인턴쉽, 연구프로젝트 참여로 일정 수당을 받는 경우 수학 중인 학교 내 조교(수업조교 포함)·도서관 사서 등 근로 장학생으로 참 여하는 경우 가사보조인 등과 같이 직업적으로 행하는 것이 아닌 일시적인 일상가사보조 또는 사무보조 등에 따른 사례금 기타 보수를 받는 경우 직업적으로 행하는 것이 아닌 조언·감정, 행사참가, 영화 또는 방송 임시(1회 및 비연속성) 출연 등 기타 이와 유사한 활동에 따른 보수를 받는 경우" +
//                        "주의점 : 질문자는 모두 충북대 유학생이야. 그리고 충북대학교 유학생에게 답변을 제공할 때는 반드시 질문의 내용을 정확히 이해하고, 컨텍스트를 너가 읽을때 마침표가 없이 긴 문장이 존재한다면 너가 보수적으로 문장을 나눠. " +
//                        "즉 아주 짧은 문장들로 너가 마침표가 없는 컨텍스트를 나눠. 그리고 두 문장을 합쳐서 한 문장으로 묶지마. " +
//                        "그리고 나눈 문장들을 완전히 분리시켜 다른 문장들로 생각해. 서로 연관되어 있다고 생각하지 마. " +
//                        "답변을 생성할 때, 먼저 질문과 명확히 100% 일치하는 답변에 해당하는 규정, 조건, 혹은 지침을 찾고 그것을 답변으로 생성해.")
//                .user(msg)
//                .call()
//                .content();
//    }



}
