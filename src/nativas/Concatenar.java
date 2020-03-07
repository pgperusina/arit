package nativas;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;

import static commons.Constantes.C_PARAMETRO;

public class Concatenar extends Funcion {

    public Concatenar(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, fila, columna);
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        int count = 1;
        Simbolo simbolo;
        while(tabla.getVariable(C_PARAMETRO + count) != null) {
            simbolo = tabla.getVariable(C_PARAMETRO + count);

            //TODO
            //validar tipos y castear!!
            if (!simbolo.getTipo().equals(new Tipo(Tipo.TipoDato.STRING))) {
                Excepcion ex = new Excepcion("Semántico", "El tipo de los parametros no coinciden.", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }

            count++;
        }


        return  null;//(simbolo.getValor() + "").toUpperCase();
    }
}
