package com.nhnacademy.novabook_front.api.category;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.novabook_front.api.ApiResponse;
import com.nhnacademy.novabook_front.api.category.dto.CreateCategoryRequest;
import com.nhnacademy.novabook_front.api.category.dto.CreateCategoryResponse;
import com.nhnacademy.novabook_front.api.category.dto.GetCategoryListResponse;
import com.nhnacademy.novabook_front.api.category.dto.GetCategoryResponse;

@FeignClient(name = "categoryClient", url = "http://localhost:8090/api/v1/store/categories")
public interface CategoryClient {
	@PostMapping
	ApiResponse<CreateCategoryResponse> createCategory(@RequestBody CreateCategoryRequest category);
	@GetMapping("/{id}")
	ApiResponse<GetCategoryResponse> getCategory(@PathVariable Long id);
	@GetMapping
	ApiResponse<List<GetCategoryListResponse>> getCategoryAll();
	@DeleteMapping("/{id}")
	ApiResponse<Void> deleteCategory(@PathVariable Long id);
}
