package br.org.edu.ifrn.LojaCarro.config;

import br.org.edu.ifrn.LojaCarro.enums.Perfil;
import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (usuarioRepository.findByLogin("admin") == null) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setPerfil(Perfil.GERENTE);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuário admin criado com sucesso!");
        } else {
            System.out.println(">>> Usuário admin já existe, pulando criação.");
        }

        if (usuarioRepository.findByLogin("vendedor") == null) {
            Usuario vendedor = new Usuario();
            vendedor.setLogin("vendedor");
            vendedor.setSenha(passwordEncoder.encode("123456"));
            vendedor.setPerfil(Perfil.VENDEDOR);
            usuarioRepository.save(vendedor);
            System.out.println(">>> Usuário vendedor criado com sucesso!");
        }
    }
}