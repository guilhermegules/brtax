package com.api.brtax.domain.user;

import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.dto.UserDetails;
import com.api.brtax.domain.user.exceptions.UserNotFoundException;

import com.api.brtax.exception.BusinessException;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails getUserById(String id) {
        return userRepository.findById(id)
                .map(user -> new UserDetails(user.getId(), user.getName(), user.getCpf(), user.getType()))
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public SaveUser save(SaveUser saveUser) {
        var isValid = saveUserValidityChecker(saveUser);

        if(!isValid) {
            throw new BusinessException("Passed user is invalid!");
        }

        var user = new User(
            saveUser.name(),
            saveUser.cpf(),
            saveUser.password(),
            saveUser.type());
        userRepository.save(user);
        return saveUser;
    }

    private boolean saveUserValidityChecker(SaveUser saveUser) {
        return emptyValidator(saveUser.password())
            && emptyValidator(saveUser.name())
            && cpfValidator(saveUser.cpf())
            && userTypeValidator(saveUser.type());
    }

    private boolean userTypeValidator(UserType type) {
        return Arrays.asList(UserType.values()).contains(type);
    }

    private boolean emptyValidator(String value) {
        if(value == null) return false;

        return !value.isEmpty();
    }

    private boolean cpfValidator(String cpf) {
        if(cpf == null) return false;

        return (!cpf.contains(".") || !cpf.contains("-")) && !cpf.isEmpty();
    }
}
