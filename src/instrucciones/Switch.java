package instrucciones;

import abstracto.AST;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class Switch extends AST {

    private AST condicion;
    private ArrayList<AST> listaCases;

    public Switch(AST condicion, ArrayList<AST> listaCases, int fila, int columna) {
        this.condicion = condicion;
        this.listaCases = listaCases;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        return this;
    }

}
