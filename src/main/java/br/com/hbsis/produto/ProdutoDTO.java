package br.com.hbsis.produto;


import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class ProdutoDTO {
    private Long id;
    @NotBlank(message = "codigo é obrigatório")
    private String codigo;
    @NotBlank(message = "nome é obrigatório")
    private String nome ;
    @NotBlank(message = "preço é obrigatório")
    private double preco ;
    @NotBlank(message = "unidade por caixa é obrigatório")
    private int unidadePorCaixa ;
    @NotBlank(message = "peso por unidade é obrigatório")
    private double pesoPorUnidade ;
    @NotBlank(message = "unidade de medida do peso é obrigatório")
    private String unidadeMedidaPeso;
    @NotBlank(message = "validade é obrigatória")
    private LocalDate validade;
    private Long linhaCategoriaId;


    public ProdutoDTO(Long id, String codigo, String nome, double preco, int unidadePorCaixa, double pesoPorUnidade,
                      String unidadeMedidaPeso, LocalDate validade, Long linhaCategoriaId) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.unidadePorCaixa = unidadePorCaixa;
        this.pesoPorUnidade = pesoPorUnidade;
        this.unidadeMedidaPeso = unidadeMedidaPeso;
        this.validade = validade;
        this.linhaCategoriaId = linhaCategoriaId;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", unidadePorCaixa=" + unidadePorCaixa +
                ", pesoPorUnidade=" + pesoPorUnidade +
                ", unidadeMedidaPeso='" + unidadeMedidaPeso + '\'' +
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

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public Long getLinhaCategoriaId() {
        return linhaCategoriaId;
    }

    public void setLinhaCategoriaId(Long linhaCategoriaId) {
        this.linhaCategoriaId = linhaCategoriaId;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getUnidadeMedidaPeso() {
        return unidadeMedidaPeso;
    }

    public void setUnidadeMedidaPeso(String unidadeMedidaPeso) {
        this.unidadeMedidaPeso = unidadeMedidaPeso;
    }

    public static ProdutoDTO of (Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco(),
                produto.getUnidadePorCaixa(),
                produto.getPesoPorUnidade(),
                produto.getUnidadeMedidaPeso(),
                produto.getValidade(),
                produto.getLinhaCategoriaId().getId()
        );

    }


}
