package roomescape.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.authentication.MemberAuthInfo;
import roomescape.exception.JwtProviderException;
import roomescape.exception.JwtValidationException;
import roomescape.member.Member;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public JwtResponse createAccessToken(Member member) {
        try {
            String accessToken = Jwts.builder()
                    .setSubject(member.getId().toString())
                    .claim("name", member.getName())
                    .claim("role", member.getRole())
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();

            return new JwtResponse(accessToken);
        } catch (JwtException e) {
            throw new JwtProviderException("JWT 생성에 실패하였습니다.", e);
        }
    }

    public MemberAuthInfo extractMemberAuthInfoFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtValidationException("토큰이 존재하지 않습니다.");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = claims.get("id", Long.class);
            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);

            return new MemberAuthInfo(id, name, role);
        } catch (JwtException e) {
            throw new JwtValidationException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    public JwtResponse extractTokenFromCookie(Cookie[] cookies) {
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
            throw new JwtValidationException("쿠키에서 토큰 추출 중 오류가 발생했습니다.", e);
        }
    }
}
