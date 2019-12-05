package br.com.hbsis.produto;


import java.time.LocalDateTime;

public class ProdutoDTO {
    private Long id;
    private int codigo;
    private String nome ;
    private double preco ;
    private int unidadePorCaixa ;
    private double pesoPorUnidade ;
    private LocalDateTime validade;
    private Long linhaCategoriaId;

    public ProdutoDTO(Long id, int codigo, String nome, double preco, int unidadePorCaixa, double pesoPorUnidade, LocalDateTime validade, Long linhaCategoriaId) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.unidadePorCaixa = unidadePorCaixa;
        this.pesoPorUnidade = pesoPorUnidade;
        this.validade = validade;
        this.linhaCategoriaId = linhaCategoriaId;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", unidadePorCaixa=" + unidadePorCaixa +
                ", pesoPorUnidade=" + pesoPorUnidade +
                ", validade=" + validade +
                ", linhaCategoriaId=" + linhaCategoriaId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getUnidadePorCaixa() {
        return unidadePorCaixa;
    }

    public void setUnidadePorCaixa(int unidadePorCaixa) {
        this.unidadePorCaixa = unidadePorCaixa;
    }

    public double getPesoPorUnidade() {
        return pesoPorUnidade;
    }

    public void setPesoPorUnidade(double pesoPorUnidade) {
        this.pesoPorUnidade = pesoPorUnidade;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public Long getLinhaCategoriaId() {
        return linhaCategoriaId;
    }

    public void setLinhaCategoriaId(Long linhaCategoriaId) {
        this.linhaCategoriaId = linhaCategoriaId;
    }

    public static ProdutoDTO of (Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco(),
                produto.getUnidadePorCaixa(),
                produto.getPesoPorUnidade(),
                produto.getValidade(),
                produto.getLinhaCategoriaId()
        );




    }


}
