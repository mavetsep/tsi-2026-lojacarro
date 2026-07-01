package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository carroRepository;

    @InjectMocks
    private CarroService carroService;

    private Carro carro;

    @BeforeEach
    void setUp() {
        carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Civic");
        carro.setAno(2022);
    }

    // salvar
    @Test
    void deveSalvarCarro() {
        when(carroRepository.save(any(Carro.class))).thenReturn(carro);
        Carro carroSalvo = carroService.save(carro);
        assertNotNull(carroSalvo);
        assertEquals("Civic", carroSalvo.getModelo());
    }

    // pitest: testa o valor limite do ano para matar o mutante
    @Test
    void deveSalvarCarroComAnoZero() {
        Carro carroAnoZero = new Carro();
        carroAnoZero.setModelo("Fusca");
        carroAnoZero.setAno(0);

        when(carroRepository.save(any(Carro.class))).thenReturn(carroAnoZero);

        Carro carroSalvo = carroService.save(carroAnoZero);

        assertNotNull(carroSalvo);
        assertEquals(0, carroSalvo.getAno());
        verify(carroRepository, times(1)).save(carroAnoZero);
    }

    // deletar
    @Test
    void deveDeletarCarro() {
        when(carroRepository.existsById(1L)).thenReturn(true); // CORREÇÃO: Mock da existência
        doNothing().when(carroRepository).deleteById(1L);
        carroService.deleteById(1L);
        verify(carroRepository, times(1)).deleteById(1L);
    }

    // atualizar
    @Test
    void deveAtualizarCarro() {
        when(carroRepository.existsById(1L)).thenReturn(true); // CORREÇÃO: Mock da existência
        when(carroRepository.save(any(Carro.class))).thenReturn(carro);
        Carro carroAtualizado = carroService.update(carro);
        assertNotNull(carroAtualizado);
    }

    // procurar pelo ID
    @Test
    void deveProcurarCarroPorId() {
        when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
        Optional<Carro> carroEncontrado = carroService.findById(1L);
        assertTrue(carroEncontrado.isPresent());
        assertEquals(2022, carroEncontrado.get().getAno());
    }

    // listar todos os carros
    @Test
    void deveListarTodosOsCarros() {
        List<Carro> listaCarros = Arrays.asList(carro, new Carro());
        when(carroRepository.findAll()).thenReturn(listaCarros);
        
        List<Carro> carrosEncontrados = carroService.findAll();
        
        assertNotNull(carrosEncontrados);
        assertEquals(2, carrosEncontrados.size());
    }

    // testes de Falha
    @Test
    void deveLancarExcecaoAoSalvarCarroSemModelo() {
        Carro carroInvalido = new Carro();
        carroInvalido.setModelo("");
        
        assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroInvalido);
        });
    }

    @Test
    void deveLancarExcecaoAoDeletarCarroInexistente() {
        when(carroRepository.existsById(99L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> {
            carroService.deleteById(99L);
        });
    }

    @Test
    void deveLancarExcecaoAoAtualizarCarroInexistente() {
        when(carroRepository.existsById(99L)).thenReturn(false);
        carro.setId(99L);
        assertThrows(IllegalArgumentException.class, () -> {
            carroService.update(carro);
        });
    }

    @Test
    void deveRetornarVazioAoProcurarIdInexistente() {
        when(carroRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Carro> carroEncontrado = carroService.findById(99L);
        assertTrue(carroEncontrado.isEmpty());
    }

    @Test
    void deveLancarExcecaoAoSalvarCarroComAnoNegativo() {
        Carro carroInvalido = new Carro();
        carroInvalido.setModelo("Fusca");
        carroInvalido.setAno(-1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroInvalido);
        });
    }
}