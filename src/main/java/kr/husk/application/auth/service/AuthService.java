package kr.husk.application.auth.service;

import kr.husk.application.auth.dto.SendAuthCodeDto;
import kr.husk.application.auth.dto.VerifyAuthCodeDto;
import kr.husk.domain.auth.repository.AuthCodeRepository;
import kr.husk.domain.auth.service.UserService;
import kr.husk.infrastructure.config.AuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthConfig authConfig;
    private final JavaMailSender mailSender;
    private final UserService userService;
    private final AuthCodeRepository authCodeRepository;

    @Transactional
    public SendAuthCodeDto.Response sendAuthCode(SendAuthCodeDto.Request dto) {
        if (userService.isExist(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String authCode = generateAuthCode();
        authCodeRepository.create(dto.getEmail(), authCode, authConfig.getCodeExpiration());

        try {
            sendEmail(dto.getEmail(), authCode);
            log.info("인증 코드가 성공적으로 전송되었습니다. 이메일: {}", dto.getEmail());
        } catch (Exception e) {
            log.error("이메일 전송 실패. 이메일: {}", dto.getEmail(), e);
            throw new RuntimeException("이메일 전송에 실패했습니다.");
        }

        return SendAuthCodeDto.Response.of("인증 코드가 성공적으로 전송되었습니다.");
    }

    @Transactional
    public VerifyAuthCodeDto.Response verifyAuthCode(VerifyAuthCodeDto.Request dto) {
        String savedCode = authCodeRepository.read(authConfig.getKeyPrefix() + dto.getEmail());
        if (savedCode != null && savedCode.equals(dto.getAuthCode())) {
            authCodeRepository.delete(authConfig.getKeyPrefix() + dto.getEmail());
            return VerifyAuthCodeDto.Response.of(true);
        }
        return VerifyAuthCodeDto.Response.of(false);
    }

    private String generateAuthCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendEmail(String to, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[HUSK] 회원가입을 위한 인증코드 발송");
        message.setText("인증코드: " + authCode);
        mailSender.send(message);
    }
}
