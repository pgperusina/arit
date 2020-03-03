package analizador.descendente;

import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

public class Analizar {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new FileReader("/Users/pgarcia/Documents/USAC/1erSemestre2020/compi2/lab/arit/entradaCorregida.arit")));
            Arbol arbol = parser.analizar();
            Tabla tabla = new Tabla(null);
            arbol.getInstrucciones().forEach(instruccion -> {
                if (instruccion instanceof Funcion) {
                    tabla.setFuncion((Funcion) instruccion);
                }
            });

//            arbol.getInstrucciones().forEach(instruccion -> {
//                if (!(instruccion instanceof Funcion)) {
//                    Object result = instruccion.interpretar(tabla, arbol);
//
//                    if (result instanceof Excepcion) {
//                        System.out.println(((Excepcion) result).toString());
//                    }
//
//                }
//            });

            arbol.getExcepciones().forEach(excepcion -> {
               System.out.println(excepcion.toString());
            });
            System.out.print(arbol);
            System.out.print(arbol);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (TokenMgrError e) {
            System.err.println(e.getMessage());
        }
    }
}
