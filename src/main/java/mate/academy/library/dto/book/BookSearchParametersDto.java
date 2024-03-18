package mate.academy.library.dto.book;

import lombok.Data;

@Data
public class BookSearchParametersDto {
        String[] titles;
        String[] authors;
}
