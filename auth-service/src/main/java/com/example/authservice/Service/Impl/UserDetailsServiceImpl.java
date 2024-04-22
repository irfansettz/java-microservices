package com.example.authservice.Service.Impl;

import com.example.authservice.Model.Entity.UserEntity;
import com.example.authservice.Model.Entity.UserSecurityEntity;
import com.example.authservice.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isPresent()) return new UserSecurityEntity(user.get());

        throw new UsernameNotFoundException("Username Not Found");
    }
}
