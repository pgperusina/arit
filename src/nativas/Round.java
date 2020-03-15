package nativas;

import abstracto.AST;
import expresiones.Funcion;

import java.util.ArrayList;

public class Round extends Funcion {

    public Round(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }
}
