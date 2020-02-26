package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

public class Primitivo extends AST {
    private Object valor;

    public Primitivo(Tipo tipo, Object valor, int fila, int columna) {
        this.valor = valor;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        return this.valor;
    }
}
