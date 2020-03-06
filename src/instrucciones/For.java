package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class For extends AST {

    private String identificador;
    private AST expresion;
    private ArrayList<AST> instrucciones;

    public For(String identificador, AST expresion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.identificador = identificador;
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        Object valorExpresion;

        Tabla childTable = new Tabla(tabla);
        valorExpresion = expresion.interpretar(childTable, tree);

        if (valorExpresion instanceof Excepcion) {
            return valorExpresion;
        }

        Object result;

        if ((Boolean) valorExpresion) {
            for (AST i : instrucciones) {
                result = i.interpretar(childTable, tree);
                if (result instanceof Excepcion) {
                    return result;
                }
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
        return null;
    }
}
