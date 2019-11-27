package br.com.hbsis.fornecedor;


import javax.persistence.*;

@Entity
@Table(name = "fornecedor")
public
class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor", nullable = false)
    private Long id;
    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;
    @Column(name = "cnpj_fornecedor", nullable = false, length = 14)
    private String cnpj;
    @Column(name = "nome_fornecedor", unique = true, nullable = false, length = 255)
    private String nome;
    @Column(name = "endereco_fornecedor", nullable = false, length = 255)
    private String endereco;
    @Column(name = "telefone_fornecedor", nullable = false, length = 255)
    private String telefone;
    @Column(name = "email_fornecedor", nullable = false, length = 255)
    private String email;

    public Fornecedor() {
    }

    public Fornecedor(String razaoSocial, String cnpj, String nome, String endereco, String telefone, String email) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public Fornecedor(Long id, String razaoSocial, String cnpj, String nome, String endereco, String telefone, String email) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
