package store.novabook.front.common.config;

import feign.Response;
import feign.codec.Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class LoggingDecoder implements Decoder {

	private final Decoder delegate;

	public LoggingDecoder(Decoder delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object decode(Response response, Type type) throws IOException {
		// 응답 본문을 로깅합니다.
		String body = "";
		if (response.body() != null) {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				body = sb.toString();
				// 로깅
				System.out.println(body);
			}
		}

		// 새 Response 객체를 생성합니다.
		Response newResponse = response.toBuilder().body(body, StandardCharsets.UTF_8).build();

		return delegate.decode(newResponse, type);
	}
}