package com.studyolle.modules.notification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    public void markAsRead(List<Notification> notifications) {
        notifications.stream().forEach(noti -> noti.setChecked(true));
    }
}
