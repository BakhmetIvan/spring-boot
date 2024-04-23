package mate.academy.library.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.user.UserRegistrationRequestDto;
import mate.academy.library.dto.user.UserResponseDto;
import mate.academy.library.exception.RegistrationException;
import mate.academy.library.mapper.UserMapper;
import mate.academy.library.model.Role;
import mate.academy.library.model.User;
import mate.academy.library.repository.role.RoleRepository;
import mate.academy.library.repository.user.UserRepository;
import mate.academy.library.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Email already registered: " + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Role.RoleName.ROLE_USER));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
}
