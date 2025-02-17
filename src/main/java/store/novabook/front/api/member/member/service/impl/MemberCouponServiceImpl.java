package store.novabook.front.api.member.member.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.front.api.coupon.dto.request.DownloadCouponRequest;
import store.novabook.front.api.coupon.dto.response.GetCouponAllResponse;
import store.novabook.front.api.coupon.dto.response.GetCouponHistoryResponse;
import store.novabook.front.api.coupon.dto.response.GetUsedCouponHistoryResponse;
import store.novabook.front.api.member.coupon.dto.DownloadCouponMessageRequest;
import store.novabook.front.api.member.coupon.service.MemberCouponClient;
import store.novabook.front.api.member.member.service.MemberClient;
import store.novabook.front.api.member.member.service.MemberCouponService;
import store.novabook.front.common.response.ApiResponse;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberClient memberClient;
	private final MemberCouponClient memberCouponClient;

	@Override
	public GetCouponAllResponse getMyCouponAllWithValid() {
		ApiResponse<GetCouponAllResponse> memberCouponValidByMemberId = memberCouponClient.getMemberCouponValidByMemberId();
		return memberCouponValidByMemberId.getBody();
	}

	@Override
	public Page<GetCouponHistoryResponse> getMyCouponHistoryAll(Pageable pageable) {
		int pageSize = Math.max(1, pageable.getPageSize()); // 최소 1 보장
		Page<GetCouponHistoryResponse> response = memberCouponClient.getMemberCouponHistoryByMemberId(
			pageable.getPageNumber(), pageSize
		).toPage();

		return response != null && response.hasContent() ? response : Page.empty();
	}

	@Override
	public Page<GetUsedCouponHistoryResponse> getMyUsedCouponHistory(Pageable pageable) {
		int pageSize = Math.max(1, pageable.getPageSize()); // 최소 1 보장
		Page<GetUsedCouponHistoryResponse> response = memberCouponClient.getMemberUsedCouponHistoryByMemberId(
			pageable.getPageNumber(), pageSize
		).toPage();

		return response != null && response.hasContent() ? response : Page.empty();
	}

	@Override
	public void downloadCoupon(DownloadCouponRequest request) {
		memberCouponClient.downloadCoupon(request);
	}

	@Override
	public void downloadLimitedCoupon(@Valid DownloadCouponMessageRequest request) {
		memberCouponClient.downloadLimitedCoupon(request);
	}

}
