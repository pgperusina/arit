package nativas;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;

import java.util.ArrayList;

import static utilities.Constantes.TYPEOF_PARAMETRO;

public class TypeOf extends Funcion {

    public TypeOf(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(TYPEOF_PARAMETRO);
        if (simbolo == null) {
            Excepcion ex = new Excepcion("Semántico", "El argumento enviado a la funcion TypeOf no es válido. " + this.nombre + ".", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }

        return (simbolo.getTipo().toString()).toUpperCase();
    }
}
