package com.example.captchabackend2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaptchaData {
    private String captcha;
    private String hiddenCaptcha;
    private String realCaptcha;
}
