package store.novabook.front.api.tag.service;

import java.util.List;

import store.novabook.front.api.tag.dto.CreateTagRequest;
import store.novabook.front.api.tag.dto.CreateTagResponse;
import store.novabook.front.api.tag.dto.GetTagResponse;
import store.novabook.front.common.response.PageResponse;

public interface TagService {

	CreateTagResponse createTags(CreateTagRequest tagRequest);

	PageResponse<GetTagResponse> getTags(int page, int size);

	List<GetTagResponse> getTagList();

	void deleteTag(Long tagId);


}
