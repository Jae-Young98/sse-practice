package org.ssepractice.sse.application;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.ssepractice.notification.application.dto.NotificationResponse;
import org.ssepractice.sse.domain.SseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

	// default timeout 1h
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	private final SseRepository sseRepository;

	public SseEmitter save(String id) {
		return sseRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));
	}

	public void deleteById(String id) {
		sseRepository.deleteById(id);
	}

	public void send(String id, NotificationResponse response) {
		String emitterId = id + "_" + System.currentTimeMillis();

		sseRepository.findById(id).ifPresent(emitter -> {
			sseRepository.saveEventCache(emitterId, response);
			sendToClient(emitter, emitterId, response);
		});
	}

	public void sendToClient(SseEmitter emitter, String emitterId, Object data) {
		try {
			log.info("send to client {}:[{}", emitterId, data);
			emitter.send(SseEmitter.event()
					.id(emitterId)
					.name("sse")
					.data(data));
		} catch (IOException e) {
			log.error("IOException | IllegalStateException is occurred. ", e);
			sseRepository.deleteById(emitterId);
		}
	}

	public Map<String, Object> findAllEventCacheStartWithId(String emitterId) {
		return sseRepository.findAllEventCacheStartWithId(emitterId);
	}
}
