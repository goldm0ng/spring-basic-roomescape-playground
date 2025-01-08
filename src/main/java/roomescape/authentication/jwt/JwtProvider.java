package roomescape.authentication.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.JwtProviderException;
import roomescape.member.Member;

@Component
public class JwtProvider {

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
            throw new JwtProviderException("JWT 생성에 실패하였습니다.");
        }
    }
}
