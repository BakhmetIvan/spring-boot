package mate.academy.library.mapper;

import mate.academy.library.config.MapperConfig;
import mate.academy.library.dto.order.OrderRequestDto;
import mate.academy.library.dto.order.OrderResponseDto;
import mate.academy.library.dto.order.OrderUpdateStatusDto;
import mate.academy.library.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(OrderRequestDto responseDto);

    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    void updateOrderFromDto(@MappingTarget Order order, OrderUpdateStatusDto orderRequestDto);
}
