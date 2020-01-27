package br.com.hbsis.email;


import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.periodovendas.PeriodoVendas;
import br.com.hbsis.periodovendas.PeriodoVendasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
    public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(br.com.hbsis.email.EmailService.class);

    private final FuncionarioService funcionarioService;
    private final PeriodoVendasService periodoVendasService;

        @Autowired
        public JavaMailSender emailSender;

    public EmailService(FuncionarioService funcionarioService, PeriodoVendasService periodoVendasService) {
        this.funcionarioService = funcionarioService;
        this.periodoVendasService = periodoVendasService;
    }


    public void sendSimpleMessage(Long funcionarioId, Long periodoVendasId) {

        Optional<Funcionario> funcionarioExistente = Optional.ofNullable(funcionarioService.findFuncionarioById(funcionarioId));
        Optional<PeriodoVendas> periodoExistente = Optional.ofNullable(periodoVendasService.findPeriodoVendasById(periodoVendasId));

        if (funcionarioExistente.isPresent() && periodoExistente.isPresent()){
            Funcionario funcionario = funcionarioExistente.get();
            PeriodoVendas periodoVendas = periodoExistente.get();

        SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(funcionario.getEmail());
            message.setSubject("Confirmação de Pedido de Vendas");
            message.setText(corpoDoEmail(funcionario.getId(), periodoVendas.getId()));

            try {
                emailSender.send(message);
                LOGGER.info ("Email enviado com sucesso!");
            } catch (Exception e) {
                LOGGER.info ("Erro ao enviar email.");
            }
        }
    }

    public String corpoDoEmail (Long funcionarioId, Long periodoVendasId){
            Funcionario funcionario = funcionarioService.findFuncionarioById(funcionarioId);
            PeriodoVendas periodoVendas = periodoVendasService.findPeriodoVendasById(periodoVendasId);
            String corpo = "";

            corpo = "Olá " + funcionario.getNome() + ", o seu pedido de vendas foi confirmado, " + "\n" +
                    "Data de retirada do pedido: " + periodoVendas.getRetiradaPedido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;

            return corpo;
        }
    }

    
