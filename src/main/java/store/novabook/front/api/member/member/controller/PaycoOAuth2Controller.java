package store.novabook.front.api.member.member.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import store.novabook.front.api.member.member.dto.PaycoResponseValidator;
import store.novabook.front.api.member.member.dto.request.GetPaycoMembersRequest;
import store.novabook.front.api.member.member.dto.request.LinkPaycoMembersUUIDRequest;
import store.novabook.front.api.member.member.dto.request.LoginMembersRequest;
import store.novabook.front.api.member.member.dto.response.GetPaycoMembersResponse;
import store.novabook.front.api.member.member.service.MemberAuthClient;
import store.novabook.front.api.member.member.service.PaycoApiClient;
import store.novabook.front.api.member.member.service.PaycoLoginClient;
import store.novabook.front.common.exception.ErrorCode;
import store.novabook.front.common.exception.PaycoApiException;
import store.novabook.front.common.exception.UnauthorizedException;
import store.novabook.front.common.response.ApiResponse;
import store.novabook.front.common.util.LoginCookieUtil;
import store.novabook.front.common.util.dto.Oauth2Dto;

@Controller
@RequestMapping("/oauth2/payco")
@RequiredArgsConstructor
public class PaycoOAuth2Controller implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(PaycoOAuth2Controller.class);
	private final Environment environment;
	private Oauth2Dto oauth2Dto;
	private String redirectUri;

	private final PaycoLoginClient paycoLoginClient;
	private final PaycoApiClient paycoApiClient;
	private final PaycoResponseValidator paycoResponseValidator;
	private final MemberAuthClient memberAuthClient;

	private static final String REDRIRECT_LOGIN = "redirect:/login";

	@Override
	public void afterPropertiesSet() {
		RestTemplate restTemplate = new RestTemplate();
	}

	@GetMapping
	public void payco(HttpServletResponse response) {
		try {
			String redirectUrl = "https://id.payco.com/oauth2.0/authorize?"
				+ "response_type=code"
				+ "&client_id=3RD6nxfHUTIZ1sl7133gUN6"
				+ "&serviceProviderCode=FRIENDS"
				+ "&redirect_uri=https%3a%2f%2fwww.novabook.store%2foauth2%2fpayco%2fcallback"
				+ "&state=gh86qj"
				+ "&userLocale=ko_KR";
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			throw new PaycoApiException(ErrorCode.PAYCO_API_ERROR);
		}
	}

	@GetMapping("/link")
	public void linkPayco(HttpServletResponse response) {
		try {
			String redirectUrl = "https://id.payco.com/oauth2.0/authorize?"
				+ "response_type=code"
				+ "&client_id=3RD6nxfHUTIZ1sl7133gUN6"
				+ "&serviceProviderCode=FRIENDS"
				+ "&redirect_uri=https%3a%2f%2fwww.novabook.store%2foauth2%2fpayco%2flink%2fcallback"
				+ "&state=gh86qj"
				+ "&userLocale=ko_KR";
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			throw new PaycoApiException(ErrorCode.PAYCO_API_ERROR);
		}
	}

	@GetMapping("/link/callback")
	public String linkCallback(@RequestParam(value = "code", required = false) String code,
		@RequestParam(value = "state", required = false) String state,
		@RequestParam(value = "serviceExtra", required = false) String serviceExtraEncoded,
		HttpServletRequest request) {

		redirectUri = "https://www.novabook.store/oauth2/payco/link/callback";
		log.info("redirectUri : {} ", redirectUri);
		Map<String, Object> authorizationCode = paycoApiClient.getAuthorizationToken("authorization_code", oauth2Dto.clientId(),
			oauth2Dto.clientSecret(), code, redirectUri, state);
		log.info("authorizationCode : {} ", authorizationCode);

		String paycoAccessToken = (String)authorizationCode.get("access_token");
		if (Objects.isNull(paycoAccessToken)) {
			throw new PaycoApiException(ErrorCode.PAYCO_API_ERROR);
		}

		String paycoId = getPaycoId(paycoAccessToken);

		if (!logout(paycoAccessToken)) {
			throw new PaycoApiException(ErrorCode.PAYCO_API_ERROR);
		}

		Cookie[] cookies = request.getCookies();
		String accessToken = "";
		for (Cookie cookie : cookies) {
			if ("Authorization".equals(cookie.getName())) {
				accessToken = cookie.getValue();
			}
		}

		if (accessToken.isEmpty()) {
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
		}

		LinkPaycoMembersUUIDRequest linkPaycoMembersUUIDRequest = new LinkPaycoMembersUUIDRequest(accessToken, paycoId);

		ApiResponse<Void> paycoLinkResponse = memberAuthClient.paycoLink(linkPaycoMembersUUIDRequest);

		if (paycoLinkResponse == null) {
			return REDRIRECT_LOGIN;
		}

		return "redirect:/mypage";
	}

	@GetMapping("/callback")
	public String callback(@RequestParam(value = "code", required = false) String code,
		@RequestParam(value = "state", required = false) String state,
		@RequestParam(value = "serviceExtra", required = false) String serviceExtraEncoded,
		HttpServletResponse response) {

		redirectUri = "https://www.novabook.store/oauth2/payco/callback";
		log.info("redirectUri : {} ", redirectUri);
		Map<String, Object> authorizationCode = paycoApiClient.getAuthorizationToken("authorization_code", oauth2Dto.clientId(),
			oauth2Dto.clientSecret(), code, redirectUri, state);
		log.info("authorizationCode : {} ", authorizationCode);

		String paycoAccessToken = (String)authorizationCode.get("access_token");
		if (Objects.isNull(paycoAccessToken)) {
			throw new PaycoApiException(ErrorCode.PAYCO_API_ERROR);
		}

		String paycoId = getPaycoId(paycoAccessToken);

		if (!logout(paycoAccessToken)) {
			throw new PaycoApiException(ErrorCode.PAYCO_API_ERROR);
		}

		GetPaycoMembersRequest getPaycoMembersRequest = GetPaycoMembersRequest.builder()
			.paycoId(paycoId)
			.build();
		ApiResponse<GetPaycoMembersResponse> paycoMembersResponse = memberAuthClient.paycoLogin(
			getPaycoMembersRequest);

		if (paycoMembersResponse.getBody() == null) {
			return REDRIRECT_LOGIN;
		}

		String authorization = paycoMembersResponse.getBody().accessToken();
		String refresh = paycoMembersResponse.getBody().refreshToken();

		if (authorization != null && !authorization.isEmpty()) {
			String accessToken = authorization.replace("Bearer ", "");
			String refreshToken = refresh.replace("Bearer ", "");

			LoginCookieUtil.createAccessTokenCookie(response, accessToken);
			LoginCookieUtil.createRefreshTokenCookie(response, refreshToken);

		} else {
			return REDRIRECT_LOGIN;
		}

		return "redirect:/";
	}

	@PostMapping("/payco/link/login")
	public String paycoLinkLogin(@ModelAttribute LoginMembersRequest loginMembersRequest,
		HttpServletResponse response) {

		return "redirect:/";
	}

	private String getPaycoId(String paycoAccessToken) {
		String response = paycoLoginClient.login(oauth2Dto.clientId(), paycoAccessToken);
		return paycoResponseValidator.validateGetPaycoId(response).orElseThrow();
	}

	public boolean logout(String token) {

		String encodedToken = "";
		encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

		String response = paycoApiClient.logout(oauth2Dto.clientId(), oauth2Dto.clientSecret(), encodedToken);
		return paycoResponseValidator.validateLogout(response);
	}
}
