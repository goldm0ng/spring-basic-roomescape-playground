package roomescape.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.authentication.MemberAuthInfo;
import roomescape.authentication.jwt.JwtResponse;
import roomescape.authentication.jwt.JwtUtils;
import roomescape.member.Member;
import roomescape.exception.MemberNotFoundException;
import roomescape.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new MemberNotFoundException("입력한 이메일 혹은 비밀번호로 가입한 회원을 찾을 수 없습니다."));

        return jwtUtils.createAccessToken(member);
    }

    public LoginCheckResponse checkLogin(MemberAuthInfo memberAuthInfo) {

        Member member= memberRepository.findByName(memberAuthInfo.name())
                .orElseThrow(() -> new MemberNotFoundException("로그인 된 회원이 아닙니다."));

        return new LoginCheckResponse(member.getName());
    }
}
