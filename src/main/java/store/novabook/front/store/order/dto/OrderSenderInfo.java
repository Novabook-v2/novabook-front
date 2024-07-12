package store.novabook.front.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderSenderInfo(
	@NotBlank
	@Size(min = 1, max = 100, message = "이름을 최소 한 글자 최대 100글자 내로 입력해주세요.")
	String name,
	@Pattern(regexp = "^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$" , message = "핸드폰 양식에 맞지 않습니다.")
	String phone
) {}
