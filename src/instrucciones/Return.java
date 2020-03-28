package instrucciones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class Return extends AST{
    private AST expresion;

    public Return(AST expresion, int fila, int columna){
        this.expresion = expresion;
        this.fila = fila;
        this.columna = columna;
    }

    public Return(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
//        Object result = expresion.interpretar(tabla, tree);
        if (!(expresion == null)) {
            this.tipo = expresion.getTipo();
        }
        return this;
    }

    public AST getExpresion() {
        return expresion;
    }

}
