package br.edu.ifsul.model;

import java.util.List;

public class Cliente {
    private Long id;
    private String nome;
    private String sobrenome;
    private Boolean situação;
    private List<Pedido> pedidos; //expressa a ligação entre as classes Cliente com Pedido

    public Cliente() {
    }

    public Cliente(Long id, String nome, String sobrenome, boolean situação) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.situação = situação;
    }

    public Cliente(Long id, String nome, String sobrenome, boolean situação, List<Pedido> pedidos) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.situação = situação;
        this.pedidos = pedidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Boolean isSituação() {
        return situação;
    }

    public void setSituação(Boolean situação) {
        this.situação = situação;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public String toString() {
        return "\nCliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", situação=" + situação +
                ", pedidos=" + pedidos +
                '}';
    }
}
