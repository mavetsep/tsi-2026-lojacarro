package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.dto.CarroRequestDTO;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroControllerTest {

    @Mock
    private CarroService carroService;

    @InjectMocks
    private CarroController carroController;

    private Carro carro;
    private CarroRequestDTO dtoValido;

    @BeforeEach
    void setUp() {
        carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Corolla");
        carro.setAno(2023);
        dtoValido = new CarroRequestDTO("Toyota", "Corolla", 2023);
    }

    // salvar
    @Test
    void deveSalvarCarro() {
        when(carroService.save(any(Carro.class))).thenReturn(carro);
        
        ResponseEntity<Carro> response = carroController.salvarCarro(dtoValido);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Corolla", response.getBody().getModelo());
    }

    // deletar
    @Test
    void deveDeletarCarro() {
        ResponseEntity<Void> response = carroController.deletarCarro(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carroService, times(1)).deleteById(1L);
    }

    // atualizar
    @Test
    void deveAtualizarCarro() {
        when(carroService.update(any(Carro.class))).thenReturn(carro);
        
        ResponseEntity<Carro> response = carroController.atualizarCarro(1L, dtoValido);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // procurar pelo ID
    @Test
    void deveProcurarCarroPorId() {
        when(carroService.findById(1L)).thenReturn(Optional.of(carro));
        ResponseEntity<Carro> response = carroController.pesquisarCarroPorId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Corolla", response.getBody().getModelo());
    }

    // listar todos os carros
    @Test
    void deveListarTodosOsCarros() {
        List<Carro> listaCarros = Arrays.asList(carro, new Carro());
        when(carroService.findAll()).thenReturn(listaCarros);
        
        ResponseEntity<List<Carro>> response = carroController.pesquisarTodosCarros();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    // testes de falha
    @Test
    void deveRetornarErroAoSalvarCarroComAnoNegativo() {
        CarroRequestDTO dtoAnoInvalido = new CarroRequestDTO("Toyota", "Corolla", -2000);

        when(carroService.save(any(Carro.class)))
            .thenThrow(new IllegalArgumentException("Ano inválido"));

        assertThrows(IllegalArgumentException.class, () -> {
            carroController.salvarCarro(dtoAnoInvalido);
        });
    }

    @Test
    void deveRetornarErroAoSalvarCarroSemModelo() {
        CarroRequestDTO dtoSemModelo = new CarroRequestDTO("Toyota", "", 2023);
        
        when(carroService.save(any(Carro.class)))
            .thenThrow(new IllegalArgumentException("Modelo vazio"));
            
        assertThrows(IllegalArgumentException.class, () -> {
            carroController.salvarCarro(dtoSemModelo);
        });
    }

    @Test
    void deveRetornarNotFoundAoBuscarIdInexistente() {
        when(carroService.findById(99L)).thenReturn(Optional.empty());
        ResponseEntity<Carro> response = carroController.pesquisarCarroPorId(99L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornarErroAoDeletarIdInexistente() {
        doThrow(IllegalArgumentException.class).when(carroService).deleteById(99L);
        assertThrows(IllegalArgumentException.class, () -> {
            carroController.deletarCarro(99L);
        });
    }

    @Test
    void deveRetornarErroAoAtualizarIdInexistente() {
        when(carroService.update(any(Carro.class)))
            .thenThrow(new IllegalArgumentException("Carro inexistente"));
            
        assertThrows(IllegalArgumentException.class, () -> {
            carroController.atualizarCarro(99L, dtoValido);
        });
    }
}