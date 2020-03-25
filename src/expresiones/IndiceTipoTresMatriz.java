package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class IndiceTipoTresMatriz extends AST {
    private AST valor;

    public IndiceTipoTresMatriz(AST valor, int fila, int columna) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {

        return this.valor.ejecutar(tabla, arbol);
    }
}
