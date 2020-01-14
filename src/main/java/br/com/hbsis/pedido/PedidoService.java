package br.com.hbsis.pedido;

import br.com.hbsis.exportimportcsv.ExportCSV;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.pedidoitem.ItemPedido;
import br.com.hbsis.pedidoitem.ItemPedidoDTO;
import br.com.hbsis.pedidoitem.ItemPedidoSave;
import br.com.hbsis.periodovendas.PeriodoVendas;
import br.com.hbsis.periodovendas.PeriodoVendasDTO;
import br.com.hbsis.periodovendas.PeriodoVendasService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class PedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository iPedidoRepository;
    private final ProdutoService produtoService;
    private final PeriodoVendasService periodoVendasService;
    private final FuncionarioService funcionarioService;
    private final FornecedorService fornecedorService;
    private final ItemPedidoSave itemPedidoSave;
    private final ExportCSV exportCSV;

    public PedidoService(IPedidoRepository iPedidoRepository, ProdutoService produtoService, PeriodoVendasService periodoVendasService,
                         FuncionarioService funcionarioService, FornecedorService fornecedorService, ItemPedidoSave itemPedidoSave, ExportCSV exportCSV) {
        this.iPedidoRepository = iPedidoRepository;
        this.produtoService = produtoService;
        this.periodoVendasService = periodoVendasService;
        this.funcionarioService = funcionarioService;
        this.fornecedorService = fornecedorService;
        this.itemPedidoSave = itemPedidoSave;
        this.exportCSV = exportCSV;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {

        LOGGER.info("Salvando pedido");
        LOGGER.debug("Pedido: {}", pedidoDTO);

        validarPeriodoDoFornecedor(periodoVendasService.findById(pedidoDTO.getPeriodoVendasId()), fornecedorService.findById(pedidoDTO.getFornecedorId()));
       // validarProdutoDoFornecedor(produtoService.findProdutoById(pedidoDTO.getItemDTOList().get().getProdutoId()) , fornecedorService.findById(pedidoDTO.getFornecedorId()));
        this.validate(pedidoDTO);

        Pedido pedido = new Pedido();
        List<ItemPedido> itemPedidosList = new ArrayList<>();

        pedido.setDataCriacao(LocalDate.now());
        pedido.setCodigo(gerarCodigoPedido(pedidoDTO.getCodigo()));
        pedido.setStatus(StatusPedido.ATIVO);

        pedido.setFuncionarioId(funcionarioService.findFuncionarioById(pedidoDTO.getFuncionarioId()));
        pedido.setPeriodoVendasId(periodoVendasService.findPeriodoVendasById(pedidoDTO.getPeriodoVendasId()));
        pedido.setFornecedorId(fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));

        pedido = this.iPedidoRepository.save(pedido);


        for (ItemPedidoDTO itemPedidoDTO : pedidoDTO.getItemDTOList()) {
            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setProdutoId(produtoService.findProdutoById(itemPedidoDTO.getProdutoId()));
            itemPedido.setPedidoId(pedido);

            itemPedido.setQuantidade(itemPedidoDTO.getQuantidade());
            itemPedido.setValorUnitario(produtoService.findProdutoById(itemPedidoDTO.getProdutoId()).getPreco());
            itemPedido.setId(itemPedidoSave.save(itemPedido).getId());

            validarProduto(itemPedidoDTO.getProdutoId(),fornecedorService.findById(pedidoDTO.getFornecedorId()));
            itemPedidosList.add(itemPedido);
        }

        pedido.setItemList(itemPedidosList);
        return PedidoDTO.of(pedido);

    }

    private void validate(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando pedido");

        if (pedidoDTO == null) {
            throw new IllegalArgumentException("PedidoDTO não deve ser nula");
        }
        if (StringUtils.isEmpty(pedidoDTO.getCodigo())) {
            throw new IllegalArgumentException("Código não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getDataCriacao()))) {
            throw new IllegalArgumentException("Data de criação não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getStatus()))) {
            throw new IllegalArgumentException("Status não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getFuncionarioId()))) {
            throw new IllegalArgumentException("FuncionarioId não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getPeriodoVendasId()))) {
            throw new IllegalArgumentException("PeriodoId não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getFornecedorId()))) {
            throw new IllegalArgumentException("FornecedorId não deve ser nulo/vazio");
        }
    }

    public boolean validarProduto (Long produtoDTO, FornecedorDTO fornecedorDTO){
        LOGGER.info("Validando produto do fornecedor");
        Optional<Produto> produtoExistente = Optional.ofNullable(produtoService.findProdutoById(produtoDTO));
        if (produtoExistente.isPresent()) {
                if (produtoExistente.get().getLinhaCategoriaId().getCategoriaId().getFornecedorId().getId()
                        .equals(fornecedorDTO.getId())) {

                    LOGGER.info("Produto existente para este fornecedor");
                    return true;
            }
            throw new IllegalArgumentException("Produto não existe para esse fornecedor");
        }
        throw new IllegalArgumentException("Produto não existe");
    }



    public boolean validarPeriodoDoFornecedor(PeriodoVendasDTO periodoVendasDTO, FornecedorDTO fornecedorDTO) {
        LOGGER.info("Validando período de vendas do pedido");

        Optional<PeriodoVendas> periodoExistente = Optional.ofNullable(periodoVendasService.findPeriodoVendasById(periodoVendasDTO.getId()));
        if (periodoExistente.isPresent()) {

            List<PeriodoVendas> periodoLista = periodoVendasService.findByList(periodoExistente.get().getFornecedorId());
            for (PeriodoVendas periodo : periodoLista) {
                if (periodo.getFornecedorId().getId().equals(fornecedorDTO.getId())) {

                    LOGGER.info("Período de vendas existente para este fornecedor");
                    return true;
                }
            }
            throw new IllegalArgumentException("Período de vendas não existe para esse fornecedor");
        }
        throw new IllegalArgumentException("Período de vendas não existe");
    }


//    public boolean validarProdutoDoFornecedor(Long produtoDTO, FornecedorDTO fornecedorDTO) {
//        LOGGER.info("Validando produto do fornecedor");
//
//        Optional<Produto> produtoExistente = Optional.ofNullable(produtoService.findProdutoById(produtoDTO));
//        if (produtoExistente.isPresent()) {
//
//            List<Produto> produtoLista = produtoService.findByList(produtoExistente.get().getLinhaCategoriaId().getCategoriaId().getFornecedorId());
//            for (Produto produto : produtoLista) {
//                if (produto.getLinhaCategoriaId().getCategoriaId().getFornecedorId().getId().equals(fornecedorDTO.getId())) {
//
//                    LOGGER.info("Produto existente para este fornecedor");
//                    return true;
//                }
//            }
//            throw new IllegalArgumentException("Produto não existe para esse fornecedor");
//        }
//        throw new IllegalArgumentException("Produto não existe");
//    }

    public String gerarCodigoPedido(String codigoDoUsuario) {

        if (codigoDoUsuario.length() < 10) {
            String codigoGerado = String.format("%10s", codigoDoUsuario).toUpperCase();
            codigoGerado = codigoGerado.replace(' ', '0');

            return codigoGerado;
        }
        return codigoDoUsuario;
    }


    public PedidoDTO findById(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return PedidoDTO.of(pedidoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Pedido findByPedidoId(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);

        if (pedidoExistenteOptional.isPresent()) {

            Pedido pedidoExistente = pedidoExistenteOptional.get();

            LOGGER.info("Atualizando pedido... id: [{}]", pedidoExistente.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedidoExistente);

            pedidoExistente.setCodigo(gerarCodigoPedido(pedidoDTO.getCodigo()));
            pedidoExistente.setStatus(pedidoDTO.getStatus());

            pedidoExistente.setFuncionarioId(funcionarioService.findFuncionarioById(pedidoDTO.getFuncionarioId()));
            pedidoExistente.setPeriodoVendasId(periodoVendasService.findPeriodoVendasById(pedidoDTO.getPeriodoVendasId()));

            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);

            return PedidoDTO.of(pedidoExistente);

        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }

    private void validarEmployee(InvoiceDTO invoiceDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "f59ff556-1b67-11ea-978f-2e728ce88125");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<InvoiceDTO> httpEntity = new HttpEntity<>(invoiceDTO, headers);

        ResponseEntity<InvoiceDTO> responseEmployee = restTemplate.exchange("http://10.2.54.25:9999/api/invoice", HttpMethod.POST, httpEntity, InvoiceDTO.class);
        invoiceDTO.setEmployeeUuid((Objects.requireNonNull(responseEmployee.getBody())).getEmployeeUuid());

        LOGGER.info("Validando pedido em HBEmployee " + responseEmployee.getBody().getEmployeeUuid());
    }

//    ///////////exercicio 15,CSV com a quantidade dos produtos vendidos por período por Fornecedor
//    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {
//        String header = "Produto;Quantidade;Fornecedor";
//        exportCSV.exportarCSV(response, header);
//
//        PrintWriter printWriter = response.getWriter();
//        Fornecedor fornecedorDoPeriodo = fornecedorService.findFornecedorById();
//        for (Pedido pedidoCSVObjeto : periodoVendasService.findByFornecedorId(pedido)) {
//
//            String pedidoProduto = (pedidoCSVObjeto.getProdutoId().getNome());
//            String pedidoQuantidade = String.valueOf(pedidoCSVObjeto.getQuantidadeItens());
//            String pedidoFornecedor = (pedidoCSVObjeto.getFornecedorId().getRazaoSocial()) + " - " + exportCSV.mask(pedidoCSVObjeto.getFornecedorId().getCnpj());
//
//
//            printWriter.println(pedidoProduto + ";" + pedidoQuantidade + ";" + pedidoFornecedor);
//        }
//        printWriter.close();
//    }
}
