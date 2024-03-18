package mate.academy.library.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.user.UserRegistrationRequestDto;
import mate.academy.library.dto.user.UserResponseDto;
import mate.academy.library.exception.RegistrationException;
import mate.academy.library.mapper.UserMapper;
import mate.academy.library.model.User;
import mate.academy.library.repository.user.UserRepository;
import mate.academy.library.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Email already registered: " + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        user = userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.toDto(user);
        return userResponseDto;
    }
}
