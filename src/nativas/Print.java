package nativas;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
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
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(PRINT_PARAMETRO);
        if (simbolo == null) {
            return new Excepcion("Semántico", "El argumento enviado a la " +
                    "función Print no es válido."
                    , this.fila, this.columna);

        }
//        //Todo validar que solo venga una expresion
//        if (!simbolo.getTipo().equals(new Tipo(Tipo.TipoDato.STRING))) {
//            Excepcion ex = new Excepcion("Semántico", "El tipo de los parametros no coinciden.", fila, columna);
//            tree.getExcepciones().add(ex);
//            return ex;
//        }
        if (simbolo.getValor() instanceof Arreglo) {
            Arreglo m = (Arreglo) simbolo.getValor();
            if (m.size() == 0) {
                System.out.println("Arreglo vacío");
                printLineaConsola(arbol, "Arreglo vacío");
            }
            if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.OBJETO)) {
                printLineaConsola(arbol,
                        simbolo.getTipo().getTipoEstructura().toString() +"=>LISTA");
                System.out.println("" + simbolo.getTipo().getTipoEstructura() + "=>" + "LISTA");
            } else {
                printLineaConsola(arbol, simbolo.getTipo().toString() + "\n");
                System.out.println(""+simbolo.getTipo()+"");
            }
            printLineaConsola(arbol, m.toString() + "\n");
            System.out.println(m.toString());
            return null;
        }
        if (simbolo.getValor() instanceof Matriz) {
            Matriz m = (Matriz) simbolo.getValor();
            if (m.size() == 0) {
                System.out.println("Matriz vacía");
            }
            printLineaConsola(arbol, simbolo.getTipo().toString());
            printLineaConsola(arbol, m.toString() + "\n");
            System.out.println(""+simbolo.getTipo()+"");
            System.out.println(m.toString());
            return null;
        }
        if (simbolo.getValor() instanceof Vector) {
            LinkedList l = (LinkedList)simbolo.getValor();
            if (l.size() == 0) {
                printLineaConsola(arbol, simbolo.getTipo().toString());
                printLineaConsola(arbol, "[] \n");
                System.out.println(""+simbolo.getTipo()+"");
                System.out.println("[]");
            }
            printLineaConsola(arbol, simbolo.getTipo().toString() + "\n");
            printLineaConsola(arbol, l.toString() + "\n");

            System.out.println(""+simbolo.getTipo()+"");
            System.out.println(l.toString());
        }
        if (simbolo.getValor() instanceof Lista) {
            LinkedList l = (LinkedList)simbolo.getValor();
            printLineaConsola(arbol, simbolo.getTipo().toString() + "\n");
            printLineaConsola(arbol, l.toString() + "\n");
            System.out.println(""+simbolo.getTipo()+"");
            System.out.println(l.toString());
        }
        return null;
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
                result = argumento.ejecutar(tabla, arbol);
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

    public void printLineaConsola(Arbol arbol, String s) {
//        arbol.getConsola().setText(arbol.getConsola().getText() + s + "\n");
    }

    public void printConsola(Arbol arbol, String s) {
//        arbol.getConsola().setText(arbol.getConsola().getText() + s );
    }
}
