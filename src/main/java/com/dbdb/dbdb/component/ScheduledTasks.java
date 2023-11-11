package com.dbdb.dbdb.component;

import com.dbdb.dbdb.service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ChangePasswordService changePasswordService;

    // 5분 이내로 인증을 진행하지 않은 번호를 자동으로 삭제하는 스케줄링 실행
    @Scheduled(fixedRate = 5000) // 5000 밀리초 == 5초
    public void deleteExpiredAuthNum() {
        changePasswordService.deleteExpiredAuthNum();
    }
}
