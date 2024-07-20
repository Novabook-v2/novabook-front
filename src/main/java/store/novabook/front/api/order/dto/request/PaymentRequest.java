package store.novabook.front.api.order.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.front.api.order.dto.PaymentType;

@Builder
public record PaymentRequest(
	PaymentType type,
	Long memberId,
	@NotNull
	String orderCode,
	Object paymentInfo
) implements Serializable {
}