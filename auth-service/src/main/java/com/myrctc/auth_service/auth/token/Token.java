package com.myrctc.auth_service.auth.token;

import lombok.Builder;

@Builder
public record Token(String token) {
}
