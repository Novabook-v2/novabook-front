package store.novabook.front.api.point.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetPointHistoryListResponse(
	List<GetPointHistoryResponse> pointHistoryResponseList

) {

}
