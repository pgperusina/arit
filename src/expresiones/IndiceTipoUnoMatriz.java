package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.LinkedList;

public class IndiceTipoUnoMatriz extends AST {
    private AST valorFila;
    private AST valorColumna;

    public IndiceTipoUnoMatriz(AST valorFila, AST valorColumna, int fila, int columna) {
        this.valorFila = valorFila;
        this.valorColumna = valorColumna;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        LinkedList valores = new LinkedList();
        valores.add(valorFila.interpretar(tabla, arbol));
        valores.add(valorColumna.interpretar(tabla, arbol));
        return valores;
    }
}
