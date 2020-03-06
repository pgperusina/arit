package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import instrucciones.Return;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class Funcion extends AST {
    protected String nombre;
    protected ArrayList<AST> parametros;
    private ArrayList<AST> instrucciones;

    public Funcion(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        this.nombre = nombre;
        this.parametros = parametros;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        for (AST instruccion : instrucciones) {
            Object result = instruccion.interpretar(tabla, tree);
            if (result instanceof Excepcion) {
                return result;
            }
            if (result instanceof Return) {
                Return r = (Return) result;
                if (r.getExpresion() == null) {
                    return null;
                }
                result = r.getExpresion().interpretar(tabla, tree);
                return result;
            }
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<AST> getParametros() {
        return parametros;
    }
}
