package instrucciones;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

public class While extends AST {

    private AST condicion;
    private ArrayList<AST> instrucciones;

    public While(AST condicion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.condicion = condicion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Tabla t = new Tabla(tabla);
        Object result = condicion.ejecutar(t, arbol);
        if (result instanceof Excepcion) {
            return result;
        }

        if (!(result instanceof Vector || result instanceof Lista
            || result instanceof Matriz ||result instanceof Arreglo)) {
            return new Excepcion("Semántico", "La condición de While debe " +
                    "ser una estructura tipo Vector, Lista, Matriz o Arreglo.", condicion.fila, condicion.columna);
        }

        /**
         * Verifico que la condición sea de tipo boolean
         */
        if (!(condicion.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN))) {
            return new Excepcion("Semántico", "La condición de While debe " +
                    "ser de tipo Boolean.", condicion.fila, condicion.columna);
        }

        while ((Boolean)((LinkedList)condicion.ejecutar(tabla,arbol)).getFirst()) {
            for (AST instruccion : instrucciones) {
                result = instruccion.ejecutar(t, arbol);
                if (result instanceof Return || result instanceof Excepcion) {
                    return result;
                }
                if(result instanceof Break){
                    return null;
                }
                if(result instanceof Continue){
                    break;
                }
            }

        }
        return null;
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        for (AST instruccion : instrucciones) {
            int random = getRandomInRange(1, 10000);
            dotBuilder.append(padre+"->"+instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

            instruccion.crearDotFile(dotBuilder, instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

        }

        return dotBuilder.toString();
    }
}
