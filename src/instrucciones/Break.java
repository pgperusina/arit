package instrucciones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

public class Break extends AST {

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        return this;
    }
}
