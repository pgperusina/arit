package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

public class Valor extends AST {

    /**
     * AST graph variables
     */
    int valorPadre = 1;
    int valorHijo = 1;

    private Object valor;

    public Valor(Tipo tipo, LinkedList valor, int fila, int columna) {
        this.valor = valor;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return this.valor;
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        dotBuilder.append(padre+"->"+valor.getClass().getSimpleName()+getRandomInRange(1,10000));
        dotBuilder.append("\n");
        return dotBuilder.toString();
    }

//    private StringBuilder getValuesToString() {
//        StringBuilder sb = new StringBuilder("[");
//        this.valor.forEach(v -> {
//            sb.append("\"").append(v).append("\"");
//        });
//        sb.append("]");
//        return sb;
//    }

//    @Override
//    public String toString() {
//        return "" + this.valor.
//    }
}
