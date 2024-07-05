package store.novabook.front.api.book.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetReviewListResponse(List<GetReviewResponse> getReviewResponses) {
}
