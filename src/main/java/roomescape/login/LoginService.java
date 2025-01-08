package roomescape.login;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.authentication.MemberAuthInfo;
import roomescape.authentication.jwt.JwtResponse;
import roomescape.authentication.jwt.JwtUtils;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberDao memberDao;
    private final JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest loginRequest) {

        try {
            Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
            return jwtUtils.createAccessToken(member);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException("이메일 혹은 비밀번호가 맞지 않습니다.", e);
        }
    }

    public LoginCheckResponse checkLogin(MemberAuthInfo memberAuthInfo) {

        try {
            Member member = memberDao.findByName(memberAuthInfo.name());
            return new LoginCheckResponse(member.getName());
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException("로그인이 되지 않은 상태입니다.", e);
        }
    }
}
