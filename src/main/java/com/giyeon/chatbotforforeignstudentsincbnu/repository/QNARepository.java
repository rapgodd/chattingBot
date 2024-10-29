package com.giyeon.chatbotforforeignstudentsincbnu.repository;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.QNA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QNARepository extends JpaRepository<QNA, Long> {
    List<QNA> findByRateGreaterThanEqual(double v);
}
