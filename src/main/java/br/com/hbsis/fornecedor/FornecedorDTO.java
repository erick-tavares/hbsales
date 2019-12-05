package br.com.hbsis.fornecedor;

public class FornecedorDTO {

    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;


    public FornecedorDTO(Long id, String razaoSocial, String cnpj,String nome,String endereco,String telefone,String email) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public static FornecedorDTO of(Fornecedor fornecedor) {
        return new FornecedorDTO(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getNome(),
                fornecedor.getEndereco(),
                fornecedor.getTelefone(),
                fornecedor.getEmail()
        );
    }

    @Override
    public String toString() {
        return "FornecedorDTO{" +
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
