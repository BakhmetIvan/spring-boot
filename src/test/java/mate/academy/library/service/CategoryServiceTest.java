package mate.academy.library.service;

import mate.academy.library.dto.category.CategoryRequestDto;
import mate.academy.library.dto.category.CategoryResponseDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.CategoryMapper;
import mate.academy.library.model.Category;
import mate.academy.library.repository.category.CategoryRepository;
import mate.academy.library.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Category FIRST_CATEGORY = new Category()
            .setId(1L)
            .setName("Category1")
            .setDescription("Description1");
    private static final Category SECOND_CATEGORY = new Category()
            .setId(2L)
            .setName("Category2")
            .setDescription("Description2");
    private static final CategoryResponseDto FIRST_CATEGORY_RESPONSE_DTO =
            new CategoryResponseDto()
            .setId(FIRST_CATEGORY.getId())
            .setName(FIRST_CATEGORY.getName())
            .setDescription(FIRST_CATEGORY.getDescription());
    private static final CategoryResponseDto SECOND_CATEGORY_RESPONSE_DTO =
            new CategoryResponseDto()
            .setId(SECOND_CATEGORY.getId())
            .setName(SECOND_CATEGORY.getName())
            .setDescription(SECOND_CATEGORY.getDescription());
    private static final CategoryRequestDto CATEGORY_UPDATE_REQUEST_DTO =
            new CategoryRequestDto()
            .setName("Updated name")
            .setDescription(FIRST_CATEGORY.getDescription());
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save category with valid data will return the valid category dto")
    public void saveCategory_WithValidData_ShouldReturnValidCategoryDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName(FIRST_CATEGORY.getName())
                .setDescription(FIRST_CATEGORY.getDescription());

        when(categoryMapper.toModel(requestDto)).thenReturn(FIRST_CATEGORY);
        when(categoryMapper.toDto(FIRST_CATEGORY)).thenReturn(FIRST_CATEGORY_RESPONSE_DTO);
        when(categoryRepository.save(FIRST_CATEGORY)).thenReturn(FIRST_CATEGORY);
        CategoryResponseDto actual = categoryService.save(requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(FIRST_CATEGORY_RESPONSE_DTO, actual);
        verify(categoryMapper, times(1)).toModel(requestDto);
        verify(categoryRepository, times(1)).save(FIRST_CATEGORY);
        verify(categoryMapper, times(1)).toDto(FIRST_CATEGORY);
    }

    @Test
    @DisplayName("Find category by valid id will return the valid category")
    public void findCategoryById_WithValidCategoryId_ShouldReturnValidCategory() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(FIRST_CATEGORY));
        when(categoryMapper.toDto(FIRST_CATEGORY)).thenReturn(FIRST_CATEGORY_RESPONSE_DTO);
        CategoryResponseDto actual = categoryService.getById(categoryId);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(FIRST_CATEGORY_RESPONSE_DTO, actual);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(FIRST_CATEGORY);
    }

    @Test
    @DisplayName("Find book by invalid id will throw the exception")
    public void findCategoryById_WithInvalidCategoryId_ShouldThrowException() {
        Long categoryId = 100L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        String actual = Assertions.assertThrows(EntityNotFoundException.class,
                (() -> categoryService.getById(categoryId))).getMessage();
        String expected = "Can't find category by id: " + categoryId;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all categories will return the list of categories")
    public void findAllCategories_ShouldReturnListOfCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(FIRST_CATEGORY);
        categoryList.add(SECOND_CATEGORY);
        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();
        categoryResponseDtoList.add(FIRST_CATEGORY_RESPONSE_DTO);
        categoryResponseDtoList.add(SECOND_CATEGORY_RESPONSE_DTO);
        Page<Category> categoryPage = new PageImpl<>(categoryList);

        when(categoryRepository.findAll(Pageable.unpaged())).thenReturn(categoryPage);
        when(categoryMapper.toDto(categoryList.get(0))).thenReturn(categoryResponseDtoList.get(0));
        when(categoryMapper.toDto(categoryList.get(1))).thenReturn(categoryResponseDtoList.get(1));
        List<CategoryResponseDto> actual = categoryService.findAll(Pageable.unpaged());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(categoryResponseDtoList, actual);
        verify(categoryRepository, times(1)).findAll(Pageable.unpaged());
        verify(categoryMapper, times(1)).toDto(categoryList.get(0));
        verify(categoryMapper, times(1)).toDto(categoryList.get(1));
    }

    @Test
    @DisplayName("Update category by valid id will return the updated category dto")
    public void updateCategoryById_WithValidId_ShouldReturnUpdatedCategoryDto() {
        Long categoryId = 1L;
        Category oldCategory = FIRST_CATEGORY;
        Category updatedCategory = FIRST_CATEGORY.setName("Updated name");
        CategoryResponseDto updatedCategoryDto =
                FIRST_CATEGORY_RESPONSE_DTO.setName("Updated name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(oldCategory));
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);
        CategoryResponseDto actual =
                categoryService.update(categoryId, CATEGORY_UPDATE_REQUEST_DTO);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(updatedCategoryDto, actual);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(updatedCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Update category by invalid id will produce an exception")
    public void updateCategoryById_WithInvalidId_ShouldThrowException() {
        Long categoryId = 100L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        String actual = Assertions.assertThrows(EntityNotFoundException.class, (
                () -> categoryService.update(categoryId, CATEGORY_UPDATE_REQUEST_DTO))
        ).getMessage();
        String expected = "Can't find category by id: " + categoryId;
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Delete category by valid id will delete the book from db")
    public void deleteBookById_WithValidId_ShouldDeleteBook() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
