package store.novabook.front.api.member.coupon.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import store.novabook.front.api.coupon.dto.request.DownloadCouponRequest;
import store.novabook.front.api.coupon.dto.response.GetCouponAllResponse;
import store.novabook.front.api.coupon.dto.response.GetCouponHistoryResponse;
import store.novabook.front.api.coupon.dto.response.GetUsedCouponHistoryResponse;
import store.novabook.front.api.member.coupon.dto.DownloadCouponMessageRequest;
import store.novabook.front.api.member.coupon.dto.GetCouponIdsResponse;
import store.novabook.front.api.member.member.dto.response.CreateMemberCouponResponse;
import store.novabook.front.common.response.ApiResponse;
import store.novabook.front.common.response.PageResponse;

@FeignClient(name = "gateway-service", url = "http://gateway-service:9777/api/v1/store/members/coupons", contextId = "memberCouponClient")
public interface MemberCouponClient {
	@GetMapping
	ApiResponse<GetCouponIdsResponse> getMemberCoupon();

	@GetMapping("/is-valid")
	ApiResponse<GetCouponAllResponse> getMemberCouponValidByMemberId();

	@GetMapping("/history")
	PageResponse<GetCouponHistoryResponse> getMemberCouponHistoryByMemberId(@RequestParam int page,
		@RequestParam int size);

	@GetMapping("/history/used")
	PageResponse<GetUsedCouponHistoryResponse> getMemberUsedCouponHistoryByMemberId(@RequestParam int page,
		@RequestParam int size);

	@PostMapping("/download")
	ApiResponse<CreateMemberCouponResponse> downloadCoupon(@RequestBody DownloadCouponRequest request);

	@PostMapping("/download/limited")
	ApiResponse<Void> downloadLimitedCoupon(@RequestBody DownloadCouponMessageRequest request);
}
