package com.giyeon.chatbotforforeignstudentsincbnu.service;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.QNA;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.QNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.repository.QNARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QNAService {

    private final QNARepository qnaRepository;

    public QNA saveQuestion(QNADto qnaDto) {
        QNA newQNA = QNA.createNewQNA(qnaDto);
        QNA savedQNA = qnaRepository.save(newQNA);
        return savedQNA;
    }

    public List<QNA> getAllQuestionAboveFour() {
        return qnaRepository.findByRateGreaterThanEqual(4.0);
        //벡터 유사도 계산 해야 하는 곳
    }
}
