package com.giyeon.chatbotforforeignstudentsincbnu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessfulResponseWithQuestionID {
    private Long id;
    private int code;
}
