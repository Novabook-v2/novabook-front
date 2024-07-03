package store.novabook.front.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import store.novabook.front.common.aop.CurrentUserArgumentResolver;
import store.novabook.front.common.interceptor.RefreshTokenInterceptor;
import store.novabook.front.common.security.RefreshTokenContext;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final RefreshTokenContext refreshTokenContext;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RefreshTokenInterceptor(refreshTokenContext))
			.excludePathPatterns(
				"/users/user/form/login",
				"/auth/**",
				"/",
				"/api/v1/front/new-token",
				"/**/*.css",
				"/**/*.html",
				"/**/*.js",
				"/**/*.png",
				"/**/*.jpg",
				"/**/*.jpeg",
				"/**/*.gif"
			);
	}

}