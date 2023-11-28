package com.dbdb.dbdb.component;

import com.dbdb.dbdb.domain.user.service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ChangePasswordService changePasswordService;

    // 생성한 인증 번호가 5분이 되면 자동으로 만료(DB에서 인증번호 삭제)
    @Scheduled(fixedRate = 5000)
    public void deleteExpiredAuthNum() {
        changePasswordService.deleteExpiredAuthNum();
    }
}
