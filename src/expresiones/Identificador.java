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
    public Object ejecutar(Tabla tabla, Arbol tree) {
        Simbolo simbolo = tabla.getVariable(identificador);
        if (simbolo == null) {
            return new Excepcion("Semántico",
                    "No se ha encontrado la variable " + identificador + ".",
                    fila, columna);
        }
        this.tipo = simbolo.getTipo();
        return simbolo.getValor();
    }

    public String getIdentificador() {
        return this.identificador;
    }
}
