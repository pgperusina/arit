package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

/**
 * Indice para acceder a listas, el cual devolverá el valor crudo de la posición definida
 * ya sea un vector o una lista, al contrario del indice tipo uno que devuelve el valor envuelto
 * en una lista
 */
public class IndiceTipoDos extends AST {

    private AST valor;

    public IndiceTipoDos(AST valor, int fila, int columna) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        return this.valor.interpretar(tabla, arbol);
    }
}
