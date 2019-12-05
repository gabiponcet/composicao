package br.edu.ifsul.model;

public class Item {
    private Long id;
    private Integer quantidade;
    private Double totalItem;
    private Boolean situacao;
    private Produto produto; //expressa a ligação entre as classes Item com Produto

    public Item(Produto produto) {
        this.produto = produto;
    }

    public Item(Long id, Integer quantidade, Double totalItem, Boolean situacao, Produto produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.totalItem = totalItem;
        this.situacao = situacao;
        this.produto = produto;
    }

    public Item() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Double totalItem) {
        this.totalItem = totalItem;
    }

    public Boolean getSituacao() {
        return situacao;
    }

    public void setSituacao(Boolean situacao) {
        this.situacao = situacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "\nItem{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", totalItem=" + totalItem +
                ", situacao=" + situacao +
                ", produto=" + produto +
                '}';
    }
}
