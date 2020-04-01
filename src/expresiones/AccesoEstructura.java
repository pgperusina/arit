package expresiones;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.Arrays;
import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

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
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariable(this.identificador);
        if (simbolo == null) {
            return new Excepcion("Semántico", "Se está tratando acceder a una posición de una variable " +
                    "que no ha sido definida aún. '" + this.identificador + "'.", fila, columna);
        }

        if (simbolo.getValor() instanceof Arreglo) {

            Arreglo arreglo = (Arreglo) simbolo.getValor();
            int filasArreglo = (Integer) arreglo.getDimensiones().getFirst();
            int columnasArreglo = (Integer) arreglo.getDimensiones().get(1);
            /**
             * Valido que se esté accediendo a todas las dimensiones del arreglo
             */
            if (posiciones.size() != arreglo.getDimensiones().size()) {
                return new Excepcion("Semántico", "Para acceder a un arreglo debe de definir " +
                        "un valor para cada dimensión del arreglo.",
                        posiciones.get(1).fila, posiciones.get(1).columna);
            }
            /**
             * Valido que cada índice sea solo de tipo 1 ([]),
             * que esté dentro del rango de cada dimensión del arreglo
             * y que sean de tipo entero
             */
            int indiceValor = 1;
            for (int i = 0; i < posiciones.size(); i++) {
                if (!(posiciones.get(i) instanceof IndiceTipoUno)) {
                    return new Excepcion("Semántico", "El acceso a arreglos solo acepta el uso " +
                            "de índices de tipo 1 ([]).",
                            posiciones.get(i).fila, posiciones.get(i).columna);
                }
                Object resultPosicion = posiciones.get(i).ejecutar(tabla, arbol);
                if (!(resultPosicion instanceof Vector)) {
                    return new Excepcion("Semántico", "Los índices del acceso a arreglos " +
                            "deben de ser de tipo Vector.",
                            posiciones.get(i).fila, posiciones.get(i).columna);
                }
                if (((Vector) resultPosicion).size() > 1) {
                    return new Excepcion("Semántico", "Los índices del acceso a arreglos " +
                            "deben de ser de una sola posición.",
                            posiciones.get(i).fila, posiciones.get(i).columna);
                }
                if (!(((Vector) resultPosicion).getFirst() instanceof Integer)) {
                    return new Excepcion("Semántico", "Los índices del acceso a arreglos " +
                            "deben de ser de tipo entero.",
                            posiciones.get(i).fila, posiciones.get(i).columna);
                }
                Integer posicionInt = (Integer) ((Vector) resultPosicion).getFirst();
                if(posicionInt > (Integer)arreglo.getDimensiones().get(i) ||
                        posicionInt < 1) {
                    return new Excepcion("Semántico", "Los índices del acceso a arreglos " +
                            "deben de estar dentro del rango de posiciones de cada dimensión.",
                            posiciones.get(i).fila, posiciones.get(i).columna);
                }
                /**
                 * Calculo el índice en el que se encuentra el valor buscado
                 */
                indiceValor = indiceValor * posicionInt ;
            }
            /**
             * Calculo la posición a modificar
             */
            int indiceFilas = (Integer)((Vector)posiciones.get(0).ejecutar(tabla, arbol)).getFirst();
            int indiceColumnas = (Integer)((Vector)posiciones.get(1).ejecutar(tabla, arbol)).getFirst();
            int indiceAModificar = ((indiceColumnas - 1) * filasArreglo)
                    + indiceFilas;
            int tamañoMatriz = filasArreglo * columnasArreglo;

//            int tamanoPosicionAnterior = 0;
//            int tamanoPosicionActual;
//            for (int i = 2; i < posiciones.size(); i++) {
//                tamanoPosicionActual = (Integer)arreglo.getDimensiones().get(i);
//                int posicionActual = (Integer) ((Vector)posiciones.get(i).interpretar(tabla, arbol)).getFirst();
//                if ()
//                indiceAModificar = indiceAModificar *
//                        (Integer) ((Vector)posiciones.get(i).interpretar(tabla, arbol)).getFirst();
//
//                tamanoPosicionAnterior = tamanoPosicionActual;
//            }

            Object result = arreglo.get(indiceValor - 1);

            /**
             * Defino el tipo que devolvemos
             * y retornamos valor encontrado
             */
            this.tipo = new Tipo(simbolo.getTipo().getTipoDato(), Tipo.TipoEstructura.VECTOR);
            return result;

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
             * Dependiendo del tipo de acceso, accedo a la posición solicitada
             */
            AST indice = posiciones.get(0);
            LinkedList valor = (Matriz) simbolo.getValor();
            /**
             * resultado que retornaremos
             */
            Vector result = new Vector();
            int filasMatriz = ((Matriz) simbolo.getValor()).getFilas();
            int columnasMatriz = ((Matriz) simbolo.getValor()).getColumnas();

            if (indice instanceof IndiceTipoUnoMatriz) {
                Object indiceInterpretado =  indice.ejecutar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }

                LinkedList indices = (LinkedList) indiceInterpretado;
                Vector indiceFila = (Vector) indices.getFirst();
                Vector indiceColumna = (Vector) indices.get(1);

                if (!(indiceFila.getFirst() instanceof Integer &
                        indiceColumna.getFirst() instanceof Integer)){
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "Los índices de un acceso a matriz tipo 1, deben de ser de tipo entero.", indice.fila, indice.columna);
                }
                if ( (Integer)indiceFila.getFirst() > filasMatriz
                        | (Integer)indiceColumna.getFirst() > columnasMatriz
                        | (Integer)indiceFila.getFirst() < 1
                        | (Integer)indiceColumna.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice debe de estar dentro del rango de filas y columnas de la matriz. ", indice.fila, indice.columna);
                }

                int posicionBuscada = (((Integer)indiceColumna.getFirst() - 1) * filasMatriz)
                        + (Integer)indiceFila.getFirst();

                result.addAll(Arrays.asList(valor.get(posicionBuscada-1)));

            } else if (indice instanceof IndiceTipoDosMatriz) {
                Object indiceInterpretado =  indice.ejecutar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }
                LinkedList valorIndice = (LinkedList) indiceInterpretado;

                if (!((Integer) valorIndice.getFirst() instanceof Integer)) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice de un acceso a matriz tipo 2, debe de ser de tipo entero.", indice.fila, indice.columna);
                }
                if ( (Integer)valorIndice.getFirst() > filasMatriz |
                        (Integer)valorIndice.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice debe de estar dentro del rango de filas de la matriz. ", indice.fila, indice.columna);
                }
                LinkedList filaBuscada = new LinkedList();
                for (int i = ((Integer) valorIndice.getFirst()) - 1; i <= valor.size() - 1; i += filasMatriz) {
                    filaBuscada.add(valor.get(i));
                }
                result.addAll(filaBuscada);

            } else if (indice instanceof IndiceTipoTresMatriz) {
                Object indiceInterpretado =  indice.ejecutar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }
                LinkedList valorIndice = (LinkedList) indiceInterpretado;

                if (!((Integer) valorIndice.getFirst() instanceof Integer)) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice de un acceso a matriz tipo 3, debe de ser de tipo entero.", indice.fila, indice.columna);
                }
                if ( (Integer)valorIndice.getFirst() > columnasMatriz |
                        (Integer)valorIndice.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice debe de estar dentro del rango de columnas de la matriz. ", indice.fila, indice.columna);
                }
                LinkedList columnaBuscada = new LinkedList();
                int inicioColumna = (((Integer)valorIndice.getFirst() - 1) * filasMatriz);
                for (int i = inicioColumna; i <= (inicioColumna + filasMatriz) - 1; i++) {
                    columnaBuscada.add(valor.get(i));
                }
                result.addAll(columnaBuscada);
            } else if (indice instanceof IndiceTipoUno) {
                Object indiceInterpretado =  indice.ejecutar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }
                LinkedList valorIndice = (LinkedList) indiceInterpretado;

                if (!((Integer) valorIndice.getFirst() instanceof Integer)) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice de un acceso a matriz tipo 4, debe de ser de tipo entero.", indice.fila, indice.columna);
                }
                if ( (Integer)valorIndice.getFirst() > valor.size() |
                        (Integer)valorIndice.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error accediendo a valor de matriz. " +
                            "El índice debe de estar dentro la cantidad de posiciones de la matriz. ", indice.fila, indice.columna);
                }

                result.add(valor.get((Integer) valorIndice.getFirst() -1));
            }

            /**
             * Defino el tipo que devolvemos
             * y retornamos valor encontrado
             */
            this.tipo = new Tipo(simbolo.getTipo().getTipoDato(), Tipo.TipoEstructura.VECTOR);
            return result;
        } else if (simbolo.getValor() instanceof Lista) {
            this.tipo = simbolo.getTipo();
            LinkedList valor = (LinkedList) simbolo.getValor();
            for (AST posicion : posiciones) {
                Object resultPosicion = posicion.ejecutar(tabla, arbol);
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
            LinkedList valor = (LinkedList) simbolo.getValor();
            for (AST posicion : posiciones) {
                if (!(posicion instanceof IndiceTipoUno)) {
                    return new Excepcion("Semántico", "Está tratando de acceder a un vector " +
                            "usando un acceso 'tipo dos' ([[]]),  el cual solo puede usarse en listas, " +
                            "debe usar el acceso 'tipo uno' ([]).", fila, columna);
                }

                Object resultPosicion = posicion.ejecutar(tabla, arbol);
                if (resultPosicion instanceof Excepcion) {
                    return resultPosicion;
                }

                if (!(resultPosicion instanceof Vector
                        | resultPosicion instanceof Integer)) {
                    return new Excepcion("Semántico", "Error accediendo a vector. El tipo del índice no es el correcto.", posicion.fila, posicion.columna);
                }
                if (!(resultPosicion instanceof Vector)) {
                    if (!(resultPosicion instanceof Integer)) {
                        return new Excepcion("Semántico", "El acceso a la posición de un vector " +
                                "debe ser con índices de tipo INTEGER.", posicion.fila, posicion.columna);
                    }
                    if ((Integer)resultPosicion > (Integer)valor.size() |
                            (Integer)resultPosicion < 1) {
                        return new Excepcion("Semántico", "Está tratando de acceder a una posición fuera " +
                                "de rango en el VECTOR '" + simbolo.getIdentificador() + "'.", posicion.fila, posicion.columna);
//
                    }
                    valor = new Vector(Arrays.asList(valor.get((Integer)resultPosicion-1)));
                } else {
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

                this.tipo = new Tipo(valor.getFirst().getClass().getSimpleName(), Tipo.TipoEstructura.VECTOR);

            }
            return valor;
        }
    }
    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1, 10000);

        dotBuilder.append(padre+"->"+this.identificador+random);
        dotBuilder.append("\n");
        for (AST posicion : posiciones) {
            random = getRandomInRange(1, 10000);
            dotBuilder.append(padre+"->"+posicion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");
            posicion.crearDotFile(dotBuilder, posicion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

        }

        return dotBuilder.toString();
    }
}
