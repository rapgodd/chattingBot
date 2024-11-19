package com.giyeon.chatbotforforeignstudentsincbnu.controller;

import com.giyeon.chatbotforforeignstudentsincbnu.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    // PDF 업로드 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            Long pdfId = pdfService.savePdf(file);
            return ResponseEntity.ok("PDF 파일이 성공적으로 업로드되었습니다. ID: " + pdfId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("PDF 업로드 실패: " + e.getMessage());
        }
    }

}
