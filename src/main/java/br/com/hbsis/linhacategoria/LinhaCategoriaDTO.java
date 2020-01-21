package br.com.hbsis.linhacategoria;

import javax.validation.constraints.NotBlank;

public class LinhaCategoriaDTO {
    private Long id;
    private String codigo;
    @NotBlank(message = "nome é obrigatório e deve ter no máximo 10 caracteres")
    private String nome;
    private Long categoriaId;

    public LinhaCategoriaDTO(Long id, String codigo, String nome, Long categoriaId) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.categoriaId = categoriaId;
    }

    public static LinhaCategoriaDTO of (LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getId(),
                linhaCategoria.getCodigo(),
                linhaCategoria.getNome(),
                linhaCategoria.getCategoriaId().getId()
        );
    }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", categoriaId=" + categoriaId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
