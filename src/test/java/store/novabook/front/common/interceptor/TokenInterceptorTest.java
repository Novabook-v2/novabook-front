package store.novabook.front.common.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import store.novabook.front.api.member.member.dto.GetNewTokenRequest;
import store.novabook.front.api.member.member.dto.GetNewTokenResponse;
import store.novabook.front.api.member.member.dto.request.IsExpireAccessTokenRequest;
import store.novabook.front.api.member.member.dto.response.IsExpireAccessTokenResponse;
import store.novabook.front.api.member.member.service.MemberService;
import store.novabook.front.common.exception.ErrorCode;
import store.novabook.front.common.exception.UnauthorizedException;

class TokenInterceptorTest {

	private TokenInterceptor tokenInterceptor;

	@Mock
	private MemberService memberService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		tokenInterceptor = new TokenInterceptor(memberService);
	}

	@Test
	void preHandle_WithExpiredAccessToken_ShouldReissueToken() {
		String refreshToken = "refreshToken";
		String oldAccessToken = "oldAccessToken";
		Cookie[] cookies = new Cookie[]{
			new Cookie("Refresh", refreshToken),
			new Cookie("Authorization", oldAccessToken)
		};
		when(request.getCookies()).thenReturn(cookies);
		when(memberService.isExpireAccessToken(new IsExpireAccessTokenRequest(oldAccessToken)))
			.thenReturn(new IsExpireAccessTokenResponse(true));
		when(memberService.newToken(new GetNewTokenRequest(refreshToken)))
			.thenReturn(new GetNewTokenResponse("newAccessToken"));

		boolean result = tokenInterceptor.preHandle(request, response, new Object());

		assertTrue(result);
		verify(response).addCookie(argThat(cookie ->
			"Authorization".equals(cookie.getName()) && "newAccessToken".equals(cookie.getValue())
		));
	}

	@Test
	void preHandle_WithValidAccessToken_ShouldProceed() {
		String refreshToken = "refreshToken";
		String validAccessToken = "validAccessToken";
		Cookie[] cookies = new Cookie[]{
			new Cookie("Refresh", refreshToken),
			new Cookie("Authorization", validAccessToken)
		};
		when(request.getCookies()).thenReturn(cookies);
		when(memberService.isExpireAccessToken(new IsExpireAccessTokenRequest(validAccessToken)))
			.thenReturn(new IsExpireAccessTokenResponse(false));

		boolean result = tokenInterceptor.preHandle(request, response, new Object());

		assertTrue(result);
		verify(response, never()).addCookie(any(Cookie.class));
		assertNull(request.getAttribute("reissuedAccessToken"));
	}

	@Test
	void preHandle_WithoutAuthorizationCookie_ShouldProceed() {
		Cookie refreshTokenCookie = new Cookie("Refresh", "refreshToken");
		Cookie[] cookies = new Cookie[]{refreshTokenCookie};
		when(request.getCookies()).thenReturn(cookies);

		boolean result = tokenInterceptor.preHandle(request, response, new Object());

		assertTrue(result);
		verify(response, never()).addCookie(any(Cookie.class));
		assertNull(request.getAttribute("reissuedAccessToken"));
	}

	@Test
	void preHandle_WithoutCookies_ShouldProceed() {
		when(request.getCookies()).thenReturn(null);

		boolean result = tokenInterceptor.preHandle(request, response, new Object());

		assertTrue(result);
		verify(response, never()).addCookie(any(Cookie.class));
		assertNull(request.getAttribute("reissuedAccessToken"));
	}

	@Test
	void preHandle_WithExpiredRefreshToken_ShouldThrowException() {
		String refreshToken = "expiredRefreshToken";
		String oldAccessToken = "oldAccessToken";
		Cookie[] cookies = new Cookie[]{
			new Cookie("Refresh", refreshToken),
			new Cookie("Authorization", oldAccessToken)
		};
		when(request.getCookies()).thenReturn(cookies);
		when(memberService.isExpireAccessToken(new IsExpireAccessTokenRequest(oldAccessToken)))
			.thenReturn(new IsExpireAccessTokenResponse(true));
		when(memberService.newToken(new GetNewTokenRequest(refreshToken)))
			.thenReturn(new GetNewTokenResponse("expired"));

		UnauthorizedException thrown = assertThrows(UnauthorizedException.class, this::invokePreHandle);
		assertEquals(ErrorCode.UNAUTHORIZED, thrown.getErrorCode());
	}

	private void invokePreHandle() {
		tokenInterceptor.preHandle(request, response, new Object());
	}
}
