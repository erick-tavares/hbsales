package br.com.hbsis.funcionario;

import javax.validation.constraints.NotBlank;

public class FuncionarioDTO {

    private Long id;
    @NotBlank(message = "nome é obrigatório")
    private String nome;
    @NotBlank(message = "e-mail é obrigatório")
    private String email;
    @NotBlank(message = "UUID é obrigatório")
    private String uuid;

    public FuncionarioDTO(Long id, @NotBlank(message = "nome é obrigatório") String nome, @NotBlank(message = "e-mail é obrigatório") String email, @NotBlank(message = "UUID é obrigatório") String uuid) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.uuid = uuid;
    }

    public static FuncionarioDTO of(Funcionario funcionario){
        return new FuncionarioDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getUuid()
        );
    }

    @Override
    public String toString() {
        return "FuncionarioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", uuid='" + uuid + '\'' +
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
