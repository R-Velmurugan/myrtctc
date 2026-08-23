package com.myrctc.auth_service.user.service;

import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import com.myrctc.auth_service.user.UserEntity;
import com.myrctc.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @NonNull
    @Override
    public Optional<UserDto> getUserByEmail(@NonNull final Email email) {
        return userRepository.findUserEntitiesByEmail(email.email())
                .map(this::convertUserEntityToUserDto);
    }

    @NonNull
    @Override
    public UserDto registerUser(@NonNull final UserDto userDto) {
        UserEntity userEntity = userRepository.save(convertUserDtoToUserEntity(userDto));
        return convertUserEntityToUserDto(userEntity); //to remove the password
    }

    @NonNull
    private UserDto convertUserEntityToUserDto(@NonNull final UserEntity userEntity) {
//      password is deliberately ignored here
        return UserDto.builder()
                .name(userEntity.getName())
                .hashedPassword(userEntity.getPassword())
                .surname(userEntity.getSurname())
                .age(userEntity.getAge())
                .email(new Email(userEntity.getEmail()))
                .build();
    }

    @NonNull
    private UserEntity convertUserDtoToUserEntity(@NonNull final UserDto userDto) {
        return UserEntity.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .age(userDto.getAge())
                .email(userDto.getEmail().email())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull final String email) throws UsernameNotFoundException {
        return getUserByEmail(new Email(email)).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
