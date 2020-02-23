package analizador.descendente;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

public class Analizar {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new FileReader("/Users/pgarcia/Documents/USAC/1erSemestre2020/compi2/lab/arit/entrada.txt")));
            String arbol = parser.Analizar();
            System.out.print(arbol);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (TokenMgrError e) {
            System.err.println(e.getMessage());
        }
    }
}
