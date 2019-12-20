package br.com.hbsis.periodovendas;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import br.com.hbsis.fornecedor.FornecedorService;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PeriodoVendasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasService.class);

    private final IPeriodoVendasRepository iPeriodoVendasRepository;
    private final FornecedorService fornecedorService;
    private final PeriodoVendasService iFornecedorRepository;

    public PeriodoVendasService(IPeriodoVendasRepository iPeriodoVendasRepository, FornecedorService fornecedorService, PeriodoVendasService iFornecedorRepository) {
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
        this.fornecedorService = fornecedorService;
        this.iFornecedorRepository = iFornecedorRepository;
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
                periodoVendasDTO.getFimVendas().isBefore(LocalDate.now()) || periodoVendasDTO.getFimVendas().isBefore(periodoVendasDTO.getInicioVendas())) {
            throw new IllegalArgumentException("A data de período é inválida");
        }
        if ((periodoVendasDTO.getRetiradaPedido().isBefore(periodoVendasDTO.getInicioVendas())) || (periodoVendasDTO.getRetiradaPedido().isBefore(periodoVendasDTO.getFimVendas()))) {
            throw new IllegalArgumentException("A data de retirada é inválida");
        }
    }

    public PeriodoVendasDTO findById(Long id) {
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendasOptional.isPresent()) {
            return PeriodoVendasDTO.of(periodoVendasOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PeriodoVendas findFornecedorById ( Long idFornecedor){
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findFornecedorById(idFornecedor);
        if (periodoVendasOptional.isPresent()) {
            return periodoVendasOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", idFornecedor));
    }
//////
    public PeriodoVendas findByData (LocalDate dataPeriodo, FornecedorDTO fornecedorDTO) {
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findByData(dataPeriodo);
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findFornecedorById().getFornecedorId();
        /////
        ccriar validação no validade para verificar se a data passada aqui ja existe, e lançar uma exxeçao
        //////


        if (periodoVendasOptional.isPresent()) {
            return periodoVendasOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", idFornecedor));
    }
/////////////

    public PeriodoVendasDTO update(PeriodoVendasDTO periodoVendasDTO, Long id) {
        Optional<PeriodoVendas> periodoVendasExistenteOptional = this.iPeriodoVendasRepository.findById(id);

        if (periodoVendasExistenteOptional.isPresent()) {
            PeriodoVendas periodoVendasExistente = periodoVendasExistenteOptional.get();

            LOGGER.info("Atualizando periodo de vendas... id: [{}]", periodoVendasExistente.getId());
            LOGGER.debug("Payload: {}", periodoVendasDTO);
            LOGGER.debug("Periodo Existente: {}", periodoVendasExistente);

            periodoVendasExistente.setFimVendas(periodoVendasDTO.getFimVendas());
            periodoVendasExistente.setInicioVendas(periodoVendasDTO.getInicioVendas());
            periodoVendasExistente.setDescricao(periodoVendasDTO.getDescricao());
            periodoVendasExistente.setRetiradaPedido(periodoVendasDTO.getRetiradaPedido());
            periodoVendasExistente.setFornecedorId(fornecedorService.findFornecedorById(periodoVendasDTO.getFornecedorId()));

            periodoVendasExistente = this.iPeriodoVendasRepository.save(periodoVendasExistente);

            return PeriodoVendasDTO.of(periodoVendasExistente);
        }



        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para período de vendas de ID: [{}]", id);

        this.iPeriodoVendasRepository.deleteById(id);
    }

}
