package instrucciones;

import abstracto.AST;

public class Acceso extends AST {

    private String identificador;
    private AST valor;
    private Object valorExplicito;

    public Acceso(String identificador, AST valor, int fila, int columna) {
        this.identificador = identificador;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

}
