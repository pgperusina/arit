package analizador.descendente;

import abstracto.AST;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.DotPrinter;
import excepciones.Excepcion;
import expresiones.Funcion;
import instrucciones.Break;
import instrucciones.Continue;
import instrucciones.Return;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.io.*;
import java.util.ArrayList;

import static utilities.Utils.agregarFuncionesNativas;
import static utilities.Utils.getRandomInRange;

public class Analizar {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new FileReader("/Users/pgarcia/Documents/USAC/1erSemestre2020/compi2/lab/arit/entrada.txt")));
//            Gramatica parser = new Gramatica(new BufferedReader(new FileReader("/Users/pgarcia/Documents/USAC/1erSemestre2020/compi2/lab/arit/entradaFinal.txt")));
//            Arbol arbol = parser.analizar();
            Arbol arbol = new Arbol(new ArrayList<>());
            arbol.setInstrucciones(parser.analizar(new ArrayList<>()));

            Tabla tabla = new Tabla(null);
            // Se agregan las funciones nativas a la tabla de símbolos
            agregarFuncionesNativas(tabla);
            arbol.setTablaGlobal(tabla);

            // Primer recorrido para agregar funciones a la TS
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
                    Object result = m.ejecutar(tabla, arbol);

                    if (result instanceof Excepcion) {
                        arbol.getExcepciones().add((Excepcion) result);
                    }
                    if (result instanceof Break) {
                        Excepcion ex = new Excepcion("Semántico", "Sentencia 'break' fuera de ciclo.", m.fila, m.columna);
                        arbol.getExcepciones().add(ex);
//                        ex.print(arbol.getConsola());
                    } else if (result instanceof Continue) {
                        Excepcion ex = new Excepcion("Semántico", "Sentencia 'continue' fuera de ciclo.", m.fila, m.columna);
                        arbol.getExcepciones().add(ex);
//                        ex.print(arbol.getConsola());
                    } else if (result instanceof Return) {
                        Excepcion ex = new Excepcion("Semántico", "Sentencia 'return' fuera de función.", m.fila, m.columna);
                        arbol.getExcepciones().add(ex);
//                        ex.print(arbol.getConsola());
                    }
                }
            }
            /**
             * Agrego a la lista de excepciones, las excepciones léxicas y sintácticas
             */
//            arbol.getExcepciones().addAll(parser.token_source.listaExcepciones);
                arbol.getExcepciones().addAll(parser.listaExcepciones);

            if (arbol.getExcepciones().size() != 0) {
                System.out.println("******* EXCEPCIONES ******");
                arbol.getExcepciones().forEach(excepcion -> {
                    System.out.println(excepcion.toString());
                });
            }

            StringBuilder dotBuilder = new StringBuilder();
            dotBuilder.append("digraph G \n");
            dotBuilder.append("{ \n");
            String padre = "";
            String hijo = "";
            for (AST instruccion : arbol.getInstrucciones()) {
                int random = getRandomInRange(1,10000);
                hijo = instruccion.getClass().getSimpleName()+random;
                dotBuilder.append("Arbol"+"->"+hijo);
                dotBuilder.append("\n");
                instruccion.crearDotFile(dotBuilder, hijo);
            }
            dotBuilder.append("}");
            System.out.println(dotBuilder.toString());
            System.out.println(System.getProperty("user.dir"));
            crearArchivoDot(dotBuilder);

        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (TokenMgrError e) {
            System.err.println(e.getMessage());
        }
    }

    public static void crearArchivoDot(StringBuilder dotBuilder) {
        try {

            File file = new File(System.getProperty("user.dir")+"/grafo.txt");
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(dotBuilder.toString());
            } finally {
                if (writer != null) writer.close();
            }

            String dotPath = "/usr/local/Cellar/graphviz/2.42.3/bin/dot";

            String fileInputPath = System.getProperty("user.dir")+"/grafo.txt";
            String fileOutputPath = System.getProperty("user.dir")+"/grafo.png";

            String tParam = "-Tpng";
            String tOParam = "-o";

            String[] cmd = new String[5];
            cmd[0] = dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;

            Runtime rt = Runtime.getRuntime();

            rt.exec( cmd );
            System.out.print("Arbol creado");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }
}
