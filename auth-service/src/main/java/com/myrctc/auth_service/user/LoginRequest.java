package com.myrctc.auth_service.user;

import lombok.Builder;

@Builder
public record LoginRequest(String email, String password) {
}
