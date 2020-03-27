package abstracto;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

public class AST {
    public Tipo tipo;
    public int fila;
    public int columna;

    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return null;
    }

    public Tipo getTipo() {
        return tipo;
    }
}
