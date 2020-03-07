package nativas;

import abstracto.AST;
import expresiones.Funcion;

import java.util.ArrayList;

public class Median extends Funcion {

    public Median(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, fila, columna);
    }
}
