package mate.academy.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.category.CategoryResponseDto;
import mate.academy.library.dto.category.CategoryRequestDto;
import mate.academy.library.service.BookService;
import mate.academy.library.service.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Categories management", description = "Endpoint for mapping categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create new category", description = "Creates a new category entry in the database")
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get all categories", description = "Retrieves all categories from the database")
    public List<CategoryResponseDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Retrieves a category by its identifier")
    public CategoryResponseDto getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category by id", description = "Updates a category info in the database")
    public CategoryResponseDto updateCategory(@PathVariable @Positive Long id,
                                              @RequestBody @Valid CategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category",
            description = "Deletes a category from the database by id using soft delete")
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable @Positive Long id, @PageableDefault Pageable pageable) {
        return bookService.findAllByCategoriesId(id, pageable);
    }
}
