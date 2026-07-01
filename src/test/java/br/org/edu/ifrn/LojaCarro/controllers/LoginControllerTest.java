package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.enums.Perfil;
import br.org.edu.ifrn.LojaCarro.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginController loginController;

    @Test
    void deveRealizarLoginComSucesso() {
        Map<String, String> payload = new HashMap<>();
        payload.put("login", "admin");
        payload.put("senha", "123");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setLogin("admin");
        usuarioMock.setPerfil(Perfil.GERENTE);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(usuarioMock);
        when(authenticationManager.authenticate(any())).thenReturn(authenticationMock);
        when(tokenService.gerarToken(usuarioMock)).thenReturn("token-jwt-simulado");

        ResponseEntity<?> response = loginController.efetuarLogin(payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}