package roomescape.authentication;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import roomescape.authentication.jwt.JwtAuthenticationInfoExtractor;
import roomescape.member.Member;

@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationInfoExtractor jwtAuthenticationInfoExtractor;

    public AuthenticationResponse createToken(Member member) {
        return authenticationProvider.createAuthenticationMethod(member);
    }

    public AuthenticationResponse extractToken(Cookie [] cookies){
        return jwtAuthenticationInfoExtractor.extractTokenFromCookie(cookies);
    }

    public MemberAuthInfo extractMemberInfo(String token) {
        return jwtAuthenticationInfoExtractor.extractMemberAuthInfoFromToken(token);
    }

    public MemberAuthInfo getMemberAuthInfoFromCookies(Cookie[] cookies) {
        AuthenticationResponse authenticationResponse = extractToken(cookies);
        return extractMemberInfo(authenticationResponse.accessToken());
    }
}
