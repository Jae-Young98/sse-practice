package org.ssepractice.notification.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationViewController {

	@GetMapping("")
	public String main() {
		return "sse";
	}
}
