package store.novabook.front.admin.category;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import store.novabook.front.api.category.dto.CreateCategoryRequest;
import store.novabook.front.api.category.service.CategoryService;

@Configuration
@RequestMapping("/admin/categories")
@Controller
@RequiredArgsConstructor
public class AdminCategoryController {
	private final CategoryService categoryService;

	@GetMapping
	public String getCategories(Model model) {

		model.addAttribute("category", categoryService.getCategoryAll());
		return "admin/category/category_list";
	}

	@PostMapping
	public String createCategory(@ModelAttribute CreateCategoryRequest request) {
		categoryService.createCategory(request);

		return "redirect:/admin/categories";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return "redirect:/admin/categories";
	}

}
