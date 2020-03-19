package expresiones;

import abstracto.AST;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import nativas.List;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class AccesoEstructura extends AST {

    private String identificador;
    private LinkedList<AST> posiciones;

    public AccesoEstructura(String identificador, LinkedList<AST> posiciones, int fila, int columna) {
        this.identificador = identificador;
        this.posiciones = posiciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(this.identificador);
        if (simbolo == null) {
            return new Excepcion("Semántico", "Se está tratando acceder a una posición de una variable " +
                    "que no ha sido definida aún. '" + this.identificador + "'.", fila, columna);
        }

        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.ARREGLO)) {
            //todo acceso a arreglo
        } else if (simbolo.getValor() instanceof Matriz) {
            /**
             * Valido que las busquedas sean solo las aceptadas para
             * las matrices
             */
            if (posiciones.size() > 1) {
                return new Excepcion("Semántico", "El acceso a matrices " +
                        "no permite mas de dos índices.", posiciones.get(1).fila, posiciones.get(1).columna);
            }

            /**
             * Depende del tipo de acceso, accedo a la posición solicitada
             */
            AST indice = posiciones.get(0);
            LinkedList valor = (Matriz) simbolo.getValor();
            int filas = ((Matriz) simbolo.getValor()).getFilas();
            int columnas = ((Matriz) simbolo.getValor()).getColumnas();
            if (indice instanceof IndiceTipoUnoMatriz) {
                Object result =  indice.interpretar(tabla, arbol);
                if (result instanceof Excepcion) {
                    return result;
                }

                LinkedList indices = (LinkedList) result;
                Vector x = (Vector) indices.getFirst();
                Vector y = (Vector) indices.get(1);

                if (!(x.getFirst() instanceof Integer &
                        y.getFirst() instanceof Integer)){
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "Los índices de un acceso a matriz tipo 1, deben de ser de tipo entero.", indice.fila, indice.columna);
                }

                int posicionBuscada = (((Integer)y.getFirst() - 1) *filas)
                        + (Integer)x.getFirst();
                valor = new Vector(Arrays.asList(valor.get(posicionBuscada-1)));
            }
            /**
             * Defino el tipo que devolvemos
             * y retornamos valor encontrado
             */
            this.tipo = new Tipo(simbolo.getTipo().getTipoDato(), Tipo.TipoEstructura.VECTOR);
            return valor;
        } else if (simbolo.getValor() instanceof Lista) {
            this.tipo = simbolo.getTipo();
            LinkedList valor = (LinkedList) simbolo.getValor();
            for (AST posicion : posiciones) {
                Object resultPosicion = posicion.interpretar(tabla, arbol);
                if (resultPosicion instanceof Excepcion) {
                    return resultPosicion;
                }

                /**
                 * Verifico que el índice sea instancia de Vector
                 */
                if (!(resultPosicion instanceof Vector)) {
                    return new Excepcion("Semántico", "Error accediendo a lista. " +
                            "El índice debe ser un Vector de tipo entero", posicion.fila, posicion.columna);
                }
                /**
                 * Verifico si el vector es de una sola posición
                 */
                if (((Vector) resultPosicion).size() > 1) {
                    return new Excepcion("Semántico", "El tamaño del índice de posición " +
                            "debe ser de 1, el índice colocado es un Vector de "
                            + ((Vector) resultPosicion).size() + " posiciones.", posicion.fila, posicion.columna);

                }
                /**
                 * Verifico si el índice es un vector de tipo Entero
                 */
                Vector r = (Vector)resultPosicion;
                if (!(r.get(0) instanceof Integer)) {
                    return new Excepcion("Semántico", "El acceso a la posición de un vector " +
                            "debe ser con índices de tipo INTEGER.", posicion.fila, posicion.columna);
                }

                /**
                 * Verifico si el índice es mayor al tamaño de la lista.
                 * Si es así, devuelvo una excepción.
                 */
                if ((Integer)r.getFirst() > (Integer)valor.size() |
                        (Integer)r.getFirst() < 1) {
                    return new Excepcion("Semántico", "Está tratando de acceder a una posición fuera " +
                            "de rango en la lista '" + simbolo.getIdentificador() + "'.", posicion.fila, posicion.columna);
                }
                /**
                 * Verifico si cada posicion es tipo 1 o 2, dependiendo de eso,
                 * traer esa posición de la lista
                 */
                if (posicion instanceof IndiceTipoUno) {   /** Devuelve una lista **/
                    /**
                     * Devolver el valor de la lista en índice indicado envuelto en una lista
                     */
                    valor = new Lista(Arrays.asList(valor.get((Integer)r.getFirst()-1)));
                }

                if (posicion instanceof IndiceTipoDos) {   /** Devuelve el valor crudo de la posición **/
                    /**
                     * Devolver el valor crudo de la lista en el índice indicado
                     */
                    if (!(valor.get((Integer)r.getFirst()-1) instanceof Vector |
                            valor.get((Integer)r.getFirst()-1) instanceof Lista)) {
                        valor = new Vector(Arrays.asList(valor.get((Integer)r.getFirst()-1)));
                    } else {
                        valor = (LinkedList) valor.get((Integer)r.getFirst()-1);
                    }
                    /**
                     * Cambiar el tipo del símbolo actual al tipo correspondiente al valor crudo
                     */
                    if (valor instanceof Vector) {
                        this.tipo = new Tipo(valor.getFirst().getClass().getSimpleName().toLowerCase(),
                                Tipo.TipoEstructura.VECTOR);
                    }
                }
            }
            return valor;
        } else {
            /**
             * ACCESO A VECTORES
             * Página 28 del enunciado
             * El índice de acceso debe ser un valor entre 1 y la cantidad de elementos del vector
             * Si el índice está fuera del rango se debe indicar un error.
             */
            //TODO - validar que el valor de posición sea mayor a cero
            this.tipo = simbolo.getTipo();
            LinkedList valor = (LinkedList) simbolo.getValor();
            for (AST posicion : posiciones) {
                if (!(posicion instanceof IndiceTipoUno)) {
                    return new Excepcion("Semántico", "Está tratando de acceder a un vector " +
                            "usando un acceso 'tipo dos' ([[]]),  el cual solo puede usarse en listas, " +
                            "debe usar el acceso 'tipo uno' ([]).", fila, columna);
                }

                Object resultPosicion = posicion.interpretar(tabla, arbol);
                if (resultPosicion instanceof Excepcion) {
                    return resultPosicion;
                }

                if (!(resultPosicion instanceof Vector)) {
                    return new Excepcion("Semántico", "Error accediendo a vector.", posicion.fila, posicion.columna);
                }
                Vector r = (Vector)resultPosicion;
                if (!(r.get(0) instanceof Integer)) {
                    return new Excepcion("Semántico", "El acceso a la posición de un vector " +
                            "debe ser con índices de tipo INTEGER.", posicion.fila, posicion.columna);
                }
                if ((Integer)r.getFirst() > (Integer)valor.size() |
                        (Integer)r.getFirst() < 1) {
                     return new Excepcion("Semántico", "Está tratando de acceder a una posición fuera " +
                            "de rango en el VECTOR '" + simbolo.getIdentificador() + "'.", posicion.fila, posicion.columna);
//
                }
                valor = new Vector(Arrays.asList(valor.get((Integer)r.getFirst()-1)));
            }
            return valor;
        }
        return null;
    }
}
