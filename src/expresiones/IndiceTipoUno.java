package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

/**
 * Indice para acceder a listas, el cual devolverá una nueva lista
 * con el contenido de la posición definida dentro de la lista.
 */
public class IndiceTipoUno extends AST {

    private AST valor;

    public IndiceTipoUno(AST valor, int fila, int columna) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {

        return this.valor.ejecutar(tabla, arbol);
    }
}
