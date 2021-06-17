package com.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

	@RequestMapping("/say")
//	@PreAuthorize("hasRole('ROLE_ADMIN')") // can use
//	@PreAuthorize("hasRole('ADMIN')") // can use
//	@PreAuthorize("hasAuthority('P1')") // can use
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')") // can use
//	@PreAuthorize("hasAuthority('ADMIN')") // can't use
	public String sayHello() {
		return "hello";
	}
}
