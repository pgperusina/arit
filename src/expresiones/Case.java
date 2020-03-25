package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class Case extends AST {

    private AST expresion;
    private ArrayList<AST> instrucciones;

    public Case(AST expresion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return this;
    }
}
