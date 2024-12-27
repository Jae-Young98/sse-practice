package org.ssepractice.notification.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.ssepractice.notification.application.dto.NotificationResponse;
import org.ssepractice.notification.domain.Notification;
import org.ssepractice.notification.domain.NotificationRepository;
import org.ssepractice.notification.event.NotificationEvent;
import org.ssepractice.sse.application.SseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final SseService sseService;

	public SseEmitter subscribe(String userId, String lastEventId) {
		SseEmitter emitter = sseService.save(userId);

		emitter.onCompletion(emitter::complete);
		emitter.onError(e -> emitter.complete());
		emitter.onTimeout(() -> {
			sseService.deleteById(userId);
		});

		Map<String, String> dummy = new HashMap<>();
		dummy.put("msg", "Successfully subscribed! [userId=" + userId + "]");
		sseService.sendToClient(emitter, userId, dummy);

		// 유실된 이벤트들을 전송한다.
		if (!lastEventId.isEmpty() && !lastEventId.equals(userId)) {
			Map<String, Object> events = sseService.findAllEventCacheStartWithId(userId);

			events.entrySet().stream()
					.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
					.forEach(entry -> sseService.sendToClient(emitter, entry.getKey(), entry.getValue()));
		}

		return emitter;
	}

	@Transactional
	public void send(NotificationEvent event) {
		Notification notification = event.toNotification();
		notificationRepository.save(notification);

		sseService.send(event.receiver(), NotificationResponse.from(notification));
	}
}
