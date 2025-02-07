package store.novabook.front.api.order.naver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import store.novabook.front.common.util.dto.NaverSearchDto;

@Service
@RequiredArgsConstructor
public class BookSearchService {

	private final NaverBookSearchApiClient naverBookSearchApiClient;

	@Value("${NAVER-SEARCH-PASSWORD}")
	private String NaverSearchApiPassword;

	@Value("${NAVER-SEARCH-ID}")
	private String NaverSearchApiId;

	public String searchBooks(String query) {
		return naverBookSearchApiClient.getSearch(NaverSearchApiId, NaverSearchApiPassword, query, 5, 1);
	}
}
