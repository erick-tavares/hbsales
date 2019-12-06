package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CnpjValidator implements ConstraintValidator<CnpjValidation, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CnpjValidator.class);

	private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	public void initialize(CnpjValidation constraint) {
		LOGGER.debug("Inicializando CnpjValidator...");
	}

	public boolean isValid(String cnpj, ConstraintValidatorContext context) {
		LOGGER.info("Validando cnpj {}", cnpj);

		if (StringUtils.isEmpty(cnpj)) {
			return false;
		}

		Pattern pattern = Pattern.compile("\\d{14}");
		Matcher matcher = pattern.matcher(cnpj);

		return matcher.find() && isValidCNPJ(cnpj);
	}

	public static int calcularDigitoCnpj(String cnpj) {
		int soma = 0;

		for (int indice = cnpj.length() - 1, digito; indice >= 0; indice--) {

			digito = Integer.parseInt(cnpj.substring(indice, indice + 1));

			soma += digito * CnpjValidator.pesoCNPJ[CnpjValidator.pesoCNPJ.length - cnpj.length() + indice];
		}

		soma = 11 - soma % 11;

		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCNPJ(String cnpj) {
		cnpj = cnpj.trim().replace(".", "").replace("-", "");

		if ((cnpj.length() != 14)) {
			return false;
		}

		int digito1 = calcularDigitoCnpj(cnpj.substring(0, 12));
		int digito2 = calcularDigitoCnpj(cnpj.substring(0, 12) + digito1);
		return cnpj.equals(cnpj.substring(0, 12) + digito1 + digito2);
	}

}
