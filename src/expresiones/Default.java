package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class Default extends AST {

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return this;
    }
}
