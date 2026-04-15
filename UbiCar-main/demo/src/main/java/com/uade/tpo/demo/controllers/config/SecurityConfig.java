package com.uade.tpo.demo.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.uade.tpo.demo.entity.Role;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;


//Aca se configura si: 
// 1. que endpoints son publicos (login/register) y cuale no, 
// 2. que endpoints requieren estar autenticados,
// 3. que endpoints requieren un rol especifico,
// 4. que filtro se va a ejecutar antes de cada request (JwtAuthenticationFilter)
// 5. que se va a manejar la session de los usuarios (STATELESS: no se guarda nada en el servidor, el token se guarda en el cliente y se manda con cada request)


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                )
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                              
                                .authorizeHttpRequests(auth -> auth

                                                //end points publicos
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()

                                                .requestMatchers("/users/sellers/**").permitAll() // cualquier usuario puede ver los vendedores y sus perfiles

                                                .requestMatchers("images/mostrar").permitAll()

                                                //productos
                                                .requestMatchers("/products/obtener/**").permitAll() // cualquier usuario puede ver los productos disponibles y activos

                                                // user loggeado puede acceder a su perfil y actualizarlo
                                                .requestMatchers("/users/user/obtener", "/users/user/actualizar")
                                                .authenticated()

                                                // admin puede acceder a los perfiles de cualquier usuario
                                                .requestMatchers("/users/admin/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers("/cart/**").authenticated()

                                                // productos
                                                .requestMatchers("/products/crear").hasAnyRole("SELLER")
                                                .requestMatchers("/products/{id}").hasAnyRole("SELLER")
                                                .requestMatchers("/products/{id}/active").hasAnyRole("SELLER","ADMIN")

                                                //imagenes
                                                .requestMatchers("images/agregar").hasAnyRole("SELLER")
                                              
                                                //reservas
                                                .requestMatchers("/reservations/crear").authenticated() // cualquier usuario puede ver sus reservas
                                                .requestMatchers("/reservations/user/{id}").hasAnyRole("USER")
                                        
                                                // cualquier otra request
                                                .anyRequest().authenticated()

                                                );

                return http.build();
        }
}
