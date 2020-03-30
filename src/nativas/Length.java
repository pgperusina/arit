package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static utilities.Constantes.LENGTH_PARAMETRO;
import static utilities.Constantes.TYPEOF_PARAMETRO;

public class Length extends Funcion {
    public Length(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(LENGTH_PARAMETRO);
        if (simbolo == null) {
            return new Excepcion("Semántico", "No se ha enviado argumento a la función " +
                    this.nombre.toUpperCase() + ".", fila, columna);
        }
        int length = ((LinkedList)simbolo.getValor()).size();

        return new Simbolo(new Tipo(Tipo.TipoDato.INTEGER, Tipo.TipoEstructura.VECTOR)
                , simbolo.getIdentificador(), new Vector(Arrays.asList(length)));
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre.toUpperCase() +"' debe recibir " +
                    " 1 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre.toUpperCase() + "' debe recibir " +
                    " 1 argumentos.",
                    this.fila, this.columna);
        }

        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), LENGTH_PARAMETRO, result);
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
