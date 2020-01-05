package br.com.hbsis.pedido;

import br.com.hbsis.exportimportcsv.ExportCSV;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class PedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository iPedidoRepository;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final ExportCSV exportCSV;

    public PedidoService(IPedidoRepository iPedidoRepository, FornecedorService fornecedorService, ProdutoService produtoService, ExportCSV exportCSV) {
        this.iPedidoRepository = iPedidoRepository;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.exportCSV = exportCSV;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {

        this.validate(pedidoDTO);

        LOGGER.info("Salvando pedido");
        LOGGER.debug("Pedido: {}", pedidoDTO);

        Pedido pedido = new Pedido();
        pedido.setCodigo(gerarCodigoPedido(pedidoDTO.getCodigo()));
        pedido.setDataCriacao(pedidoDTO.getDataCriacao());
        pedido.setStatus(pedidoDTO.getStatus());

        pedido.setFornecedorId(fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));

        pedido.setProdutoId(produtoService.findByProdutoId(pedidoDTO.getProdutoId()));

        pedido = this.iPedidoRepository.save(pedido);

        return PedidoDTO.of(pedido);
    }

    private void validate(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando linha da categoria");

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
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getFornecedorId()))) {
            throw new IllegalArgumentException("FornecedorId não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getProdutoId()))) {
            throw new IllegalArgumentException("ProdutoId não deve ser nulo/vazio");
        }
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

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);

        if (pedidoExistenteOptional.isPresent()) {

            Pedido pedidoExistente = pedidoExistenteOptional.get();

            LOGGER.info("Atualizando pedido... id: [{}]", pedidoExistente.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedidoExistente);

            pedidoExistente.setCodigo(gerarCodigoPedido(pedidoDTO.getCodigo()));
            pedidoExistente.setDataCriacao(pedidoDTO.getDataCriacao());
            pedidoExistente.setStatus(pedidoDTO.getStatus());

            pedidoExistente.setFornecedorId(fornecedorService.findFornecedorById(pedidoDTO.getFornecedorId()));
            pedidoExistente.setProdutoId(produtoService.findByProdutoId(pedidoDTO.getProdutoId()));

            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);

            return PedidoDTO.of(pedidoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }


    public void exportCSV(HttpServletResponse response) throws IOException {
        String header = "Código da Linha;Linha da categoria;Código da categoria;Categoria";
        exportCSV.exportarCSV(response, header);

        PrintWriter printWriter = response.getWriter();
        for (LinhaCategoria linhaCategoriaCSVObjeto : this.iPedidoRepository.findAll()) {
            String linhaCategoriaCodigo = linhaCategoriaCSVObjeto.getCodigo();
            String linhaCategoriaNome = linhaCategoriaCSVObjeto.getNome();
            String categoriaProdutoCodigo = linhaCategoriaCSVObjeto.getCategoriaId().getCodigo();
            String categoriaProdutoNome = linhaCategoriaCSVObjeto.getCategoriaId().getNome();

            printWriter.println(linhaCategoriaCodigo + ";" + linhaCategoriaNome + ";" + categoriaProdutoCodigo + ";" + categoriaProdutoNome);
        }
        printWriter.close();
    }
}
