package br.com.hbsis.funcionario;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FuncionarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(br.com.hbsis.funcionario.FuncionarioService.class);

    private final IFuncionarioRepository iFuncionarioRepository;

    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }

    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO) {

        this.validarEmployee(funcionarioDTO);
        this.validate(funcionarioDTO);

        LOGGER.info("Salvando funcionário");
        LOGGER.debug("Funcionario: {}", funcionarioDTO);

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setEmail(funcionarioDTO.getEmail());
        funcionario.setUuid(UUID.randomUUID().toString());

        funcionario = this.iFuncionarioRepository.save(funcionario);

        return FuncionarioDTO.of(funcionario);
    }

    private void validate(FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Validando Funcionario");

        if (funcionarioDTO == null) {
            throw new IllegalArgumentException("FuncionarioDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(funcionarioDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(funcionarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(funcionarioDTO.getUuid())) {
            throw new IllegalArgumentException("UUID não deve ser nulo/vazio");
        }
    }

    public Funcionario findFuncionarioById(Long id) {
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()) {
            return funcionarioOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public FuncionarioDTO findById(Long id) {
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()) {
            return FuncionarioDTO.of(funcionarioOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO, Long id) {
        Optional<Funcionario> funcionarioExistenteOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioExistenteOptional.isPresent()) {
            Funcionario funcionarioExistente = funcionarioExistenteOptional.get();

            LOGGER.info("Atualizando funcionário... id: [{}]", funcionarioExistente.getId());
            LOGGER.debug("Payload: {}", funcionarioDTO);
            LOGGER.debug("Funcionario Existente: {}", funcionarioExistente);

            funcionarioExistente.setNome(funcionarioDTO.getNome());
            funcionarioExistente.setEmail(funcionarioDTO.getEmail());
            funcionarioExistente.setUuid(funcionarioDTO.getUuid());

            funcionarioExistente = this.iFuncionarioRepository.save(funcionarioExistente);

            return FuncionarioDTO.of(funcionarioExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para funcionário de ID: [{}]", id);

        this.iFuncionarioRepository.deleteById(id);
    }

    private void validarEmployee(FuncionarioDTO funcionarioDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "f59ff556-1b67-11ea-978f-2e728ce88125");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<FuncionarioDTO> httpEntity = new HttpEntity<>(funcionarioDTO, headers);

        ResponseEntity<EmployeeDTO> responseEmployee = restTemplate.exchange("http://10.2.54.25:9999/api/employees", HttpMethod.POST, httpEntity, EmployeeDTO.class);
        funcionarioDTO.setUuid((Objects.requireNonNull(responseEmployee.getBody())).getEmployeeUuid());

        LOGGER.info("Validando funcionário em HBEmployee " + responseEmployee.getBody().getEmployeeUuid());
    }

}
