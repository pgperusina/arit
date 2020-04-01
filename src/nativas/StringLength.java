package nativas;

import abstracto.AST;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.Arrays;

import static utilities.Constantes.NCOL_PARAMETRO;
import static utilities.Constantes.STRING_LENGTH;

public class StringLength extends Funcion {

    public StringLength(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {

        Simbolo simbolo = tabla.getVariableLocal(STRING_LENGTH);
        if (simbolo == null) {
            return new Excepcion("Semántico", "No se han enviado argumentos a la función " +
                    this.nombre + ".", fila, columna);
        }
        if (!(simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
            return new Excepcion("Semántico", "El argumento enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Vector o Primitivo. Se envió '"
                    + simbolo.getTipo().toString()+ "'.", simbolo.getFila(), simbolo.getColumna());
        }
        if (!(simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Cadena.", simbolo.getFila(), simbolo.getColumna());
        }
        if ( ((Vector)simbolo.getValor()).size() > 1) {
            return new Excepcion("Semántico", "El Vector de tipo cadena enviado a la función " +
                    this.nombre.toUpperCase() + " debe tener una sola posición.", simbolo.getFila(), simbolo.getColumna());
        }
        int stringLength = ((Vector)simbolo.getValor()).getFirst().toString().length();

        return new Simbolo(new Tipo(Tipo.TipoDato.INTEGER, Tipo.TipoEstructura.VECTOR)
                , simbolo.getIdentificador(), new Vector(Arrays.asList(stringLength)));
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre +"' debe recibir " +
                    " 1 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 1 argumentos.",
                    this.fila, this.columna);
        }

        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), STRING_LENGTH, result);
            simbolo.setFila(argumento.fila);
            simbolo.setColumna(argumento.columna);
            result = tabla.setVariableLocal(simbolo);

            /**
             * Si el retorno de setVariable no es nulo
             * devuelvo result ya que es una excepción.
             */
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
