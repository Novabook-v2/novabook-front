package com.nhnacademy.viewtest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {
	@GetMapping("/")
	public String home() {
		return "order-details";
	}

	@GetMapping("/mypage/orders")
	public String test() {
		return "book_detail";
	}
}
