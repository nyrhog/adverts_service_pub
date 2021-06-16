package com.project.service;

import com.project.CodeGenerator;
import com.project.MailSender;
import com.project.dao.RoleRepository;
import com.project.dao.UserRepository;
import com.project.entity.Role;
import com.project.entity.User;
import com.project.entity.UserStatus;
import com.project.exception.WrongRestoreCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        return registerUser(user, userRoles);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setStatus(UserStatus.DELETED);

    }

    @Override
    public User registerAdminUser(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        Role adminRoleUser = roleRepository.findByName("ROLE_ADMIN");

        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        userRoles.add(adminRoleUser);

        return registerUser(user, userRoles);
    }

    @Override
    public void sendMessageWithCode(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
        String email = user.getEmail();

        int code = saveRestoreCode(user);
        MailSender mailSender = new MailSender();

        mailSender.send(email, code);
        log.info("Code was sent");
    }

    protected int saveRestoreCode(User user) {

        int code = CodeGenerator.generate();
        user.setGeneratedValue(code);

        userRepository.saveAndFlush(user);
        return code;
    }

    @Override
    @Transactional
    public void restorePassword(String username, int code, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
        boolean codeIsEquals = user.getGeneratedValue().equals(code);

        if (codeIsEquals) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setGeneratedValue(null);
            log.info("Password restored");
            return;
        }

        throw new WrongRestoreCodeException("Code not valid");
    }

    private User registerUser(User user, List<Role> userRoles) {

        User userUsername = userRepository.findByUsername(user.getUsername()).orElse(null);
        User userEmail = userRepository.findByEmail(user.getEmail()).orElse(null);

        if(userUsername != null){
            throw new ValidationException("Username is already used");
        }

        if(userEmail != null){
            throw new ValidationException("Email is already used");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(UserStatus.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser.getUsername());
        return registeredUser;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }
}
