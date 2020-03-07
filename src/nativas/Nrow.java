package nativas;

import abstracto.AST;
import expresiones.Funcion;

import java.util.ArrayList;

public class Nrow extends Funcion {

    public Nrow(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, fila, columna);
    }
}
