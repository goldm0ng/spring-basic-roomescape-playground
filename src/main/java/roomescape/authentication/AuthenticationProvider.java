package roomescape.authentication;

import roomescape.member.Member;

public interface AuthenticationProvider {

    AuthenticationResponse createAuthenticationMethod(Member member);

}
