package org.nikolait.assigment.telegramwebappauth.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;
import org.nikolait.assigment.telegramwebappauth.service.TelegramInitDataService;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.util.pattern.PathPatternParser.defaultInstance;

@Component
@RequiredArgsConstructor
public class TelegramAuthFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Telegram ";

    private static final List<PathPattern> EXCLUDED_PATTERNS = List.of(
            defaultInstance.parse("/"),
            defaultInstance.parse("/actuator/health/**"),
            defaultInstance.parse("/js/**"),
            defaultInstance.parse("/css/**"),
            defaultInstance.parse("/images/**")
    );

    private final TelegramInitDataService telegramInitDataService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(PREFIX)) {
            response.sendError(UNAUTHORIZED.value(), "Invalid Authorization header");
            return;
        }

        String initData = authHeader.substring(PREFIX.length());

        if (!telegramInitDataService.validateInitData(initData)) {
            response.sendError(UNAUTHORIZED.value(), "Invalid Telegram initData");
            return;
        }

        TelegramUser user = telegramInitDataService.parseTelegramUser(initData);
        request.setAttribute("telegramUser", user);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        PathContainer pathContainer = PathContainer.parsePath(request.getRequestURI());
        return EXCLUDED_PATTERNS.stream().anyMatch(pattern -> pattern.matches(pathContainer));
    }
}
