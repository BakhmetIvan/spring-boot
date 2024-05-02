package mate.academy.library.service;

import mate.academy.library.dto.category.CategoryResponseDto;
import mate.academy.library.dto.category.CategoryRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}
