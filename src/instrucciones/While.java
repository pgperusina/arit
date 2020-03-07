package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class While extends AST {

    private AST condicion;
    private ArrayList<AST> instrucciones;

    public While(AST condicion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.condicion = condicion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        Object valorCondicion;
        do {
            Tabla childTable = new Tabla(tabla);
            valorCondicion = this.condicion.interpretar(childTable, arbol);

            if (valorCondicion instanceof Excepcion) {
                return valorCondicion;
            }

            if (!(valorCondicion instanceof Boolean)) {
                Excepcion ex = new Excepcion("Semántico",
                        "La condición del ciclo WHILE espera un valor Booleano",
                        fila, columna);
                arbol.getExcepciones().add(ex);
                return ex;
            }

            Object result;
            if ((Boolean) valorCondicion) {
                for (AST instruccion : instrucciones) {
                    result = instruccion.interpretar(childTable, arbol);
                    if (result instanceof Return || result instanceof Excepcion) {
                        return result;
                    }
                    if(result instanceof Break){
                        return null;
                    }
                    if(result instanceof Continue){
                        break;
                    }
                }
            }

        } while ((Boolean) valorCondicion);

        return null;
    }
}
