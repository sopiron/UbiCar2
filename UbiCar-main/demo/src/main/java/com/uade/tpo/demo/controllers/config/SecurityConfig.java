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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import java.util.List;

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

               .cors(Customizer.withDefaults())
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

                                                .requestMatchers("/users/sellers/**")
                                                .permitAll() // cualquier usuario puede ver los vendedores y sus perfiles

                                                .requestMatchers("images/mostrar").permitAll()

                                                //productos
                                                .requestMatchers("/products/obtener/**")
                                                .permitAll() // cualquier usuario puede ver los productos disponibles y activos

                                                //fechas bloqueadas
                                                .requestMatchers("/products/{productId}/blocked-dates/obtener")
                                                .permitAll()

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
                                                .requestMatchers("/products/{id}/deleted").hasAnyRole("SELLER","ADMIN") // solo el vendedor o el admin pueden eliminar un producto
                                        
                                                //imagenes
                                                .requestMatchers("images/agregar").hasAnyRole("SELLER")
                                              
                                                //reservas
                                                .requestMatchers("/reservations/crear").authenticated() // cualquier usuario puede ver sus reservas
                                                .requestMatchers("/reservations/user/{id}").hasAnyRole("USER")
                                                .requestMatchers("/reservations/{id}").authenticated() // cualquier usuario puede ver el detalle de su reserva
                                                .requestMatchers("/reservations/{id}/pay").authenticated() // cualquier usuario puede pagar
                                                .requestMatchers("/reservations/{id}/cancel").authenticated() // cualquier usuario puede cancelar su reserva

                                                //fechas bloqueadas

                                                .requestMatchers("/products/{productId}/blocked-dates/crear").hasAnyRole("SELLER")
                                                .requestMatchers("/products/{productId}/blocked-dates/{date}/borrar").hasAnyRole("SELLER")

                                                // cualquier otra request
                                                .anyRequest().authenticated()

                                                );

                return http.build();
        }

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:5174"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
}
}
