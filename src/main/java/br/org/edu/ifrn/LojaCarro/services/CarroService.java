package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    public CarroRepository carroRepository;

    public Carro save(Carro c) {
        // salvar sem modelo
        if (c.getModelo() == null || c.getModelo().isEmpty()) {
            throw new IllegalArgumentException("Modelo não pode ser vazio");
        }
        // ano negativo
        if (c.getAno() < 0) {
            throw new IllegalArgumentException("Ano inválido");
        }
        return carroRepository.save(c);
    }

    public void deleteById(Long id) {
        // deletar ID inexistente
        if (!carroRepository.existsById(id)) {
            throw new IllegalArgumentException("ID não encontrado");
        }
        carroRepository.deleteById(id);
    }

    public Optional<Carro> findById(Long id) {
        return carroRepository.findById(id);
    }

    public List<Carro> findAll() {
        return carroRepository.findAll();
    }

    public Carro update(Carro c) {
        // atualizar carro inexistente
        if (c.getId() == null || !carroRepository.existsById(c.getId())) {
            throw new IllegalArgumentException("Carro inexistente");
        }
        return carroRepository.save(c);
    }
}