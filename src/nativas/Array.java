package nativas;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
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

import static utilities.Constantes.*;
import static utilities.Utils.getRandomInRange;

public class Array extends Funcion {
    public Array(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        int count = 1;
        LinkedList<Simbolo> parametros = new LinkedList<>();
        while(tabla.getVariableLocal(ARRAY_PARAMETRO + count) != null) {
            parametros.add(tabla.getVariableLocal(ARRAY_PARAMETRO + count));
            count++;
        }

        Simbolo simboloData = parametros.get(0);
        Simbolo simboloDimensiones = parametros.get(1);

        /**
         * Los valores del array pueden ser vectores o listas
         * por eso uso una LinkedList
         */
        LinkedList valorData = (LinkedList)simboloData.getValor();
        Vector dimensiones = (Vector)simboloDimensiones.getValor();

        /**
         * Creo una nueva estructura de tipo array con los datos de 'data' y con
         * la información de sus dimensiones
         */
        Arreglo result = new Arreglo();
        int posicionData = 0;
        int cantidadPosicionesArreglo = 1;

        /**
         * Calculo la cantidad de posiciones que tendrá el arreglo.
         */
        for (Object dimension : dimensiones) {
            cantidadPosicionesArreglo *= (Integer)dimension;
        }

        for (int i = 0; i < cantidadPosicionesArreglo; i++) {
            /**
             * Si la posicion actual es menor o igual al tamaño del
             * vector de data, sigo jalando datos de data, cuando
             * llegue al tamaño, reinicio posicionData a 0
             */
            if (posicionData <= valorData.size()-1) {
                if (valorData.get(posicionData) instanceof Integer ||
                        valorData.get(posicionData) instanceof String ||
                        valorData.get(posicionData) instanceof Double ||
                        valorData.get(posicionData) instanceof Boolean) {
                    result.add(new Vector(Arrays.asList(valorData.get(posicionData++))));
                } else {
                    result.add(valorData.get(posicionData++));
                }
            } else {
                posicionData = 0;
                if (valorData.get(posicionData) instanceof Integer ||
                        valorData.get(posicionData) instanceof String ||
                        valorData.get(posicionData) instanceof Double ||
                        valorData.get(posicionData) instanceof Boolean) {
                    result.add(new Vector(Arrays.asList(valorData.get(posicionData++))));
                } else {
                    result.add(valorData.get(posicionData++));
                }
            }
        }

        result.setDimensiones(dimensiones);
        return new Simbolo(new Tipo(parametros.get(0).getTipo().getTipoDato(),
                Tipo.TipoEstructura.ARREGLO), this.getNombre(), result);

//        if (((Integer)valorData.getFirst() %
//                ((Integer)valorNrow.getFirst() * (Integer)valorNcol.getFirst())) != 0) {
//            return new Excepcion("Semántico","La cantidad de elementos a insertar en la " +
//                    " matriz debe de ser múltiplo o submúltiplo de la cantidad de posiciones de la matriz.",
//                    simboloData.getFila(), simboloData.getColumna());
//        }
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        int count = 1;
        if (!(argumentos.size() == 2)) {
            return new Excepcion("Semántico","La función 'Array' debe recibir " +
                    " 2 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        /**
         * Verifico que el primer argumento sea un vector o una lista
         * de lo contrario retorno una excepción
         */
        result = argumentos.get(0).ejecutar(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        if (!(result instanceof estructuras.Vector || result instanceof Lista)) {
            return new Excepcion("Semántico","El primer argumento de la función 'Array' " +
                    " debe de ser un VECTOR o una LISTA.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }

        /**
         * Verifico que el seguno argumento sea un vector
         * de lo contrario retorno una excepción
         */
        result = argumentos.get(1).ejecutar(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        if (!(result instanceof Vector)) {
            return new Excepcion("Semántico","El segundo argumento de la función 'ARRAY' " +
                    " debe de ser un VECTOR.",
                    argumentos.get(1).fila, argumentos.get(1).columna);
        }

        /**
         * Verifico que cada dimensión sea de tipo entero
         */
        for (Object d : (Vector)result) {
            if (!(d instanceof Integer)) {
                return new Excepcion("Semántico", "Las dimensiones definidas para el arreglo " +
                        " deben de ser de tipo entero.",
                        argumentos.get(1).fila, argumentos.get(1).columna);
            }
        }

        /**
         * Cargo los argumentos a la tabla de símbolos
         */
        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), ARRAY_PARAMETRO + count++, result);
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
