package com.dbdb.dbdb.component;

import com.dbdb.dbdb.domain.user.service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ChangePasswordService changePasswordService;

    // Enable scheduling to automatically delete numbers that have not been authenticated within 5 minutes
    @Scheduled(fixedRate = 5000) // 5000ms == 5s
    public void deleteExpiredAuthNum() {
        changePasswordService.deleteExpiredAuthNum();
    }
}
