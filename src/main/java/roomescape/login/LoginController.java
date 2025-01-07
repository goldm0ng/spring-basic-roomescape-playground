package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.authentication.MemberAuthInfo;
import roomescape.jwt.JwtResponse;
import roomescape.jwt.JwtUtils;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        JwtResponse jwtResponse = loginService.login(loginRequest);

        Cookie cookie = new Cookie("token", jwtResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public LoginCheckResponse checkLogin(HttpServletRequest request) {
        JwtResponse jwtResponse = JwtUtils.extractTokenFromCookie(request.getCookies());
        MemberAuthInfo memberAuthInfo = JwtUtils.extractMemberAuthInfoFromToken(jwtResponse.accessToken());
        LoginCheckResponse loginCheckResponse = loginService.checkLogin(memberAuthInfo);

        return loginCheckResponse;
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
