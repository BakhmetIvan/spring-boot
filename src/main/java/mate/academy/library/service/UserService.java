package mate.academy.library.service;

import mate.academy.library.dto.user.UserRegistrationRequestDto;
import mate.academy.library.dto.user.UserResponseDto;
import mate.academy.library.exception.RegistrationException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
