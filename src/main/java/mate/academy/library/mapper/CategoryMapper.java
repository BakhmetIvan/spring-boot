package mate.academy.library.mapper;

import mate.academy.library.config.MapperConfig;
import mate.academy.library.dto.category.CategoryDto;
import mate.academy.library.dto.category.CategoryRequestDto;
import mate.academy.library.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryDTO);

    void updateCategoryFromDto(CategoryRequestDto categoryDto, @MappingTarget Category category);
}
