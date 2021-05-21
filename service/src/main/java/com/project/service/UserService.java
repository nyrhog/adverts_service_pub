package com.project.service;

import com.project.dao.RoleRepository;
import com.project.dao.UserRepository;
import com.project.entity.Role;
import com.project.entity.User;
import com.project.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        return getUser(user, userRoles);
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

        return getUser(user, userRoles);
    }

    private User getUser(User user, List<Role> userRoles) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(UserStatus.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }
}
