package roomescape.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.authentication.MemberAuthInfo;
import roomescape.exception.JwtValidationException;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static String secretKey;

    @Value("${roomescape.auth.jwt.secret}")
    public void setSecretKey(String secretKey) {
        JwtUtils.secretKey = secretKey;
    }

    public static MemberAuthInfo extractMemberAuthInfoFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtValidationException("토큰이 존재하지 않습니다.");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);

            return new MemberAuthInfo(name, role);
        } catch (JwtException e) {
            throw new JwtValidationException("유효하지 않은 JWT 토큰입니다.");
        }
    }

    public static JwtResponse extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new JwtValidationException("쿠키가 존재하지 않습니다.");
        }

        try {
            String accessToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("token"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElseThrow(() -> new JwtValidationException("토큰이 존재하지 않습니다."));

            return new JwtResponse(accessToken);
        } catch (Exception e) {
            throw new JwtValidationException("쿠키에서 토큰 추출 중 오류가 발생했습니다.");
        }
    }
}
