package com.myrctc.auth_service.auth.filter;

import com.myrctc.auth_service.auth.token.Token;
import com.myrctc.auth_service.auth.token.service.TokenService;
import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import com.myrctc.auth_service.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final TokenService jwtService;
    private final UserService userService;

    private static final String AUTH_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws ServletException, IOException {
        if(!isAuthHeaderPresentAndValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final Token jwt = jwtService.getTokenFromHeader(request.getHeader(AUTH_HEADER));
        final Email email = jwtService.getUserEmail(jwt);

        if(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())){
            filterChain.doFilter(request, response);
            return;
        }
        final Optional<UserDto> optionalUser = userService.getUserByEmail(email);
        if(optionalUser.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }
        final UserDto user = optionalUser.get();
        if(!jwtService.isTokenValid(jwt, user)){
            filterChain.doFilter(request, response);
            return;
        }

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        return uri.startsWith("/login") || uri.startsWith("/register") || uri.contains("swagger");
    }

    private boolean isAuthHeaderPresentAndValid(@NonNull final HttpServletRequest request){
        final String authHeader = request.getHeader(AUTH_HEADER);
        return Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ");
    }
}
