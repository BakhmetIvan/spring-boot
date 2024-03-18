package mate.academy.library.mapper;

import mate.academy.library.dto.book.BookDto;
import mate.academy.library.dto.book.CreateBookRequestDto;
import mate.academy.library.model.Book;
import mate.academy.library.config.MapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);
}
