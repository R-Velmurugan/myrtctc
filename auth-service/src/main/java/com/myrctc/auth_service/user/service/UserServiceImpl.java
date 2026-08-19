package com.myrctc.auth_service.user.service;

import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import com.myrctc.auth_service.user.UserEntity;
import com.myrctc.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @NonNull
    @Override
    public Optional<UserDto> getUserByEmail(@NonNull Email email) {
        return userRepository.findUserEntitiesByEmail(email)
                .map(this::convertUserEntityToUserDto);
    }

    @NonNull
    @Override
    public UserDto registerUser(@NonNull UserDto userDto) {
        UserEntity userEntity = userRepository.save(convertUserDtoToUserEntity(userDto));
        return convertUserEntityToUserDto(userEntity); //to remove the password
    }

    @NonNull
    private UserDto convertUserEntityToUserDto(@NonNull final UserEntity userEntity) {
//      password is deliberately ignored here
        return UserDto.builder()
                .name(userEntity.getName())
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
                .password(userDto.getPassword())
                .age(userDto.getAge())
                .email(userDto.getEmail().email())
                .build();
    }
}
