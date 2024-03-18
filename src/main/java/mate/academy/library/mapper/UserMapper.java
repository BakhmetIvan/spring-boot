package mate.academy.library.mapper;

import mate.academy.library.config.MapperConfig;
import mate.academy.library.dto.user.UserRegistrationRequestDto;
import mate.academy.library.dto.user.UserResponseDto;
import mate.academy.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "password", source = "password")
    User toModel(UserRegistrationRequestDto requestDto);
}
