package org.ssepractice.sse.domain;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseRepository {

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	public SseEmitter save(String id, SseEmitter emitter) {
		emitters.put(id, emitter);

		return emitter;
	}

	public void saveEventCache(String emitterId, Object event) {
		eventCache.put(emitterId, event);
	}

	public Optional<SseEmitter> findById(String id) {
		return Optional.ofNullable(emitters.get(id));
	}

	public Map<String, Object> findAllEventCacheStartWithId(String id) {

		return eventCache.entrySet().stream()
				.filter(entry -> entry.getKey().substring(0, entry.getKey().indexOf('_')).equals(id))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public void deleteById(String id) {
		emitters.remove(id);
	}

	public void deleteAllEventCacheStartWithId(String id) {
		eventCache.forEach((key, emitter) -> {
			if (key.substring(0, key.indexOf('_')).equals(id)) {
				eventCache.remove(key);
			}
		});
	}
}
