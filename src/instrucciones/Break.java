package instrucciones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class Break extends AST {

    public Break(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return this;
    }
}
