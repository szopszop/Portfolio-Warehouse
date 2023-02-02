package com.example.springbootaws.security.services;


import com.example.springbootaws.security.oauth.CustomOAuth2User;
import com.example.springbootaws.user.User;
import com.example.springbootaws.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return UserDetailsImpl.build(user);
    }

    public void processOAuthPostLogin(CustomOAuth2User user) {
        Optional<User> existUser = userRepository.findUserByEmail(user.getEmail());
        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword("no_password_configured_yet");
            newUser.setProvider(User.Provider.GOOGLE);
            UUID userId = userRepository.save(newUser).getUserId();
            newUser.setUserId(userId);
            userRepository.save(newUser);
        }
    }




}
