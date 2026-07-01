package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.dto.CarroRequestDTO;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @PostMapping("salvar")
    public ResponseEntity<Carro> salvarCarro(@RequestBody @Valid CarroRequestDTO dto) {
        Carro c = new Carro();
        c.setModelo(dto.modelo());
        c.setAno(dto.ano());
        
        Carro savedCarro = carroService.save(c);
        return ResponseEntity.ok(savedCarro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizarCarro(@PathVariable Long id, @RequestBody @Valid CarroRequestDTO dto) {
        Carro c = new Carro();
        c.setId(id);
        c.setModelo(dto.modelo());
        c.setAno(dto.ano());
        
        Carro updatedCarro = carroService.update(c);
        return ResponseEntity.ok(updatedCarro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCarro(@PathVariable Long id) {
        carroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> pesquisarCarroPorId(@PathVariable Long id) {
        Optional<Carro> carro = carroService.findById(id);
        return carro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Carro>> pesquisarTodosCarros() {
        List<Carro> carros = carroService.findAll();
        return ResponseEntity.ok(carros);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleErroDeValidacaoDto(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        var erro = ex.getBindingResult().getFieldError();
        String mensagem = erro != null ? erro.getDefaultMessage() : "Erro de validação nos dados enviados.";
        return ResponseEntity.badRequest().body(mensagem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleErroDeValidacao(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}