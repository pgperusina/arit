package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;

import java.util.ArrayList;
import java.util.LinkedList;

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
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object valorExpresion;

        Tabla t = new Tabla(tabla);
        valorExpresion = expresion.ejecutar(t, arbol);

        if (valorExpresion instanceof Excepcion) {
            return valorExpresion;
        }

        Object result = tabla.getVariable(this.identificador);

        if (result == null) {
            t.setVariable(new Simbolo(expresion.getTipo(), this.identificador, new LinkedList<>()));
        }

        LinkedList linkedList = (LinkedList) valorExpresion;
        for (int i = 0; i < linkedList.size(); i++) {
            Object o = linkedList.get(i);
            result = instrucciones.get(i).ejecutar(t, arbol);
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

        return null;
    }
}
