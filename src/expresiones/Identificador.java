package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;

public class Identificador extends AST {
    private String identificador;

    public Identificador(String identificador, int fila, int columna){
        this.identificador = identificador;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        Simbolo simbolo = tabla.getVariable(identificador);
        if (simbolo == null) {
            Excepcion ex = new Excepcion("Semántico",
                    "No se ha encontrado la variable " + identificador + ".",
                    fila, columna);
            tree.getExcepciones().add(ex);
            return ex;
        }
        this.tipo = simbolo.getTipo();
        return simbolo.getValor();
    }
}
