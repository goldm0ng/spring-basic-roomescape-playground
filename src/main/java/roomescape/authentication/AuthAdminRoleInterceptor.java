package roomescape.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.authentication.jwt.JwtProvider;
import roomescape.authentication.jwt.JwtResponse;
import roomescape.authentication.jwt.JwtUtils;

@Component
@RequiredArgsConstructor
public class AuthAdminRoleInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        JwtResponse jwtResponse = JwtUtils.extractTokenFromCookie(request.getCookies());
        if (jwtResponse.accessToken() == null || !isAdmin(jwtResponse.accessToken())) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private boolean isAdmin(String token) {
        MemberAuthInfo memberAuthInfo = JwtUtils.extractMemberAuthInfoFromToken(token);
        return "ADMIN".equals(memberAuthInfo.role());
    }
}
