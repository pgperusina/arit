package expresiones;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

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
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object condicion;

        condicion = this.condicion.ejecutar(tabla, arbol);
        if (condicion instanceof Excepcion) return condicion;

        this.tipo = new Tipo(Tipo.TipoDato.BOOLEAN);

        if ( (this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
            || this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ))
                & this.condicion.getTipo().getTipoDato() == Tipo.TipoDato.BOOLEAN) {
            return (Boolean)((LinkedList) condicion).getFirst()
                    ? operando1.ejecutar(tabla, arbol)
                    : operando2.ejecutar(tabla, arbol);
        } else {
            return new Excepcion("Semántico", "La condición del operador ternario " +
                    "no es Matriz o Vector booleano.", fila, columna);
        }
    }
}
