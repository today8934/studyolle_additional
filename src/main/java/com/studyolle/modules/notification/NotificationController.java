package com.studyolle.modules.notification;

import com.studyolle.modules.account.Account;
import com.studyolle.modules.account.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotifications(@CurrentAccount Account account, Model model) {

        List<Notification> notifications =
                notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);

        long checkedNotificationsCount = notificationRepository.countByAccountAndChecked(account, true);

        List<Notification> newStudyNotifications = notifications
                .stream()
                .filter(noti -> noti.getNotificationType().equals(NotificationType.STUDY_CREATED))
                .collect(Collectors.toList());

        List<Notification> watchingStudyNotifications = notifications
                .stream()
                .filter(noti -> noti.getNotificationType().equals(NotificationType.STUDY_UPDATED))
                .collect(Collectors.toList());

        List<Notification> eventEnrollmentNotifications = notifications
                .stream()
                .filter(noti -> noti.getNotificationType().equals(NotificationType.EVENT_ENROLLMENT))
                .collect(Collectors.toList());

        model.addAttribute("isNew", true);
        model.addAttribute("numberOfNotChecked", notifications.size());
        model.addAttribute("numberOfChecked", checkedNotificationsCount);
        model.addAttribute("notifications", notifications);
        model.addAttribute("newStudyNotifications", newStudyNotifications);
        model.addAttribute("watchingStudyNotifications", watchingStudyNotifications);
        model.addAttribute("eventEnrollmentNotifications", eventEnrollmentNotifications);

        notificationService.checked(newStudyNotifications);

        return "notification/list";
    }

    @GetMapping("/notifications/old")
    public String getOldNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications =
                notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);

        List<Notification> newStudyNotifications = notifications
                .stream()
                .filter(noti -> noti.getNotificationType().equals(NotificationType.STUDY_CREATED))
                .collect(Collectors.toList());

        List<Notification> watchingStudyNotifications = notifications
                .stream()
                .filter(noti -> noti.getNotificationType().equals(NotificationType.STUDY_UPDATED))
                .collect(Collectors.toList());

        List<Notification> eventEnrollmentNotifications = notifications
                .stream()
                .filter(noti -> noti.getNotificationType().equals(NotificationType.EVENT_ENROLLMENT))
                .collect(Collectors.toList());

        model.addAttribute("isNew", false);
        model.addAttribute("numberOfNotChecked", 0);
        model.addAttribute("numberOfChecked", notifications.size());
        model.addAttribute("notifications", notifications);
        model.addAttribute("newStudyNotifications", newStudyNotifications);
        model.addAttribute("watchingStudyNotifications", watchingStudyNotifications);
        model.addAttribute("eventEnrollmentNotifications", eventEnrollmentNotifications);


        model.addAttribute("isNew", false);
        return "notification/list";
    }

    @DeleteMapping("/notifications")
    public String deleteNotifications(@CurrentAccount Account account) {

        return "redirect:/notifications";
    }
}
