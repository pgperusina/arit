package instrucciones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class Continue extends AST {

    public Continue(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return this;
    }
}
