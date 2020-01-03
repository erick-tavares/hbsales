package br.com.hbsis.periodovendas;

import br.com.hbsis.fornecedor.Fornecedor;
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
        Fornecedor fornecedorDoPeriodo = fornecedorService.findFornecedorById(periodoVendasDTO.getFornecedorId());
        this.validate(periodoVendasDTO);
        this.validarPeriodo(periodoVendasDTO, fornecedorDoPeriodo);

        LOGGER.info("Salvando período de vendas");
        LOGGER.debug("Período: {}", periodoVendasDTO);

        PeriodoVendas periodoVendas = new PeriodoVendas();
        periodoVendas.setFimVendas(periodoVendasDTO.getFimVendas());
        periodoVendas.setInicioVendas(periodoVendasDTO.getInicioVendas());
        periodoVendas.setDescricao(periodoVendasDTO.getDescricao());
        periodoVendas.setRetiradaPedido(periodoVendasDTO.getRetiradaPedido());
        periodoVendas.setFornecedorId(fornecedorDoPeriodo);

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
        if ((periodoVendasDTO.getInicioVendas().isBefore(LocalDate.now())) || periodoVendasDTO.getFimVendas().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de período não pode ser registrada antes do dia de hoje");
        }
        if ((periodoVendasDTO.getFimVendas().isBefore(periodoVendasDTO.getInicioVendas()))) {
            throw new IllegalArgumentException("A data de fim de vendas não pode ser registrada antes da data de inicio de vendas");
        }
        if ((periodoVendasDTO.getFimVendas().isEqual(periodoVendasDTO.getInicioVendas()))) {
            throw new IllegalArgumentException("A data de fim de vendas não pode ser igual a data de inicio de vendas");
        }
        if (periodoVendasDTO.getRetiradaPedido().isBefore(periodoVendasDTO.getFimVendas())) {
            throw new IllegalArgumentException("A data de retirada do pedido é inválida");
        }
    }

    public PeriodoVendasDTO findById(Long id) {
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendasOptional.isPresent()) {
            return PeriodoVendasDTO.of(periodoVendasOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void validarPeriodo(PeriodoVendasDTO periodoVendasDTO, Fornecedor fornecedorId) {

        List<PeriodoVendas> periodoExistente = iPeriodoVendasRepository.findByFornecedorId(fornecedorId);
        for (PeriodoVendas periodo : periodoExistente) {
            if (periodo.getFornecedorId().getId().equals(periodoVendasDTO.getFornecedorId())) {
                if (!periodo.getId().equals(periodoVendasDTO.getId())){

                if ((periodoVendasDTO.getInicioVendas().isBefore(periodo.getFimVendas())) && periodoVendasDTO.getFimVendas().isAfter(periodo.getFimVendas())
                        || periodoVendasDTO.getInicioVendas().isEqual(periodo.getFimVendas())) {
                    throw new IllegalArgumentException("A data de início de período já existe entre outro período");
                }
                if ((periodoVendasDTO.getInicioVendas().isBefore(periodo.getInicioVendas())) && periodoVendasDTO.getFimVendas().isAfter(periodo.getInicioVendas())
                        || periodoVendasDTO.getFimVendas().isEqual(periodo.getInicioVendas())) {
                    throw new IllegalArgumentException("A data de fim de período já existe entre outro período");
                }
                if ((periodoVendasDTO.getInicioVendas().isBefore(periodo.getInicioVendas())) && periodoVendasDTO.getFimVendas().isAfter(periodo.getFimVendas())) {
                    throw new IllegalArgumentException("A data de período já existe entre outro período");
                }
                if (periodoVendasDTO.getRetiradaPedido().isBefore(periodo.getFimVendas())) {
                    throw new IllegalArgumentException("A data de retirada do pedido é inválida");
                }
                }
            }
        }
    }


    public PeriodoVendasDTO update(PeriodoVendasDTO periodoVendasDTO, Long id) {
        Optional<PeriodoVendas> periodoVendasExistenteOptional = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendasExistenteOptional.isPresent()) {
            PeriodoVendas periodoVendasExistente = periodoVendasExistenteOptional.get();

            periodoVendasDTO.setId(id);

            Fornecedor fornecedorDoPeriodo = fornecedorService.findFornecedorById(periodoVendasDTO.getFornecedorId());
            this.validate(periodoVendasDTO);
            this.validarPeriodo(periodoVendasDTO, fornecedorDoPeriodo);

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
                periodoVendasExistente.setFornecedorId(fornecedorDoPeriodo);

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
