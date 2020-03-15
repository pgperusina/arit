package analizador.descendente;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Funcion;
import instrucciones.Break;
import instrucciones.Continue;
import instrucciones.Return;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static utilities.Utils.agregarFuncionesNativas;

public class Analizar {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new FileReader("/Users/pgarcia/Documents/USAC/1erSemestre2020/compi2/lab/arit/entrada.txt")));
            Arbol arbol = parser.analizar();
            Tabla tabla = new Tabla(null);
            // Se agregan las funciones nativas a la tabla de símbolos
            agregarFuncionesNativas(tabla);
            arbol.setTablaGlobal(tabla);
            // Segundo recorrido para agregar funciones a la TS
            for (AST instruccion : arbol.getInstrucciones()) {
                if (instruccion instanceof Funcion) {
                    Object result = tabla.setFuncion((Funcion) instruccion);
                    if (result instanceof Excepcion) {
                        arbol.getExcepciones().add((Excepcion) result);
                        break;
                    }
                }
            }

            // TODO - validar que excepciones esté vacía para poder ejecutar el AST

            for (AST m : arbol.getInstrucciones()) {
                if (!(m instanceof Funcion)) {
                    Object result = m.interpretar(tabla, arbol);

                    if (result instanceof Excepcion) {
                        arbol.getExcepciones().add((Excepcion) result);
                    }
                    if (result instanceof Break) {
                        Excepcion ex = new Excepcion("Semántico", "Sentencia 'break' fuera de ciclo.", m.fila, m.columna);
                        arbol.getExcepciones().add(ex);
                        ex.print(arbol.getConsola());
                    } else if (result instanceof Continue) {
                        Excepcion ex = new Excepcion("Semántico", "Sentencia 'continue' fuera de ciclo.", m.fila, m.columna);
                        arbol.getExcepciones().add(ex);
                        ex.print(arbol.getConsola());
                    } else if (result instanceof Return) {
                        Excepcion ex = new Excepcion("Semántico", "Sentencia 'return' fuera de función.", m.fila, m.columna);
                        arbol.getExcepciones().add(ex);
                        ex.print(arbol.getConsola());
                    }
                }
            }

            System.out.println("******* EXCEPCIONES ******");
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
