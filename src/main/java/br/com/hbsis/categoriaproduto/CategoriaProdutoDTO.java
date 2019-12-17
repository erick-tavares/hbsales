package br.com.hbsis.categoriaproduto;


import javax.validation.constraints.NotBlank;

public class CategoriaProdutoDTO {
    private Long id;
    @NotBlank(message = "nome é obrigatório")
    private String nome;
    private Long fornecedorId;
    @NotBlank(message = "código deve ter entre 1 a 4 números")
    private String codigo;


    public CategoriaProdutoDTO(Long id, String nome, Long fornecedorId, String codigo) {
        this.id = id;
        this.nome = nome;
        this.fornecedorId = fornecedorId;
        this.codigo = codigo;
    }

    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
        return new CategoriaProdutoDTO(
                categoriaProduto.getId(),
                categoriaProduto.getNome(),
                categoriaProduto.getFornecedorId().getId(),
                categoriaProduto.getCodigo()
        );
    }

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", fornecedorId=" + fornecedorId +
                ", codigo=" + codigo +
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

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
