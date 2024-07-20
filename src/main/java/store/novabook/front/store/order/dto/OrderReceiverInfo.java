package store.novabook.front.store.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrderReceiverInfo(
	@Valid
	OrderAddressInfo orderAddressInfo,
	@NotBlank
	@Size(min = 1, max = 100, message = "이름을 최소 한 글자 최대 100글자 내로 입력해주세요.")
	String name,
	@Pattern(regexp = "^01[016-9](\\d{3,4})(\\d{4})$", message = "핸드폰 양식에 맞지 않습니다.")
	String phone,
	@Email(message = "이메일 형식에 맞지 않습니다.")
	String email
) {
}
