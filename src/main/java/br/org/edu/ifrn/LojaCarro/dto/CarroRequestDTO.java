package br.org.edu.ifrn.LojaCarro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CarroRequestDTO(

        @NotBlank(message = "A marca é obrigatória")
        @Size(min = 2, max = 50, message = "A marca deve ter entre 2 e 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕçÇ\\s]+$", message = "A marca contém caracteres inválidos ou perigosos (XSS)")
        String marca,

        @NotBlank(message = "O modelo é obrigatório")
        @Size(min = 2, max = 50, message = "O modelo deve ter entre 2 e 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕçÇ\\s]+$", message = "O modelo contém caracteres inválidos ou perigosos (XSS)")
        String modelo,

        @NotNull(message = "O ano é obrigatório")
        Integer ano
) {}