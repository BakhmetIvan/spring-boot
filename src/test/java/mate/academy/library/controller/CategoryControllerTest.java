package mate.academy.library.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.library.dto.category.CategoryRequestDto;
import mate.academy.library.dto.category.CategoryResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.util.ArrayList;
import java.util.List;

@Sql(scripts = "classpath:database/category/add-categories-to-categories-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/category/remove-categories-from-categories-table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final CategoryResponseDto FIRST_CATEGORY_RESPONSE_DTO = new CategoryResponseDto()
            .setId(1L)
            .setName("Category 1")
            .setDescription("Description 1");
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    void save_ValidCategoryDto_WillReturnCategoryDto() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName(FIRST_CATEGORY_RESPONSE_DTO.getName())
                .setDescription(FIRST_CATEGORY_RESPONSE_DTO.getDescription());
        CategoryResponseDto expected = FIRST_CATEGORY_RESPONSE_DTO;

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryResponseDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Find all categories")
    void findAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(FIRST_CATEGORY_RESPONSE_DTO);
        expected.add(new CategoryResponseDto()
                .setId(2L)
                .setName("Category 2")
                .setDescription("Description 2"));

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryResponseDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryResponseDto[].class
        ));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Find by id")
    void findById_ValidId_ShouldReturnValidCategoryDto() throws Exception {
        Long id = 1L;
        CategoryResponseDto expected = FIRST_CATEGORY_RESPONSE_DTO;

        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update by id")
    void updateById_ValidRequest_ShouldReturnValidUpdatedCategoryDto() throws Exception {
        Long id = 2L;
        CategoryRequestDto updateRequestDto = new CategoryRequestDto()
                .setName("Updated Category")
                .setDescription("Updated Description");
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(id)
                .setName(updateRequestDto.getName())
                .setDescription(updateRequestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete by id")
    void deleteById_ValidId_ShouldDeleteCategory() throws Exception {
        Long id = 2L;

        mockMvc.perform(
                        delete("/categories/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
