package store.novabook.front.store.order.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderAddressInfoTest {


	@Test
	void testOrderAddressInfoValid() {
		// Arrange
		String zipCode = "12345";
		String streetAddress = "123 Main St";
		String detailAddress = "Apt 4B";

		// Act
		OrderAddressInfo orderAddressInfo = OrderAddressInfo.builder()
			.zipCode(zipCode)
			.streetAddress(streetAddress)
			.detailAddress(detailAddress)
			.build();

		// Assert
		assertThat(orderAddressInfo).isNotNull();
		assertThat(orderAddressInfo.zipCode()).isEqualTo(zipCode);
		assertThat(orderAddressInfo.streetAddress()).isEqualTo(streetAddress);
		assertThat(orderAddressInfo.detailAddress()).isEqualTo(detailAddress);
	}

}
