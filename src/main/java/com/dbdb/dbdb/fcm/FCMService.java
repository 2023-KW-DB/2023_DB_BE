package com.dbdb.dbdb.fcm;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FCMService {

    private final FCMTokenRepository fcmTokenRepository;

    public void saveToken(UserDto userDto) {
        fcmTokenRepository.saveToken(userDto);
    }

    public String getToken(String email) {
        return fcmTokenRepository.getToken(email);
    }

    public void deleteToken(String email) {
        fcmTokenRepository.deleteToken(email);
    }

    public boolean hasKey(String email) {
        return fcmTokenRepository.hasKey(email);
    }

    public void sendLogincompletedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("로그인 완료 알림")
                        .setBody("로그인 되었쥬?")
                        .build())
                .putData("title", "로그인 완료 알림")
                .putData("content", "로그인 되었쥬?")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendPurchaseCompletedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .putData("title", "구매 완료 알림")
                .putData("content", "등록하신 구매 입찰이 낙찰되었습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void send(Message message) {
        FirebaseMessaging.getInstance().sendAsync(message);
    }
}