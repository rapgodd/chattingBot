package com.giyeon.chatbotforforeignstudentsincbnu.domain;

import com.giyeon.chatbotforforeignstudentsincbnu.dto.QNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.RateQNADto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class QNA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    private LocalDateTime whenqna;
    private String question;
    private String answer;
    private int phone;
    private double rate;

    //두개의 필드 추가
    private int total_satisfaction_rating;
    private int rateCount;

    public static QNA createNewQNA(QNADto qnaDto){
        QNA qna = new QNA();
        qna.question = qnaDto.getQuestion();
        qna.answer = qnaDto.getAnswer();
        qna.phone = qnaDto.getPhone();
        qna.total_satisfaction_rating = 23;
        qna.rateCount = 5;
        qna.whenqna = LocalDateTime.now();
        qna.rate = (double) qna.getTotal_satisfaction_rating() / qna.getRateCount();
        return qna;
    }

    public QNA updateRate(RateQNADto rateQNADto){
        this.total_satisfaction_rating += rateQNADto.getRate();
        this.rateCount++;
        this.rate = (double) this.total_satisfaction_rating / this.rateCount;
        return this;
    }


}
