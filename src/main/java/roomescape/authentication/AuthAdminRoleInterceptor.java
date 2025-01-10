package roomescape.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthAdminRoleInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationResponse authenticationResponse = authenticationService.extractToken(request.getCookies());
        if (authenticationResponse.accessToken() == null || !isAdmin(authenticationResponse.accessToken())) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private boolean isAdmin(String token) {
        MemberAuthInfo memberAuthInfo = authenticationService.extractMemberInfo(token);
        return "ADMIN".equals(memberAuthInfo.role());
    }
}
