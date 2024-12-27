package org.ssepractice.notification.application.dto;

import org.ssepractice.notification.domain.Notification;

public record NotificationResponse(
		String receiver,
		String title,
		String content,
		String uri,
		boolean isRead
) {

	public static NotificationResponse from(Notification notification) {
		return new NotificationResponse(
				notification.getReceiver(),
				notification.getTitle(),
				notification.getContent(),
				notification.getUri(),
				notification.isRead()
		);
	}
}
