package expresiones;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import instrucciones.Break;
import instrucciones.Continue;
import instrucciones.Return;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

public class Case extends AST {

    private AST expresion;
    private ArrayList<AST> instrucciones;

    public AST getExpresion() {
        return  this.expresion;
    }

    public Case(AST expresion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        for (AST instruccion : instrucciones) {
            Object result = instruccion.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            if (result instanceof Continue) {
                return new Excepcion("Semántico", "Sentencia 'continue' " +
                        "fuera de ciclo.", ((Continue) result).fila, ((Continue) result).columna);
            }
            if (result instanceof Return) {
                return new Excepcion("Semántico", "Sentencia 'return' " +
                        "fuera de función.", ((Return) result).fila, ((Return) result).columna);
            }
            if (result instanceof Break) {
                return result;
            }
        }

        return null;
    }
}
