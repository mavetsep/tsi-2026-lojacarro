package br.org.edu.ifrn.LojaCarro.enums;

public enum Perfil {
    GERENTE("GERENTE"),
    VENDEDOR("VENDEDOR");

    private final String nome;

    Perfil(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}