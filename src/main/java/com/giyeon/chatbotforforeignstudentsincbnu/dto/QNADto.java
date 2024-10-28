package com.giyeon.chatbotforforeignstudentsincbnu.dto;

import lombok.Data;

@Data
public class QNADto {
    private String question;
    private String answer;
    private Integer phone;
}
