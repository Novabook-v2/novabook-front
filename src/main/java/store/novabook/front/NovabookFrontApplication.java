package store.novabook.front;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

@EnableFeignClients
@SpringBootApplication
public class NovabookFrontApplication {
	public static final String APPLICATION_LOCATIONS =
		"spring.config.location=" + "classpath:application.yml," + "classpath:deployment-application.yml";

	public static void main(String[] args) {
		System.out.println("2월 11일 14:42");
		new SpringApplicationBuilder(NovabookFrontApplication.class).properties(APPLICATION_LOCATIONS).run(args);
	}
}
