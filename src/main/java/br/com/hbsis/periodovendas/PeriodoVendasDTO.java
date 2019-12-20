package br.com.hbsis.periodovendas;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class PeriodoVendasDTO {
    private Long id;
     @NotBlank (message = "o inicioVendas é obrigatório")
    private LocalDate inicioVendas;
     @NotBlank (message = "o fimVendas é obrigatório")
    private LocalDate fimVendas;
     @NotBlank (message = "o retiradaPedido é obrigatório")
    private LocalDate retiradaPedido;
     @NotBlank (message = "a descricao é obrigatória")
    private String descricao;
     @NotBlank (message = "o fornecedorId é obrigatório")
    private Long fornecedorId;

    public PeriodoVendasDTO(Long id, LocalDate inicioVendas, LocalDate fimVendas, LocalDate retiradaPedido, String descricao, Long fornecedorId) {
        this.id = id;
        this.inicioVendas = inicioVendas;
        this.fimVendas = fimVendas;
        this.retiradaPedido = retiradaPedido;
        this.descricao = descricao;
        this.fornecedorId = fornecedorId;
    }

    public static PeriodoVendasDTO of(PeriodoVendas periodoVendas) {
        return new PeriodoVendasDTO(periodoVendas.getId(),
                periodoVendas.getInicioVendas(),
                periodoVendas.getFimVendas(),
                periodoVendas.getRetiradaPedido(),
                periodoVendas.getDescricao(),
                periodoVendas.getFornecedorId().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInicioVendas() {
        return inicioVendas;
    }

    public void setInicioVendas(LocalDate inicioVendas) {
        this.inicioVendas = inicioVendas;
    }

    public LocalDate getFimVendas() {
        return fimVendas;
    }

    public void setFimVendas(LocalDate fimVendas) {
        this.fimVendas = fimVendas;
    }

    public LocalDate getRetiradaPedido() {
        return retiradaPedido;
    }

    public void setRetiradaPedido(LocalDate retiradaPedido) {
        this.retiradaPedido = retiradaPedido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    @Override
    public String toString() {
        return "PeriodoVendasDTO{" +
                "id=" + id +
                ", inicioVendas=" + inicioVendas +
                ", fimVendas=" + fimVendas +
                ", retiradaPedido=" + retiradaPedido +
                ", descricao='" + descricao + '\'' +
                ", fornecedorId=" + fornecedorId +
                '}';
    }
}
