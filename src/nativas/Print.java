package nativas;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Constantes.*;

public class Print extends Funcion {

    public Print(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(PRINT_PARAMETRO);
        if (simbolo == null) {
            Excepcion ex = new Excepcion("Semántico", "El argumento enviado a la " +
                    "función Print no es válido."
                    , simbolo.getFila(), simbolo.getColumna());
            arbol.getExcepciones().add(ex);
            return ex;
        }
//        //Todo validar que solo venga una expresion
//        if (!simbolo.getTipo().equals(new Tipo(Tipo.TipoDato.STRING))) {
//            Excepcion ex = new Excepcion("Semántico", "El tipo de los parametros no coinciden.", fila, columna);
//            tree.getExcepciones().add(ex);
//            return ex;
//        }
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)) {
            LinkedList l = (LinkedList)simbolo.getValor();
            if (l.size() == 0) {
                System.out.println(""+simbolo.getTipo()+"");
                System.out.println("[]");
            }
            System.out.println(""+simbolo.getTipo()+"");
            System.out.println(l.toString());
        }
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
            LinkedList l = (LinkedList)simbolo.getValor();
            System.out.println("--"+simbolo.getTipo().toString()+"--");
            System.out.println(l.toString());
        }
        return null;
//        return (simbolo.getValor() + "").toUpperCase();
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 1) {
            return new Excepcion("Semántico", "La función 'Print' solamente acepta" +
                    " 1 argumento",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        } else {
            for (AST argumento : argumentos) {
                result = argumento.interpretar(tabla, arbol);
                if (result instanceof Excepcion) {
                    return result;
                }

                Simbolo simbolo = new Simbolo(argumento.getTipo(), PRINT_PARAMETRO, result);
                result = tabla.setVariable(simbolo);

                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
