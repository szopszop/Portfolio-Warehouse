package com.szymontracz.warehouse.service;

import com.szymontracz.warehouse.dto.RegistrationDto;
import com.szymontracz.warehouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;

    public String registerNewUser(RegistrationDto registrationDto) {

    }
}
