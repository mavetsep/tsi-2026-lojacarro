package br.org.edu.ifrn.LojaCarro;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/insert_carros.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CarroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarErro403QuandoSemToken() throws Exception {
        mockMvc.perform(get("/carro"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "VENDEDOR")
    void deveListarCarrosInseridosPeloSql() throws Exception {
        mockMvc.perform(get("/carro"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(Matchers.greaterThanOrEqualTo(2)));
    }

    @Test
    @WithMockUser(roles = "VENDEDOR")
    void deveRetornarNotFoundAoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/carro/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornarBadRequestAoSalvarCarroComAnoNegativo() throws Exception {
        Carro carroInvalido = new Carro();
        carroInvalido.setModelo("Fusca");
        carroInvalido.setAno(-1);

        mockMvc.perform(post("/carro/salvar")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(carroInvalido)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornarBadRequestAoAtualizarCarroInexistente() throws Exception {
        Carro carroAtualizado = new Carro();
        carroAtualizado.setModelo("Golf");
        carroAtualizado.setAno(2020);

        mockMvc.perform(put("/carro/999")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(carroAtualizado)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornarBadRequestAoSalvarCarroSemModelo() throws Exception {
        Carro carroSemModelo = new Carro();
        carroSemModelo.setAno(2022);

        mockMvc.perform(post("/carro/salvar")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(carroSemModelo)))
               .andExpect(status().isBadRequest());
    }
}