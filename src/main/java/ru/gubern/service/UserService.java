package ru.gubern.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import ru.gubern.dao.UserRepository;
import ru.gubern.dto.UserCreateDto;
import ru.gubern.dto.UserReadDto;
import ru.gubern.entity.User;
import ru.gubern.mapper.UserCreateMapper;
import ru.gubern.mapper.UserReadMapper;
import ru.gubern.validation.UpdateCheck;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto){
        //validation
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var validationResult = validator.validate(userDto, UpdateCheck.class);
        if (!validationResult.isEmpty()){
            throw new ConstraintViolationException(validationResult);
        }
        User userEntity = userCreateMapper.mapFrom(userDto);
        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id){
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), userRepository.getEntityManager().getEntityGraph("WithCompany")
        );
        return userRepository.findById(id, properties)
                .map(userReadMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id){
        var maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }
}
