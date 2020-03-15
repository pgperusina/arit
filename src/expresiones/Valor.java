package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

public class Valor extends AST {

    private Object valor;

    public Valor(Tipo tipo, LinkedList valor, int fila, int columna) {
        this.valor = valor;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        return this.valor;
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
