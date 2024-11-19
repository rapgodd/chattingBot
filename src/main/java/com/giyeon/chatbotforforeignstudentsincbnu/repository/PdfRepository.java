package com.giyeon.chatbotforforeignstudentsincbnu.repository;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.Pdf;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PdfRepository {

    private final EntityManager em;

    public Pdf findById(Long id) {
        Pdf pdf = em.createQuery("select p from Pdf p where p.id = :id", Pdf.class)
                .setParameter("id", id).getSingleResult();
        return pdf;
    }


    public Long save(Pdf pdf) {
        if (pdf.getId() == null) {
            em.persist(pdf);
        } else {
            em.merge(pdf);
        }
        return pdf.getId();
    }

}
