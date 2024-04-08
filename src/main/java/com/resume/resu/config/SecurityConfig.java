package com.resume.resu.config;


import com.resume.resu.service.impl.login.LoginServiceImpl;
import com.resume.resu.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration

// Spring Security 활성화
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled =true)

// Spring Security 설정을 담당하는 클래스
// 웹 보안 설정, 인증 매커니즘 설정, PW 인코딩, 보안 관련 빈들의 등록 담당
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final LoginServiceImpl loginServiceImpl;

    // JwtUtils 빈은 해당 빈이 처음 요청 될 때만 생성됨. 미리 생성 되지 않음.
    public SecurityConfig(@Lazy JwtUtils jwtUtils, LoginServiceImpl loginServiceImpl){
        this.jwtUtils=jwtUtils;
        this.loginServiceImpl=loginServiceImpl;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(사이트 간 요청 위조) 공격 방어 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                /* 세션 사용 x, stateless 인증 사용
                * 클라이언트가 매 요청마다 토큰을 제공하여 인증하는 방식
                * Restful API와 같이, 클라이언트와 서버 간의 통신이 상태를 유지 하지 않는 경우 주로 사용
                *  */
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                /* TODO : 개발 마무리 단계에서 권한 인가 수정해야함! 지금은 개발 단계여서 권한 없이 접근 가능하도록 해둠 */
//                .authorizeHttpRequests(authorizeRequest->authorizeRequest
//
//                        // 모든 경로에 대한 접근 허용
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
//                );

        return http.build();
    }

    /*
    * AuthenticationManager : Spring Security에서 인증을 수행하는 핵심 인터페이스. 인증 검증 / 인증된 Authentiction 객체 반환
    *
    * */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
