package br.com.hbsis.categoriaproduto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoriaProdutoDTO {
    private Long id;
    @NotBlank(message = "nome é obrigatório")
    private String nome;
    @NotBlank(message = "fornecedorId é obrigatório")
    private Long fornecedorId;
    @NotBlank(message = "codigo é obrigatório")
    @Size(min = 10, max = 10)
    private int codigo;


    public CategoriaProdutoDTO(Long id, String nome, Long fornecedorId, int codigo) {
        this.id = id;
        this.nome = nome;
        this.fornecedorId = fornecedorId;
        this.codigo = codigo;
    }

    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
        return new CategoriaProdutoDTO(
                categoriaProduto.getId(),
                categoriaProduto.getNome(),
                categoriaProduto.getFornecedorId(),
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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
