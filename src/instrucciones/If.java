package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;

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

        if (!(valorCondicion instanceof Boolean)) {
            Excepcion ex = new Excepcion("Semántico", "La condición de la sentencia IF debe de ser de tipo Boolean", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }

        Object result;
        if ((Boolean) valorCondicion) {
            for (AST m : instruccionesIf) {
                result = m.ejecutar(childTable, arbol);
                if (result instanceof Return || result instanceof Excepcion
                        || result instanceof Break || result instanceof Continue) {
                    return result;
                }
            }
        } else {
            for (AST m : instruccionesElse) {
                result = m.ejecutar(childTable, arbol);
                if (result instanceof Return || result instanceof Excepcion
                        || result instanceof Break || result instanceof Continue) {
                    return result;
                }
            }
        }

        return null;
    }
}
