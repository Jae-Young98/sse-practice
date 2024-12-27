package org.ssepractice.notification.event;

import org.ssepractice.notification.domain.Notification;

public record NotificationEvent(
		String receiver,
		String title,
		String content,
		String uri
) {

	public Notification toNotification() {
		return new Notification(receiver, title, content, uri);
	}
}
