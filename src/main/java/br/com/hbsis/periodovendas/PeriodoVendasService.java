package br.com.hbsis.periodovendas;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import br.com.hbsis.fornecedor.FornecedorService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoVendasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasService.class);

    private final IPeriodoVendasRepository iPeriodoVendasRepository;
    private final FornecedorService fornecedorService;

    public PeriodoVendasService(IPeriodoVendasRepository iPeriodoVendasRepository, FornecedorService fornecedorService) {
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
        this.fornecedorService = fornecedorService;
    }

    public PeriodoVendasDTO save(PeriodoVendasDTO periodoVendasDTO) {

        this.validate(periodoVendasDTO);

        LOGGER.info("Salvando período de vendas");
        LOGGER.debug("Período: {}", periodoVendasDTO);

        PeriodoVendas periodoVendas = new PeriodoVendas();
        periodoVendas.setFimVendas(periodoVendasDTO.getFimVendas());
        periodoVendas.setInicioVendas(periodoVendasDTO.getInicioVendas());
        periodoVendas.setDescricao(periodoVendasDTO.getDescricao());
        periodoVendas.setRetiradaPedido(periodoVendasDTO.getRetiradaPedido());
        periodoVendas.setFornecedorId(fornecedorService.findFornecedorById(periodoVendasDTO.getFornecedorId()));

        periodoVendas = this.iPeriodoVendasRepository.save(periodoVendas);

        return PeriodoVendasDTO.of(periodoVendas);
    }


    private void validate(PeriodoVendasDTO periodoVendasDTO) {
        LOGGER.info("Validando período de vendas");

        if (periodoVendasDTO == null) {
            throw new IllegalArgumentException("PeriodoVendasDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendasDTO.getInicioVendas()))) {
            throw new IllegalArgumentException("InicioVendas não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendasDTO.getFimVendas()))) {
            throw new IllegalArgumentException("FimVendas não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(periodoVendasDTO.getDescricao())) {
            throw new IllegalArgumentException("Descrição não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendasDTO.getRetiradaPedido()))) {
            throw new IllegalArgumentException("RetiradaPedido não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendasDTO.getFornecedorId()))) {
            throw new IllegalArgumentException("FornecedorId não deve ser nulo/vazio");
        }
        if ((periodoVendasDTO.getInicioVendas().isBefore(LocalDate.now())) || periodoVendasDTO.getInicioVendas().isAfter(periodoVendasDTO.getFimVendas()) ||
                periodoVendasDTO.getRetiradaPedido().isBefore(periodoVendasDTO.getFimVendas())) {
            throw new IllegalArgumentException("A data de período é inválida");
        }
        if (this.validarPeriodo(periodoVendasDTO, periodoVendasDTO.getFornecedorId())) {
            throw new IllegalArgumentException(String.format("Período de vendas ja existe"));
        }
    }

    public List<PeriodoVendas> listarPeriodo() {
        List<PeriodoVendas> periodoVendas = this.iPeriodoVendasRepository.findAll();
        return periodoVendas;
    }

    public PeriodoVendasDTO findById(Long id) {
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendasOptional.isPresent()) {
            return PeriodoVendasDTO.of(periodoVendasOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PeriodoVendas findFornecedorById(Long idFornecedor) {
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findFornecedorById(idFornecedor);
        if (periodoVendasOptional.isPresent()) {
            return periodoVendasOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", idFornecedor));
    }

    public boolean validarPeriodo(PeriodoVendasDTO periodoDTO, Long fornecedorId) {

        for (PeriodoVendas periodo : iPeriodoVendasRepository.findAllFornecedorById(fornecedorId)) {

            if (periodoDTO.getInicioVendas().isBefore(periodo.getFimVendas()) && periodoDTO.getInicioVendas().isAfter(periodo.getInicioVendas())) {
                return true;
            }
            if (periodoDTO.getFimVendas().isBefore(periodoDTO.getFimVendas()) && periodoDTO.getFimVendas().isAfter(periodoDTO.getInicioVendas())) {
                return true;
            }
        }
        return false;
    }

    public PeriodoVendasDTO update(PeriodoVendasDTO periodoVendasDTO, Long id) {
        Optional<PeriodoVendas> periodoVendasExistenteOptional = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendasExistenteOptional.isPresent()) {
            PeriodoVendas periodoVendasExistente = periodoVendasExistenteOptional.get();

            LOGGER.info("Atualizando periodo de vendas... id: [{}]", periodoVendasExistente.getId());
            LOGGER.debug("Payload: {}", periodoVendasDTO);
            LOGGER.debug("Periodo Existente: {}", periodoVendasExistente);

            if (periodoVendasExistente.getFimVendas().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(String.format("Período de vendas já expirou"));
            } else {
                periodoVendasExistente.setFimVendas(periodoVendasDTO.getFimVendas());
                periodoVendasExistente.setInicioVendas(periodoVendasDTO.getInicioVendas());
                periodoVendasExistente.setDescricao(periodoVendasDTO.getDescricao());
                periodoVendasExistente.setRetiradaPedido(periodoVendasDTO.getRetiradaPedido());
                periodoVendasExistente.setFornecedorId(fornecedorService.findFornecedorById(periodoVendasDTO.getFornecedorId()));

                periodoVendasExistente = this.iPeriodoVendasRepository.save(periodoVendasExistente);

                return PeriodoVendasDTO.of(periodoVendasExistente);
            }
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para período de vendas de ID: [{}]", id);

        this.iPeriodoVendasRepository.deleteById(id);
    }

}
