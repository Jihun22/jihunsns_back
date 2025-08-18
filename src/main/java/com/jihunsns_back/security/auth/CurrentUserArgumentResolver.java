package com.jihunsns_back.security.auth;

import com.jihunsns_back.security.jwt.JwtAuthFilter.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mav,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;

        Object principal = auth.getPrincipal();
        if (principal instanceof UserPrincipal up) {
            Class<?> targetType = parameter.getParameterType();
            if (targetType.equals(Long.class)) return up.id();
            if (targetType.equals(UserPrincipal.class)) return up;
        }
        return null;
    }
}