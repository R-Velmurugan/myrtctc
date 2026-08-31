package com.myrctc.auth_service.user;

import lombok.Builder;

@Builder
public record LoginRequest(Email email, String password) {
}
