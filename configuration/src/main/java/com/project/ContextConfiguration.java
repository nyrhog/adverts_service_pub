package com.project;

import com.project.dao.RoleRepository;
import com.project.dao.UserRepository;
import com.project.service.IUserService;
import com.project.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ContextConfiguration {

    @Bean
    IUserService getUserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder){
        return new UserService(userRepository, roleRepository, passwordEncoder);
    }

}
