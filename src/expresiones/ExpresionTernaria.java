package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

public class ExpresionTernaria extends AST {

    private AST condicion;
    private AST operando1;
    private AST operando2;

    public ExpresionTernaria(AST condicion, AST operando1, AST operando2, int fila, int columna) {
        this.condicion = condicion;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol tree) {
        Object condicion;

        condicion = this.condicion.ejecutar(tabla, tree);
        if (condicion instanceof Excepcion) return condicion;

        this.tipo = new Tipo(Tipo.TipoDato.BOOLEAN);

        if (this.condicion.tipo.getTipoDato() == Tipo.TipoDato.BOOLEAN) {
            return (boolean) operando1.ejecutar(tabla, tree) ? operando1.ejecutar(tabla, tree) : operando2.ejecutar(tabla, tree);
        } else {
            Excepcion ex = new Excepcion("Semántico", "La condición del operador ternario no es de tipo Boolean.", fila, columna);
            tree.getExcepciones().add(ex);
            return ex;
        }
    }
}
