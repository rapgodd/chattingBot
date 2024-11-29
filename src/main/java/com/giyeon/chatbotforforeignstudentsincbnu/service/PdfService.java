package com.giyeon.chatbotforforeignstudentsincbnu.service;

import com.giyeon.chatbotforforeignstudentsincbnu.domain.Pdf;
import com.giyeon.chatbotforforeignstudentsincbnu.repository.PdfRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PdfService {

    private final PdfRepository pdfRepository;
    private final ChatClient chatClient;


    public String extractRelevantContent(Long pdfId, String query) throws IOException {
        Pdf pdfFile = pdfRepository.findById(pdfId);

        PDDocument document = Loader.loadPDF(pdfFile.getFileData());
        PDFTextStripper stripper = new PDFTextStripper();
        String content = stripper.getText(document);

        // PDF 내용을 문장 단위로 나눔

        List<String> chunks = new ArrayList<>();
        int chunkSize = 1000;
        for (int i = 0; i < content.length(); i += chunkSize) {
            int end = Math.min(content.length(), i + chunkSize);
            chunks.add(content.substring(i, end).replace("\n", ""));
        }
        JaccardSimilarity similarity = new JaccardSimilarity();

        // 질문과 가장 유사한 문장 찾기
        String bestMatch = null;
        int bestMatchIndex = -1;
        double highestScore = 0;

        for (int i = 0; i < chunks.size(); i++) {
            double score = similarity.apply(query, chunks.get(i));
            if (score > highestScore) {
                highestScore = score;
                bestMatch = chunks.get(i);
                bestMatchIndex = i;
            }
        }

        StringBuilder relevantContent = new StringBuilder();
        if (bestMatchIndex != -1) {
            // 앞뒤 10문장을 포함하여 추출
            int startIndex = Math.max(bestMatchIndex - 3, 0); // 최소 인덱스는 0
            int endIndex = Math.min(bestMatchIndex + 3, chunks.size() - 1); // 최대 인덱스는 문장 개수 - 1

            // 선택된 문장들을 합쳐서 반환
            for (int i = startIndex; i <= endIndex; i++) {
                relevantContent.append(chunks.get(i)); // 문장 구분자 "." 추가
            }
        }
        return relevantContent.toString();
    }

    @Transactional
    public Long savePdf(MultipartFile file) throws IOException {
        Pdf pdf = new Pdf();
        pdf.setFileData(file.getBytes());
        return pdfRepository.save(pdf);
    }


}
