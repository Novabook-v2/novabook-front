package store.novabook.front.api.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateBookRequest(
	@NotNull(message = "아이디 값은 필수 입력값입니다.")
	Long id,
	@NotNull(message = "BookStatusId 값은 필수 입력값입니다.")
	Long bookStatusId,
	@NotNull(message = "책 재고값은 필수 입력값입니다.")
	Integer inventory,
	@NotNull(message = "책 가격값은 필수 입력값입니다.")
	Long price,
	@NotNull(message = "책 할인가격값은 필수 입력값입니다.")
	Long discountPrice,
	@NotNull(message = "책 포장여부값은 필수 입력값입니다.")
	Boolean isPackaged) {
}
