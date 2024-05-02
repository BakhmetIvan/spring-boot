package mate.academy.library.service;

import mate.academy.library.dto.book.BookResponseDto;
import mate.academy.library.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.book.BookSearchParametersDto;
import mate.academy.library.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    void deleteById(Long id);

    BookResponseDto updateBookInfoById(Long id, CreateBookRequestDto requestDto);

    List<BookResponseDto> search(BookSearchParametersDto searchParameters, Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllByCategoriesId(Long id, Pageable pageable);
}
