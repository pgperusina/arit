package expresiones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class ArgumentoDefault extends AST {

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        return this;
    }
}
