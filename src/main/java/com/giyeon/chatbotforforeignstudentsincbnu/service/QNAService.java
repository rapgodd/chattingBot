package com.giyeon.chatbotforforeignstudentsincbnu.service;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.QNA;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.QNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.dto.RateQNADto;
import com.giyeon.chatbotforforeignstudentsincbnu.repository.QNARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 기존 QNA 가져오고
     * 이거 업데이트 한다음에 다시 저장
     * @param rateQnaDto
     */
    @Transactional
    public void updateRate(RateQNADto rateQnaDto) {

        QNA qna = qnaRepository.findById(rateQnaDto.getId()).get();
        QNA updateRate = qna.updateRate(rateQnaDto);

    }
}
