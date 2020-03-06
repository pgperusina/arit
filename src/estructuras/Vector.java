package estructuras;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;

public class Vector extends AST {

    private ArrayList valor;

    public Vector(Tipo tipo, ArrayList valor, int fila, int columna) {
        this.valor = valor;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        return this.valor;
    }
}
