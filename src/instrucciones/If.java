package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

public class If extends AST {

    private AST condicion;
    private ArrayList<AST> instruccionesIf;
    private ArrayList<AST> instruccionesElse;

    public If(AST condicion, ArrayList<AST> instruccionesIf, ArrayList<AST> instruccionesElse, int fila, int columna) {
        this.condicion = condicion;
        this.instruccionesIf = instruccionesIf;
        this.instruccionesElse = instruccionesElse;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object valorCondicion;

        Tabla childTable = new Tabla(tabla);
        valorCondicion = condicion.ejecutar(childTable, arbol);

        if (valorCondicion instanceof Excepcion) {
            return valorCondicion;
        }

        if (!( (this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                || this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ))
                & this.condicion.getTipo().getTipoDato() == Tipo.TipoDato.BOOLEAN) ) {
            return new Excepcion("Semántico", "La condición de la sentencia IF " +
                    "debe de ser un Vector o Matriz booleano.", fila, columna);
        }

        Object result;
        if ((Boolean) ((LinkedList)valorCondicion).getFirst()) {
            for (AST instruccionIf : instruccionesIf) {
                result = instruccionIf.ejecutar(childTable, arbol);
                if (result instanceof Return || result instanceof Excepcion
                        || result instanceof Break || result instanceof Continue) {
                    return result;
                }
            }
        } else {
            for (AST instruccionElse : instruccionesElse) {
                result = instruccionElse.ejecutar(childTable, arbol);
                if (result instanceof Return || result instanceof Excepcion
                        || result instanceof Break || result instanceof Continue) {
                    return result;
                }
            }
        }

        return null;
    }
}
