package nativas;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;

import static commons.Constantes.*;

public class Print extends Funcion {

    public Print(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, fila, columna);
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        Simbolo simbolo = tabla.getVariable(PRINT_PARAMETRO);
        if (simbolo == null) {
            Excepcion ex = new Excepcion("Semántico", "El argumento enviado a la función Print no es válido. " + this.nombre + ".", fila, columna);
            tree.getExcepciones().add(ex);
            return ex;
        }
        //Todo validar que solo venga una expresion
        if (!simbolo.getTipo().equals(new Tipo(Tipo.TipoDato.STRING))) {
            Excepcion ex = new Excepcion("Semántico", "El tipo de los parametros no coinciden.", fila, columna);
            tree.getExcepciones().add(ex);
            return ex;
        }
        return (simbolo.getValor() + "").toUpperCase();
    }
}
