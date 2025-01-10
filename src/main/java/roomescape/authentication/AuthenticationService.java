package roomescape.authentication;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.authentication.jwt.JwtAuthenticationInfoExtractor;
import roomescape.member.Member;

@Service
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
}
