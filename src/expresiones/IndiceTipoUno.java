package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

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
    public Object interpretar(Tabla tabla, Arbol arbol) {

        return this.valor.interpretar(tabla, arbol);
    }
}
