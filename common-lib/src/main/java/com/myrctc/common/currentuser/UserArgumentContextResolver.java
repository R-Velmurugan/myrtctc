package com.myrctc.common.currentuser;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class UserArgumentContextResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) && parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public @Nullable Object resolveArgument(@NonNull final MethodParameter parameter, @Nullable final ModelAndViewContainer mavContainer, @NonNull final NativeWebRequest webRequest, @Nullable final WebDataBinderFactory binderFactory) {
        final String userID = webRequest.getHeader("userID");
        final CurrentUser annotation = parameter.getParameterAnnotation(CurrentUser.class);

        if(StringUtils.hasLength(userID) && Objects.requireNonNull(annotation).required()) {
            throw new UnauthorizedException();
        }

        return Objects.nonNull(userID) ? UserContext.withEmail(userID) : null;
    }
}
