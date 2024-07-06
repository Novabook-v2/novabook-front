package store.novabook.front.store.cart.controller;

import static store.novabook.front.common.util.CookieUtil.*;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import store.novabook.front.api.cart.dto.request.CreateCartBookListRequest;
import store.novabook.front.common.security.aop.CurrentMembers;
import store.novabook.front.common.util.CookieUtil;
import store.novabook.front.store.cart.hash.RedisCartHash;
import store.novabook.front.store.cart.service.CartService;
import store.novabook.front.store.cart.service.RedisCartService;

@RequestMapping("/carts")
@Controller
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final RedisCartService redisCartService;

	//장바구니 버튼 클릭했을 때
	@GetMapping
	public String getCartBookAll(
		@CookieValue(name = GUEST_COOKIE_NAME, required = false) Cookie guestCookie,
		@CurrentMembers Long memberId,
		HttpServletResponse response,
		Model model) {

		//로그인되어 있을때
		if (Objects.nonNull(memberId) && Objects.isNull(guestCookie)) {
			if (!redisCartService.existsCart(memberId)) {
				redisCartService.creatCart(memberId);
			}
			model.addAttribute("cart", redisCartService.getCartList(memberId));

			return "store/cart/cart_list";
		}

		// 로그인 되어있는데 비회원 쿠키가 존재할 경우
		if (Objects.nonNull(memberId) && Objects.nonNull(guestCookie)) {
			RedisCartHash redisCartHash = redisCartService.getCartList(guestCookie.getValue());
			if (!redisCartService.existsCart(memberId)) {
				redisCartService.creatCart(memberId);
			}
			if (Objects.nonNull(redisCartHash.cartBookList())) {
				cartService.addCartBooks(new CreateCartBookListRequest(redisCartHash.cartBookList()));
				redisCartService.addCartBooks(memberId, new CreateCartBookListRequest(redisCartHash.cartBookList()));
			}
			redisCartService.deleteCart(guestCookie.getValue());
			CookieUtil.deleteGuestCookie(response);
			model.addAttribute("cart", cartService.getCartList());
			return "store/cart/cart_list";
		}

		// 비회원이면서 장바구니에 어떤 상품도 담겨있지 않은 상태라면 비회원용 장바구니 UUID를 발급해서 넣어준다.
		if (Objects.isNull(guestCookie)) {
			String uuid = String.valueOf(UUID.randomUUID());
			CookieUtil.createGuestCookie(response, uuid);
		} else {
			String uuid = guestCookie.getValue();
			model.addAttribute("cart", redisCartService.getCartList(uuid));
		}



		return "store/cart/cart_list";
	}

	@GetMapping("/delete/{bookId}")
	public String deleteCart(
		@PathVariable Long bookId,
		@CookieValue(name = GUEST_COOKIE_NAME, required = false) Cookie guestCookie,
		@CurrentMembers Long memberId) {
		if (Objects.nonNull(memberId)) {
			redisCartService.deleteCartBook(memberId, bookId);
			cartService.deleteCartBook(bookId);

		} else if (Objects.nonNull(guestCookie)) {
			redisCartService.deleteCartBook(guestCookie.getValue(), bookId);

		}

		return "redirect:/carts";
	}
}
