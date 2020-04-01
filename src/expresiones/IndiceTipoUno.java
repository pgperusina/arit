package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import static utilities.Utils.getRandomInRange;

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

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1, 10000);

        dotBuilder.append(padre+"->"+this.valor.getClass().getSimpleName()+random);
        dotBuilder.append("\n");

        return dotBuilder.toString();
    }
}
