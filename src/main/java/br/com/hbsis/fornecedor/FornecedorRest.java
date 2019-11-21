package br.com.hbsis.fornecedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorRest {

	private final FornecedorService fornecedorService;

	@Autowired
	public FornecedorRest(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}

	@PostMapping
	public FornecedorDTO save(@RequestBody FornecedorDTO fornecedorDTO) {
		return this.fornecedorService.save(fornecedorDTO);
	}

}
