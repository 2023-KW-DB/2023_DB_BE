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

    public void send(Message message) {
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    public void sendLogincompletedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("로그인 완료 알림")
                        .setBody("로그인 되었습니다.")
                        .build())
                .putData("title", "로그인 완료 알림")
                .putData("content", "로그인 되었습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendLogoutcompletedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("로그아웃 완료 알림")
                        .setBody("로그아웃 되었습니다.")
                        .build())
                .putData("title", "로그아웃 완료 알림")
                .putData("content", "로그아웃 되었습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendTicketPurchaseFailedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("이용권 구매 실패 알림")
                        .setBody("잔액이 부족하여 이용권 구매를 실패하였습니다.")
                        .build())
                .putData("title", "이용권 구매 실패 알림")
                .putData("content", "잔액이 부족하여 이용권 구매를 실패하였습니다.")
                .setToken(token)
                .build();

        send(message);
    }



    public void sendTicketPurchaseSuccessMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("이용권 구매 성공 알림")
                        .setBody("이용권 구매를 성공하였습니다.")
                        .build())
                .putData("title", "이용권 구매 성공 알림")
                .putData("content", "이용권 구매를 성공하였습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendBikeRentalSuccessMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("자전거 대여 성공 알림")
                        .setBody("자전거 대여를 성공하였습니다.")
                        .build())
                .putData("title", "자전거 대여 성공 알림")
                .putData("content", "자전거 대여를 성공하였습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendBikeRentalFailedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("자전거 대여 실패 알림")
                        .setBody("자전거 대여를 실패하였습니다.")
                        .build())
                .putData("title", "자전거 대여 실패 알림")
                .putData("content", "자전거 대여를 실패하였습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendBikeReturnSuccessMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("자전거 반납 성공 알림")
                        .setBody("자전거 반납을 성공하였습니다.")
                        .build())
                .putData("title", "자전거 반납 성공 알림")
                .putData("content", "자전거 반납을 성공하였습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendBikeReturnFailedMessage(String email) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("자전거 반납 실패 알림")
                        .setBody("자전거 반납을 실패하였습니다.")
                        .build())
                .putData("title", "자전거 반납 실패 알림")
                .putData("content", "자전거 반납을 실패하였습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendCommentLikeMessage(String email, String title) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("["+title+"]에 작성한 댓글에 좋아요")
                        .setBody("작성한 댓글에 좋아요가 눌렸습니다")
                        .build())
                .putData("title", "댓글 좋아요 알림입니다")
                .putData("content", "작성한 댓글에 좋아요가 눌렸습니다")
                .setToken(token)
                .build();

        send(message);
    }

    public void sendBoardLikeMessage(String email, String title) {
        if (!fcmTokenRepository.hasKey(email)) {
            return;
        }

        String token = fcmTokenRepository.getToken(email);
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("[" + title+ "]" +" 좋아요")
                        .setBody("작성한 글에 좋아요가 눌렸습니다")
                        .build())
                .putData("title", "글 좋아요 알림입니다")
                .putData("content", "작성한 글에 좋아요가 눌렸습니다")
                .setToken(token)
                .build();

        send(message);
    }
}