package mate.academy.library.mapper;

import mate.academy.library.config.MapperConfig;
import mate.academy.library.dto.shoppingcart.CartItemDto;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book.id")
    CartItem toModel(CartItemRequestDto cartItemRequestDto);
}
