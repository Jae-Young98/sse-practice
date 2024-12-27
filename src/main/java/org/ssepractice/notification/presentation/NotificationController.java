package org.ssepractice.notification.presentation;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.ssepractice.notification.application.NotificationService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> subscribe(
			@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return ResponseEntity.ok(notificationService.subscribe("test", lastEventId));
	}
}
