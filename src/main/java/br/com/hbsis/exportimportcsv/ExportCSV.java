package br.com.hbsis.exportimportcsv;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

@Component
public class ExportCSV {

    public PrintWriter exportarCSV (HttpServletResponse response, String header) throws IOException {
        String arquivoCSV = "arquivo.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", arquivoCSV);
        response.setHeader(headerKey, headerValue);
        PrintWriter printWriter = response.getWriter();

        printWriter.println(header);
        return  printWriter;
    }

    public String mask (String cnpj) throws ParseException {
        MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
        mask.setValueContainsLiteralCharacters(false);
        return mask.valueToString(cnpj);
    }


}
