package store.novabook.front.api.member.grade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import store.novabook.front.api.member.grade.dto.GetMemberGradeResponse;
import store.novabook.front.common.response.ApiResponse;

@FeignClient(name = "gateway-service", url = "http://gateway-service:9777/api/v1/store/members/grade", contextId = "memberGradeClient")
public interface MemberGradeClient {

	@GetMapping
	ApiResponse<GetMemberGradeResponse> getMemberGrade();

}
