package mate.academy.library.service;

import mate.academy.library.dto.user.UserRegistrationRequestDto;
import mate.academy.library.dto.user.UserResponseDto;
import mate.academy.library.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
