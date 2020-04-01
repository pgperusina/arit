package expresiones;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

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
        Object result;

        result = this.condicion.ejecutar(tabla, arbol);
        if (result instanceof Excepcion) return result;

        if ( (this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
            || this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ))
                & this.condicion.getTipo().getTipoDato() == Tipo.TipoDato.BOOLEAN) {

            if ((Boolean)((LinkedList) condicion.ejecutar(tabla, arbol)).getFirst()) {
                result = operando1.ejecutar(tabla, arbol);
                this.tipo = operando1.getTipo();
            } else {
                result = operando2.ejecutar(tabla, arbol);
                this.tipo = operando2.getTipo();
            }

            return result;
        } else {
            return new Excepcion("Semántico", "La condición del operador ternario " +
                    "no es Matriz o Vector booleano.", this.condicion.fila, this.condicion.columna);
        }
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1,10000);
        dotBuilder.append(padre+"->"+this.condicion.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        this.condicion.crearDotFile(dotBuilder, this.condicion.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        random = getRandomInRange(1,10000);
        dotBuilder.append(padre+"->"+this.operando1.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        this.operando1.crearDotFile(dotBuilder, this.operando1.getClass().getSimpleName()+random);
        random = getRandomInRange(1,10000);
        dotBuilder.append(padre+"->"+this.operando2.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        this.operando2.crearDotFile(dotBuilder, this.operando2.getClass().getSimpleName()+random);
        dotBuilder.append("\n");

        return dotBuilder.toString();
    }
}
