package com.blog.app.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blog.app.models.AppRole;
import com.blog.app.models.Role;
import com.blog.app.models.User;
import com.blog.app.repositories.RoleRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.security.jwt.AuthEntryPointJwt;
import com.blog.app.security.jwt.AuthTokenFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
					securedEnabled = true,
					jsr250Enabled = true)
public class SecurityConfig{
	
	@Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    @Value("${frontend.url}")
    private String frontendUrl;
    
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.csrf(csrf -> 
			csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringRequestMatchers("/api/auth/public/**"));
	//	http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests((requests) 
				-> requests
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/csrf-token").permitAll()
				.requestMatchers("/api/auth/public/**").permitAll()
				.anyRequest().authenticated());
		http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		//http.formLogin(withDefaults());
//		http.sessionManagement((session) -> 
//				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.httpBasic(withDefaults());
		return http.build();
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin(frontendUrl); // Allow your React app
        configuration.addAllowedMethod("*"); // Allow all methods (GET, POST, etc.)
        configuration.addAllowedHeader("*"); // Allow all headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // Apply to your API endpoints
        return source;
    }
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
    		throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	//defining which password encoder is being used
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	    public CommandLineRunner initData(RoleRepository roleRepository, 
	    		UserRepository userRepository,
	    		PasswordEncoder passwordEncoder) {
	        return args -> {
	            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
	                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

	            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
	                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

	            if (!userRepository.existsByUserName("user1")) {
	                User user1 = new User("user1", "user1@example.com", 
	                		passwordEncoder.encode("password1"));
	                user1.setAccountNonLocked(false);
	                user1.setAccountNonExpired(true);
	                user1.setCredentialsNonExpired(true);
	                user1.setEnabled(true);
	                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
	                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
	                user1.setTwoFactorEnabled(false);
	                user1.setSignUpMethod("email");
	                user1.setRole(userRole);
	                userRepository.save(user1);
	            }
	            
	            if (!userRepository.existsByUserName("admin")) {
	                User admin = new User("admin", "admin@example.com", 
	                		passwordEncoder.encode("adminPass"));
	                admin.setAccountNonLocked(true);
	                admin.setAccountNonExpired(true);
	                admin.setCredentialsNonExpired(true);
	                admin.setEnabled(true);
	                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
	                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
	                admin.setTwoFactorEnabled(false);
	                admin.setSignUpMethod("email");
	                admin.setRole(adminRole);
	                userRepository.save(admin);
	            }
	        };
	 }

	
	
	
	
//	@Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        JdbcUserDetailsManager manager =
//                new JdbcUserDetailsManager(dataSource);
//        if (!manager.userExists("user1")) {
//            manager.createUser(
//                    User.withUsername("user1")
//                            .password("{noop}password1")
//                            .roles("USER")
//                            .build()
//            );
//        }
//        if (!manager.userExists("admin")) {
//            manager.createUser(
//                    User.withUsername("admin")
//                            .password("{noop}adminPass")
//                            .roles("ADMIN")
//                            .build()
//            );
//        }
//        return manager;
//    }

	
}
