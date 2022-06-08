package uz.yt.springdata.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.yt.springdata.auth.UserRoles;
import uz.yt.springdata.dao.Authorities;
import uz.yt.springdata.dao.User;
import uz.yt.springdata.dto.*;
import uz.yt.springdata.helper.Validator;
import uz.yt.springdata.helper.constants.AppResponseCode;
import uz.yt.springdata.helper.constants.AppResponseMessages;
import uz.yt.springdata.mapping.UserMapping;
import uz.yt.springdata.repository.AuthoritiesRepository;
import uz.yt.springdata.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;


    public ResponseDTO<UserDTO> addUser(UserDTO userDTO) {
        List<ValidatorDTO> errors = Validator.validateUser(userDTO);
        try {
            if (errors.size() > 0) {
                return new ResponseDTO<>(false,
                        AppResponseCode.VALIDATION_ERROR,
                        AppResponseMessages.VALIDATION_ERROR,
                        null, errors);
            }
            boolean exists = userRepository.existsByUsername(userDTO.getUsername());
            if (exists) {
                return new ResponseDTO<>(false, 1,
                        String.format("This username: %s is already taken",
                                userDTO.getUsername()), null, null);
            }
            User user = UserMapping.toEntity(userDTO);
            Set<Authorities> authorities = UserRoles.GUEST.getAuthorities();

            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setAuthorities(authorities);

            User savedUser = userRepository.save(user);
            return new ResponseDTO<>(true,
                    AppResponseCode.OK,
                    AppResponseMessages.OK,
                    UserMapping.toDto(savedUser), errors);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO<>(false,
                    AppResponseCode.DATABASE_ERROR,
                    AppResponseMessages.DATABASE_ERROR,
                    null,
                    errors);
        }
    }

    public ResponseDTO<UserDTO> getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseDTO<>(false,
                    AppResponseCode.NOT_FOUND,
                    AppResponseMessages.NOT_FOUND, null, null);
        }
        UserDTO userDTO = UserMapping.toDto(optionalUser.get());
        return new ResponseDTO<>(true, AppResponseCode.OK,
                AppResponseMessages.OK, userDTO, null);
    }

    public ResponseDTO<Page<User>> getAllUsersPage(Integer size, Integer page) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> all = userRepository.findAll(pageable);
        return new ResponseDTO<>(true,
                AppResponseCode.OK,
                AppResponseMessages.OK,
                all,
                null);
    }

    public ResponseDTO<UserDTO> editUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getId() == null) {
            return new ResponseDTO<>(false, AppResponseCode.VALIDATION_ERROR,
                    AppResponseMessages.VALIDATION_ERROR,
                    null, null);
        }

        List<ValidatorDTO> errors = Validator.validateUser(userDTO);
        try {

            if (errors.size() > 0) {
                return new ResponseDTO<>(false,
                        AppResponseCode.VALIDATION_ERROR,
                        AppResponseMessages.VALIDATION_ERROR,
                        null, errors);
            }
            Optional<User> optionalUser = userRepository.findById(userDTO.getId());
            if (!optionalUser.isPresent()){
                return new ResponseDTO<>(false, AppResponseCode.NOT_FOUND,
                        AppResponseMessages.NOT_FOUND,null);
            }
            boolean exists = userRepository.existsByUsernameAndIdNot(userDTO.getUsername(), userDTO.getId());
            if (exists) {
                return new ResponseDTO<>(false, 1,
                        String.format("This username: %s is already taken",
                                userDTO.getUsername()), null, null);
            }
            User user = UserMapping.toEntity(userDTO);
            User savedUser = userRepository.save(user);
            return new ResponseDTO<>(true,
                    AppResponseCode.OK,
                    AppResponseMessages.OK,
                    UserMapping.toDto(savedUser), errors);
        } catch (Exception e) {
            return new ResponseDTO<>(false,
                    AppResponseCode.DATABASE_ERROR,
                    AppResponseMessages.DATABASE_ERROR,
                    null,
                    errors);
        }
    }

    public ResponseDTO<UserDTO> deleteUser(Integer id) {

        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (!optionalUser.isPresent()){
                return new ResponseDTO<>(false, AppResponseCode.NOT_FOUND,
                        AppResponseMessages.NOT_FOUND,null);
            }

           userRepository.deleteById(id);

            return new ResponseDTO<>(true,
                    AppResponseCode.OK,
                    AppResponseMessages.OK,
                    UserMapping.toDto(optionalUser.get()));
        } catch (Exception e) {
            return new ResponseDTO<>(false,
                    AppResponseCode.DATABASE_ERROR,
                    AppResponseMessages.DATABASE_ERROR,
                    null);
        }

    }

    public ResponseDTO<UserDTO> changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (!user.isPresent()){
            return new ResponseDTO<>(false, AppResponseCode.NOT_FOUND, AppResponseMessages.NOT_FOUND, null);
        }
        User u = user.get();
        if(!passwordEncoder.matches(oldPassword, u.getPassword()) || oldPassword.equals(newPassword)){
            return new ResponseDTO<>(false, AppResponseCode.VALIDATION_ERROR, AppResponseMessages.MISMATCH, null);
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
        return new ResponseDTO<>(true, AppResponseCode.OK, AppResponseMessages.OK, UserMapping.toDto(u));
    }

    public ResponseDTO<UserDTO> currentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseDTO<>(true, AppResponseCode.OK, AppResponseMessages.OK, UserMapping.toDto(user));
    }
}
