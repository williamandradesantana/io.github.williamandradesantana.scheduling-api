package io.github.williamandradesantana.scheduling_api.domain.auth.services;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserDetailsService {

    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user != null) return user;
        throw new UsernameNotFoundException("Username " + userName + "not found!");
    }
}
