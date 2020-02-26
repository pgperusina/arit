package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class Retorno extends AST{
    private AST expresion;

    public Retorno(AST expresion, int fila, int columna){
        this.expresion = expresion;
        this.fila = fila;
        this.columna = columna;
    }

    public Retorno(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        //Object result = expresion.interpretar(tabla, tree);
        return this;
    }

    public AST getExpresion() {
        return expresion;
    }

}
