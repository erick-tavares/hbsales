package br.com.hbsis.pedido;

import br.com.hbsis.email.EmailService;
import br.com.hbsis.exportimportcsv.ExportCSV;
import br.com.hbsis.exportimportcsv.ItemPedidoModel;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioDTO;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.pedidoitem.ItemPedido;
import br.com.hbsis.pedidoitem.ItemPedidoDTO;
import br.com.hbsis.periodovendas.PeriodoVendas;
import br.com.hbsis.periodovendas.PeriodoVendasDTO;
import br.com.hbsis.periodovendas.PeriodoVendasService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    private final ExportCSV exportCSV;
    private final EmailService emailService;

    public PedidoService(IPedidoRepository iPedidoRepository, ProdutoService produtoService, PeriodoVendasService periodoVendasService,
                         FuncionarioService funcionarioService, FornecedorService fornecedorService, ExportCSV exportCSV, EmailService emailService) {
        this.iPedidoRepository = iPedidoRepository;
        this.produtoService = produtoService;
        this.periodoVendasService = periodoVendasService;
        this.funcionarioService = funcionarioService;
        this.fornecedorService = fornecedorService;
        this.exportCSV = exportCSV;
        this.emailService = emailService;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {

        LOGGER.info("Salvando pedido");
        LOGGER.debug("Pedido: {}", pedidoDTO);

        validarPeriodoDoFornecedor(periodoVendasService.findById(pedidoDTO.getPeriodoVendasId()), fornecedorService.findById(pedidoDTO.getFornecedorId()));
        validarProdutoDoFornecedor(pedidoDTO.getItemDTOList(), fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));
        pedidoDTO.setStatus(StatusPedido.ATIVO.getDescricao().toUpperCase());
        this.validate(pedidoDTO);

        Pedido pedido = new Pedido();

        pedido.setDataCriacao(LocalDate.now());
        pedido.setCodigo(gerarCodigoPedido(pedidoDTO.getCodigo()));
        pedido.setStatus(pedidoDTO.getStatus());

        pedido.setFuncionarioId(funcionarioService.findFuncionarioById(pedidoDTO.getFuncionarioId()));
        pedido.setPeriodoVendasId(periodoVendasService.findPeriodoVendasById(pedidoDTO.getPeriodoVendasId()));
        pedido.setFornecedorId(fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));

        pedido.setItemList(preencherListaItens(pedidoDTO.getItemDTOList(), pedido));

        this.validarInvoice(pedido, funcionarioService.findById(pedidoDTO.getFuncionarioId()), fornecedorService.findById(pedidoDTO.getFornecedorId()));
        pedido = this.iPedidoRepository.save(pedido);
        emailService.sendSimpleMessage(pedidoDTO.getFuncionarioId(), pedidoDTO.getPeriodoVendasId());

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
        if (!((StringUtils.equalsIgnoreCase(pedidoDTO.getStatus(), "ativo")) || (StringUtils.equalsIgnoreCase(pedidoDTO.getStatus(),
                "cancelado")) || (StringUtils.equalsIgnoreCase(pedidoDTO.getStatus(), "retirado")))) {
            throw new IllegalArgumentException("O Status de ter o valor - ATIVO, RETIRADO ou CANCELADO -");
        }
    }

    public void exportCSVPorFornecedorPorFuncionario(HttpServletResponse response, Long id) throws IOException, java.text.ParseException {
        String header = "Funcionário;Produto;Quantidade;Fornecedor";
        exportCSV.exportarCSV(response, header);
        PrintWriter printWriter = response.getWriter();

        Fornecedor fornecedor = fornecedorService.findFornecedorById(id);
        String fornecedorDoPedido = (fornecedor.getRazaoSocial()) + " - " + exportCSV.mask(fornecedor.getCnpj());

        List<Pedido> pedidosDoFornecedor = this.findByFornecedor(fornecedor);

        List<ItemPedidoModel> itemConferido = new ArrayList<>();

        for (Pedido pedidoCSVFornecedor : pedidosDoFornecedor) {
            Funcionario funcionario = funcionarioService.findFuncionarioById(pedidoCSVFornecedor.getFuncionarioId().getId());
            itemConferido = popularListaCSVPorFuncionario(pedidoCSVFornecedor.getItemList(), itemConferido, funcionario.getNome());
        }

        for (ItemPedidoModel itemModel : itemConferido) {
            printWriter.println(itemModel.getFuncionario() + ";" +
                    itemModel.getProduto().getNome() + ";" +
                    itemModel.getQuantidade() + ";" +
                    fornecedorDoPedido);
        }
    }


    public void exportCSVPorPeriodoPorFornecedor(HttpServletResponse response, Long id) throws IOException, ParseException, java.text.ParseException {
        String header = "Produto;Quantidade;Fornecedor";
        exportCSV.exportarCSV(response, header);
        PrintWriter printWriter = response.getWriter();

        PeriodoVendas periodoVendas = periodoVendasService.findPeriodoVendasById(id);
        Fornecedor fornecedorDoPeriodo = periodoVendas.getFornecedorId();

        List<Pedido> pedidoLista = this.findByPeriodo(periodoVendas);
        String pedidoFornecedor = (fornecedorDoPeriodo.getRazaoSocial()) + " - " + exportCSV.mask(fornecedorDoPeriodo.getCnpj());

        List<ItemPedidoModel> itemConferido = new ArrayList<>();
        for (Pedido pedidoCSVObjeto : pedidoLista) {
            itemConferido = popularListaCSVPorPeriodo(pedidoCSVObjeto.getItemList(), itemConferido);

        }
        for (ItemPedidoModel itemPedidoModel : itemConferido) {
            printWriter.println(itemPedidoModel.getProduto().getNome() + ";" + itemPedidoModel.getQuantidade() + ";" + pedidoFornecedor);
        }
    }


    private List<ItemPedidoModel> popularListaCSVPorPeriodo(List<ItemPedido> itensExistentes, List<ItemPedidoModel> itemRetorno) {

        for (ItemPedido item : itensExistentes) {
            if (itemRetorno.isEmpty()) {
                itemRetorno.add(new ItemPedidoModel(item.getQuantidade(), item.getProdutoId()));

            } else {
                for (ItemPedidoModel itemNovo : itemRetorno) {
                    if (item.getProdutoId().getId().equals(itemNovo.getProduto().getId())) {
                        itemNovo.setQuantidade(itemNovo.getQuantidade() + item.getQuantidade());
                        break;

                    } else if ((itemRetorno.indexOf(itemNovo) + 1) == itemRetorno.size()) {
                        itemRetorno.add(new ItemPedidoModel(item.getQuantidade(), item.getProdutoId()));
                        break;
                    }
                }
            }
        }

        return itemRetorno;
    }

    private List<ItemPedidoModel> popularListaCSVPorFuncionario(List<ItemPedido> itemExistente, List<ItemPedidoModel> itemRetorno, String funcionario) {

        for (ItemPedido item : itemExistente) {
            if (itemRetorno.isEmpty()) {
                itemRetorno.add(new ItemPedidoModel(item.getQuantidade(), item.getProdutoId(), funcionario));

            } else {
                for (ItemPedidoModel itemNovo : itemRetorno) {
                    if (item.getProdutoId().getId().equals(itemNovo.getProduto().getId()) && funcionario.equals(itemNovo.getFuncionario())) {
                        itemNovo.setQuantidade(itemNovo.getQuantidade() + item.getQuantidade());
                        break;

                    } else if ((itemRetorno.indexOf(itemNovo) + 1) == itemRetorno.size()) {
                        itemRetorno.add(new ItemPedidoModel(item.getQuantidade(), item.getProdutoId(), funcionario));
                        break;
                    }
                }
            }
        }

        return itemRetorno;
    }

    public List<ItemPedido> preencherListaItens(List<ItemPedidoDTO> itemPedidoDTO, Pedido pedido) {
        List<ItemPedido> itemPedidoList = new ArrayList<>();
        for (ItemPedidoDTO itemPedidoDTOList : itemPedidoDTO) {
            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setProdutoId(produtoService.findProdutoById(itemPedidoDTOList.getProdutoId()));
            itemPedido.setPedidoId(pedido);

            itemPedido.setQuantidade(itemPedidoDTOList.getQuantidade());
            itemPedido.setValorUnitario(produtoService.findProdutoById(itemPedidoDTOList.getProdutoId()).getPreco());

            itemPedidoList.add(itemPedido);
        }
        return itemPedidoList;
    }

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

    public Pedido findPedidoById(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Pedido> findByPeriodo(PeriodoVendas periodoVendas) {
        List<Pedido> listPedido = this.iPedidoRepository.findByPeriodoVendasId(periodoVendas);

        return listPedido;
    }

    public List<Pedido> findByFornecedor(Fornecedor fornecedor) {
        List<Pedido> listPedido = this.iPedidoRepository.findByFornecedorId(fornecedor);

        return listPedido;
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);

        if (pedidoExistenteOptional.isPresent()) {

            Pedido pedidoExistente = pedidoExistenteOptional.get();

            this.validarPeriodoDoFornecedor(periodoVendasService.findById(pedidoDTO.getPeriodoVendasId()), fornecedorService.findById(pedidoDTO.getFornecedorId()));
            this.validarProdutoDoFornecedor(pedidoDTO.getItemDTOList(), fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));
            this.validarStatus(id);

            LOGGER.info("Atualizando pedido... id: [{}]", pedidoExistente.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedidoExistente);

            pedidoExistente.setCodigo(gerarCodigoPedido(pedidoDTO.getCodigo()));
            pedidoExistente.setStatus(pedidoDTO.getStatus().toUpperCase());

            pedidoExistente.setFuncionarioId(funcionarioService.findFuncionarioById(pedidoDTO.getFuncionarioId()));
            pedidoExistente.setPeriodoVendasId(periodoVendasService.findPeriodoVendasById(pedidoDTO.getPeriodoVendasId()));
            pedidoExistente.setFornecedorId(fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));


            pedidoExistente.updateItens(this.preencherListaItens(pedidoDTO.getItemDTOList(), pedidoExistente));
            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);

            return PedidoDTO.of(pedidoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }

    public double valorTotal(List<ItemPedido> item) {
        double valorTotalPedido = 0;
        for (ItemPedido itemPedidoObjeto : item) {
            Produto produto = produtoService.findProdutoById(itemPedidoObjeto.getProdutoId().getId());

            double valorTotalItem = (produto.getPreco() * (itemPedidoObjeto.getQuantidade()));
            valorTotalPedido += valorTotalItem;
            LOGGER.info("Somando valor total do pedido");

        }
        LOGGER.info("Valor total do pedido : " + valorTotalPedido);
        return valorTotalPedido;
    }

    private List<InvoiceItemDTO> parseItemPedidoToInvoiceItem(List<ItemPedido> itemPedidos) {
        List<InvoiceItemDTO> invoiceItemDTOSet = new ArrayList<>();
        try {
            for (ItemPedido item : itemPedidos) {
                InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO(item.getQuantidade(), item.getProdutoId().getNome());
                invoiceItemDTOSet.add(invoiceItemDTO);
            }
            LOGGER.info("Convertendo lista de ItemPedido para InvoiceItemDTO");
            return invoiceItemDTOSet;
        } catch (Exception e) {
            LOGGER.error("Erro ao converter lista");
        }
        return invoiceItemDTOSet;
    }


    public boolean validarProdutoDoFornecedor(List<ItemPedidoDTO> itemDTO, Fornecedor fornecedor) {
        LOGGER.info("Validando produto do fornecedor");

        for (ItemPedidoDTO item : itemDTO) {
            Produto produtoExistente = produtoService.findProdutoById(item.getProdutoId());

            if (produtoExistente.getLinhaCategoriaId().getCategoriaId().getFornecedorId().getId().equals(fornecedor.getId())) {

                LOGGER.info("Produto existente para este fornecedor");
                return true;
            }
        }
        throw new IllegalArgumentException("Produto não existe para esse fornecedor");
    }

    public boolean validarPeriodoDoFornecedor(PeriodoVendasDTO periodoVendasDTO, FornecedorDTO fornecedorDTO) {
        LOGGER.info("Validando período de vendas do pedido");

        PeriodoVendas periodoExistente = periodoVendasService.findPeriodoVendasById(periodoVendasDTO.getId());

        List<PeriodoVendas> periodoLista = periodoVendasService.findByFornecedor(periodoExistente.getFornecedorId());
        for (PeriodoVendas periodo : periodoLista) {
            if (periodo.getFornecedorId().getId().equals(fornecedorDTO.getId())) {

                LOGGER.info("Período de vendas existente para este fornecedor");
                return true;
            }
        }
        throw new IllegalArgumentException("Período de vendas não existe para esse fornecedor");
    }

    private boolean validarInvoice(Pedido pedido, FuncionarioDTO funcionario, FornecedorDTO fornecedor) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "f59ff556-1b67-11ea-978f-2e728ce88125");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        InvoiceDTO invoiceDTO = new InvoiceDTO(fornecedor.getCnpj(),
                funcionario.getUuid(),
                parseItemPedidoToInvoiceItem(pedido.getItemList()),
                valorTotal(pedido.getItemList()));

        HttpEntity<InvoiceDTO> httpEntity = new HttpEntity<>(invoiceDTO, headers);

        ResponseEntity<InvoiceDTO> responseInvoice = restTemplate.exchange(
                "http://10.2.54.25:9999/api/invoice", HttpMethod.POST, httpEntity, InvoiceDTO.class);
        if (responseInvoice.getStatusCodeValue() == 200 || responseInvoice.getStatusCodeValue() == 201) {
            LOGGER.info("Validando pedido em HBEmployee ");
            return true;
        }
        throw new IllegalArgumentException("Pedido não validado " + responseInvoice.getStatusCodeValue());
    }

    public Pedido validarStatus(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            Pedido pedidoExistente = pedidoOptional.get();
            if (pedidoExistente.getStatus().equalsIgnoreCase(StatusPedido.ATIVO.getDescricao())) {
                LocalDate dataAtual = LocalDate.now();
                if ((dataAtual.isAfter(pedidoExistente.getPeriodoVendasId().getInicioVendas())) &&
                        (dataAtual.isBefore(pedidoExistente.getPeriodoVendasId().getFimVendas()))) {
                    return pedidoExistente;
                } else {
                    throw new IllegalArgumentException("O pedido está fora do período de vendas");
                }
            } else {
                LOGGER.info("Pedido... id: [{}] não está ativo", id);
                throw new IllegalArgumentException("Somente pedidos ativos podem ser alterados");
            }
        } else {
            throw new IllegalArgumentException(String.format("ID %s não existe", id));
        }
    }


    public PedidoDTO cancelarPedido(Long id) {
        Pedido pedido = validarStatus(id);
        pedido.setStatus(StatusPedido.CANCELADO.getDescricao().toUpperCase());
        iPedidoRepository.save(pedido);
        return PedidoDTO.of(pedido);
    }

    public PedidoDTO retirarPedido(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            Pedido pedidoExistente = pedidoOptional.get();
            if (pedidoExistente.getStatus().equalsIgnoreCase(StatusPedido.ATIVO.getDescricao())) {
                if (pedidoExistente.getPeriodoVendasId().getRetiradaPedido().equals(LocalDate.now())) {
                    pedidoExistente.setStatus(StatusPedido.RETIRADO.getDescricao().toUpperCase());
                    iPedidoRepository.save(pedidoExistente);
                    return PedidoDTO.of(pedidoExistente);
                } else {
                    throw new IllegalArgumentException("O pedido não pode ser retirado antes da data prevista");
                }
            } else {
                LOGGER.info("Pedido... id: [{}] não está ativo", id);
                throw new IllegalArgumentException("Somente pedidos ativos podem ser alterados");
            }
        } else {
            throw new IllegalArgumentException(String.format("ID %s não existe", id));
        }
    }

    public List<PedidoDTO> visualizarPedidoDoFuncionario(Long id) {
        Funcionario funcionarioExistente = funcionarioService.findFuncionarioById(id);
        List<PedidoDTO> pedidoDTO = new ArrayList<>();

            List<Pedido> pedidoDoFuncionario = this.iPedidoRepository.findByFuncionarioId(funcionarioExistente);
            LOGGER.info("Visualizando pedidos do funcionario [{}]", funcionarioExistente.getNome());

            for (Pedido pedido : pedidoDoFuncionario) {
                if (pedido.getStatus().equalsIgnoreCase("ATIVO") || (pedido.getStatus().equalsIgnoreCase("RETIRADO"))) {
                    pedidoDTO.add(PedidoDTO.of(pedido));
                }
            }
            return pedidoDTO;
    }
}