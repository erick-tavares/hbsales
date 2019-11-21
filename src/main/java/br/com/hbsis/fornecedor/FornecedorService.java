package br.com.hbsis.fornecedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {
	private final IFornecedorRepository iFornecedorRepository;

	@Autowired
	public FornecedorService(IFornecedorRepository iFornecedorRepository) {
		this.iFornecedorRepository = iFornecedorRepository;
	}

	public FornecedorDTO save(FornecedorDTO fornecedorDTO) {
		Fornecedor fornecedor = new Fornecedor(
				fornecedorDTO.getNome(),
				fornecedorDTO.getCnpj()
		);

		fornecedor = this.iFornecedorRepository.save(fornecedor);

		return FornecedorDTO.of(fornecedor);
	}

}
