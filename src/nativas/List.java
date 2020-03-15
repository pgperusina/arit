package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;

import java.util.ArrayList;

import static utilities.Constantes.C_PARAMETRO;

public class List extends Funcion {

    public List(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Tabla t = new Tabla(tabla);
        Object result;
        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.interpretar(t, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            if (!(argumento instanceof Vector)) {
                return new Excepcion("Semántico","La función 'C' solamente acepta" +
                        " argumentos de tipo VECTOR o PRIMITIVOS.",
                        argumento.fila, argumento.columna);
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), C_PARAMETRO + count++, result);
            result = t.setVariable(simbolo);

            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
