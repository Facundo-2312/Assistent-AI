package com.facundo.assistentia.application.user.service;

import com.facundo.assistentia.application.user.dto.UserCreateRequest;
import com.facundo.assistentia.application.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountService userAccountService;

    public UserResponse register(UserCreateRequest request) {
        return userAccountService.registerTeamMember(request);
    }
}
