package com.api.brtax.domain.user;

import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.dto.UpdateUser;
import com.api.brtax.domain.user.dto.UserDetails;

import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import com.api.brtax.util.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDetails getUserById(UUID id) {
    return userRepository
        .findById(id)
        .map(user -> new UserDetails(user.getId(), user.getName(), user.getCpf(), user.getType()))
        .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
  }

  public UUID save(SaveUser saveUser) {
    if (!isSavePayloadValid(saveUser)) {
      throw new BusinessException("Passed user is invalid!");
    }

    var user = new User(saveUser.name(), saveUser.cpf(), saveUser.password(), saveUser.type());
    var savedUser = userRepository.save(user);
    return savedUser.getId();
  }

  public User update(UUID userId, UpdateUser updateUser) {
    if(!isUpdatePayloadValid(updateUser)) {
      throw new BusinessException("User data for update is invalid! " + updateUser);
    }

    var user =
        userRepository
            .findById(userId)
            .map(u -> updateUserData(u, updateUser))
            .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

    return userRepository.save(user);
  }

  private User updateUserData(User user, UpdateUser updateUser) {
    if (!Objects.equals(user.getName(), updateUser.name()) && Objects.nonNull(updateUser.name())) {
      user.setName(updateUser.name());
    }

    if (!Objects.equals(user.getPassword(), updateUser.password())
        && Objects.nonNull(updateUser.password())) {
      user.setPassword(updateUser.password());
    }

    if (!Objects.equals(user.getType(), updateUser.type()) && Objects.nonNull(updateUser.type())) {
      user.setType(updateUser.type());
    }

    if (!Objects.equals(user.getCpf(), updateUser.cpf()) && Objects.nonNull(updateUser.cpf())) {
      user.setCpf(updateUser.cpf());
    }

    return user;
  }

  private boolean isSavePayloadValid(SaveUser saveUser) {
    return Validator.hasValue(saveUser.password())
        && Validator.hasValue(saveUser.name())
        && Validator.isValidCpf(saveUser.cpf())
        && Validator.hasValueOnList(Arrays.asList(UserType.values()), saveUser.type());
  }

  private boolean isUpdatePayloadValid(UpdateUser saveUser) {
    return Objects.nonNull(saveUser.cpf()) && Validator.isValidCpf(saveUser.cpf())
        || Objects.nonNull(saveUser.password())
        || Objects.nonNull(saveUser.type()) && Validator.hasValueOnList(Arrays.asList(UserType.values()), saveUser.type())
        || Objects.nonNull(saveUser.name());
  }
}
