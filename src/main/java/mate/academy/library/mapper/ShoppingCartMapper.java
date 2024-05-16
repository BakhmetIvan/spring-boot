package mate.academy.library.mapper;

import mate.academy.library.config.MapperConfig;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
