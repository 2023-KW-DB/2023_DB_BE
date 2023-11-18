package com.dbdb.dbdb.component;

import com.dbdb.dbdb.domain.user.service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ChangePasswordService changePasswordService;

    // 5�� �̳��� ������ �������� ���� ��ȣ�� �ڵ����� �����ϴ� �����ٸ� ����
    @Scheduled(fixedRate = 5000) // 5000 �и��� == 5��
    public void deleteExpiredAuthNum() {
        changePasswordService.deleteExpiredAuthNum();
    }
}
