package expresiones;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import static utilities.Utils.getRandomInRange;

public class Identificador extends AST {
    private String identificador;

    public Identificador(String identificador, int fila, int columna){
        this.identificador = identificador;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(identificador);
        if (simbolo == null) {
            return new Excepcion("Semántico",
                    "No se ha encontrado la variable " + identificador + ".",
                    fila, columna);
        }
        this.tipo = simbolo.getTipo();
        return simbolo.getValor();
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        dotBuilder.append(padre+"->"+this.identificador.getClass().getSimpleName()+getRandomInRange(1,10000));
        return dotBuilder.toString();
    }

    public String getIdentificador() {
        return this.identificador;
    }
}
