package analizador.descendente;

import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import static commons.Helper.agregarFuncionesNativas;

public class Analizar {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new FileReader("/Users/pgarcia/Documents/USAC/1erSemestre2020/compi2/lab/arit/entradaCorregida.arit")));
            Arbol arbol = parser.analizar();
            Tabla tabla = new Tabla(null);
            // Se agregan las funciones nativas a la tabla de símbolos
            agregarFuncionesNativas(tabla);
            // Primer recorrido al arbol para reconocer funciones
            arbol.setTablaGlobal(tabla);
            arbol.getInstrucciones().forEach(instruccion -> {
                if (instruccion instanceof Funcion) {
                    tabla.setFuncion((Funcion) instruccion);
                }
            });

//            tree.getInstrucciones().forEach(m -> {
//            if (!(m instanceof Funcion)) {
//                Object result = m.interpretar(tabla, tree);
//
//                if (result instanceof Excepcion) {
//                    ((Excepcion) result).imprimir(tree.getConsola());
//                }
//                if (result instanceof Detener) {
//                    Excepcion ex = new Excepcion("Semantico", "Sentencia break fuera de ciclo.", m.fila, m.columna);
//                    tree.getExcepciones().add(ex);
//                    ex.imprimir(tree.getConsola());
//                } else if (result instanceof Retorno) {
//                    Excepcion ex = new Excepcion("Semantico", "Sentencia retorno fuera de funcion.", m.fila, m.columna);
//                    tree.getExcepciones().add(ex);
//                    ex.imprimir(tree.getConsola());
//                }
//            }
//        });

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
