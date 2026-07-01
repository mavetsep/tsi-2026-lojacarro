package br.org.edu.ifrn.LojaCarro.services;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public boolean autenticar(String usuario, String senha) {
        return "admin".equals(usuario) && "123".equals(senha);
    }
}