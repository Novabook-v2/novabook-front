package store.novabook.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import store.novabook.front.common.interceptor.FeignClientInterceptor;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

	private final HttpServletRequest request;

	private final HttpServletResponse response;


	@Bean
	public RequestInterceptor requestInterceptor() {
		return new FeignClientInterceptor(request, response);
	}

	// @Bean
	// public ErrorDecoder errorDecoder() {
	// 	return new CustomErrorDecoder();
	// }

}