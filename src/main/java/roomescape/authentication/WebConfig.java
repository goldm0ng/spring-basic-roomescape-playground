package roomescape.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.authentication.jwt.JwtAuthenticationInfoExtractor;
import roomescape.authentication.jwt.JwtAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAuthenticationInfoExtractor jwtAuthenticationInfoExtractor;

    @Bean
    public AuthenticationService authenticationService(){
        return new AuthenticationService(jwtAuthenticationProvider, jwtAuthenticationInfoExtractor);
    }
}
