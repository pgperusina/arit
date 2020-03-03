package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import instrucciones.Declaracion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class Llamada extends AST {

    private String nombre;
    private ArrayList<AST> argumentos;

    public Llamada(String nombre, ArrayList<AST> argumentos, int fila, int columna) {
        this.nombre = nombre;
        this.argumentos = argumentos;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        Object result = tabla.getFuncion(nombre);
        if (result == null) {
            Excepcion ex = new Excepcion("Semántico", "No se ha encontrado la función " + this.nombre, fila, columna);
            tree.getExcepciones().add(ex);
            return ex;
        }
        Funcion f = (Funcion) result;
        this.tipo = f.getTipo();
        if (f.getParametros().size() == this.argumentos.size()) {
            Tabla t = new Tabla(tree.getTablaGlobal());
            for (int i = 0; i < f.getParametros().size(); i++) {
                result = this.argumentos.get(i).interpretar(tabla, tree);
                if (result instanceof Excepcion) {
                    return result;
                }
                Declaracion dec = (Declaracion) f.getParametros().get(i);
                if (!this.argumentos.get(i).getTipo().equals(dec.getTipo())) {
                    Excepcion ex = new Excepcion("Semántico", "Los tipos de los parámetros no coinciden", fila, columna);
                    tree.getExcepciones().add(ex);
                    return ex;
                }
                Simbolo simbolo = new Simbolo(dec.getTipo(), dec.getIdentificador(), result);
                result = t.setVariable(simbolo);
                if (result != null) {
                    Excepcion ex = new Excepcion("Semántico", result.toString(), fila, columna);
                    tree.getExcepciones().add(ex);
                    return ex;
                }
            }
            return f.interpretar(t, tree);
        } else {
            Excepcion ex = new Excepcion("Semántico", "La cantidad de argumentos "
                    + "enviados no coincide con la cantidad de parámetros "
                    + "de la función " + this.nombre, fila, columna);
            tree.getExcepciones().add(ex);
            return ex;
        }
    }
}
