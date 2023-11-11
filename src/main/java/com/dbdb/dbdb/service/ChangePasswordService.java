package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.EmailAuthDto;
import com.dbdb.dbdb.repository.EmailAuthRepository;
import com.dbdb.dbdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChangePasswordService {

    @Autowired
    private final JavaMailSender emailSender;
    @Autowired
    private final EmailAuthRepository emailAuthRepository;
    @Autowired
    private final UserRepository userRepository;
    // 랜덤 인증 코드
    private String authNum;
    private final SpringTemplateEngine templateEngine;

    // 랜덤 인증 코드 생성
    public void createCode(String email) {
        Random random = new Random();
        authNum = String.valueOf(random.nextInt(8888)+1000); // 범위 : 1000 ~ 9999

        EmailAuthDto emailAuthEntity = new EmailAuthDto();
        emailAuthEntity.setEmail(email);
        emailAuthEntity.setAuth_num(Integer.parseInt(authNum));
        emailAuthEntity.setCreated_at(LocalDateTime.now());

        emailAuthRepository.save(emailAuthEntity);

    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(email); //인증 코드 생성
        String setFrom = "kw2023db@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; // 받는 사람
        String title = "[데베및데베시각화] 인증 코드는 " + authNum + "입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 보내는 이메일
        message.setText(setContext(authNum), "utf-8", "html");

        return message;
    }

    // 실제 메일 전송 - controller에서 호출
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {

        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }

    public String verifyCode(String email, String code) {
        EmailAuthEntity emailAuthEntity = emailSignupRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

        if (code.equals(emailAuthEntity.getAuthNum())) {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(emailAuthEntity.getAuthNumTimestamp(), now);
            //log.info("duration = {}", duration);
            //log.info("duration.toMinutes = {}", duration.toMinutes());
            if (duration.toMinutes() < 5) { // 5분 이하로 인증 코드를 맞춘 경우
                emailAuthRepository.delete(emailAuthEntity);
                return "success: correct auth code";
            }
            else{
                emailAuthRepository.delete(emailAuthEntity);
                return "failed: over 5 minute";
            }
        }
        // 인증 코드가 틀린 경우
        return "failed: not correct auth code";
    }

    public void deleteExpiredAuthNum(){
        List<EmailAuthEntity> emailAuthEntityList = emailSignupRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        for(EmailAuthEntity emailAuthEntity : emailAuthEntityList){
            Duration duration = Duration.between(emailAuthEntity.getAuthNumTimestamp(), now);

            if(duration.toMinutes() >= 5){
                emailAuthRepository.delete(emailAuthEntity);
            }
        }
    }

    public String changePassword(String email, String password){
        UserEntity userEntity = userRepository.findByEmail(email).get();

        userRepository.save(userEntity);

        return "success: change password";
    }

    public Boolean isUserTypeEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).get();

        if(userEntity.getUserType().equals(UserTypeEnum.EMAIL))
            return true;
        else
            return false;
    }

    public void deleteExistCode(String email){ // 한 유저가 2번 이상 연속으로 인증 코드를 보낼 경우에 대한 예외 처리를 위해 기존의 코드 삭제
        emailAuthRepository.deleteByEmail(email);
    }
}
