package org.ssepractice.notification.domain;

import org.ssepractice.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 구현용 단일 컬럼, ManyToOne 관계를 맺어야함.
	private String receiver;

	private String title;

	private String content;

	private String uri;

	private boolean isRead;

	public Notification(String receiver, String title, String content, String uri) {
		this.receiver = receiver;
		this.title = title;
		this.content = content;
		this.uri = uri;
		this.isRead = false;
	}

	public void read() {
		this.isRead = true;
	}
}
