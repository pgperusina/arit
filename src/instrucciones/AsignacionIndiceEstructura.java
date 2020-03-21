package instrucciones;

import abstracto.AST;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.*;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;
import utilities.Utils;

import java.util.Arrays;
import java.util.LinkedList;

import static utilities.Utils.definirPrioridadCasteo;

public class AsignacionIndiceEstructura extends AST {

    private String identificador;
    private LinkedList<AST> posiciones;
    private AST valor;

    public AsignacionIndiceEstructura(String identificador, LinkedList<AST> posiciones, AST valor, int fila, int columna) {
        this.identificador = identificador;
        this.posiciones = posiciones;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {

        Simbolo simbolo = tabla.getVariable(this.identificador);
        if (simbolo == null) {
            return new Excepcion("Semántico", "Se está tratando de modificar una posición de una variable " +
                    "que no ha sido definida aún. '" + this.identificador + "'.", fila, columna);
        }
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.ARREGLO)) {
            //todo modificación a arreglo
        } else if (simbolo.getValor() instanceof Matriz) {
            /**
             * Valido que las modificaciones sean solo las aceptadas para
             * las matrices
             */
            if (posiciones.size() > 1) {
                return new Excepcion("Semántico", "La modificación de matrices " +
                        "no permite mas de dos índices.", posiciones.get(1).fila, posiciones.get(1).columna);
            }

            AST indice = posiciones.get(0);
            /**
             * Número de filas y columnas de la matriz a modificar
             */
            int filasMatriz = ((Matriz) simbolo.getValor()).getFilas();
            int columnasMatriz = ((Matriz) simbolo.getValor()).getColumnas();

            /**
             * Dependiendo del tipo de acceso, accedo a la posición solicitada
             */
            if (indice instanceof IndiceTipoUnoMatriz) {
                Object indiceInterpretado =  indice.interpretar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }

                /**
                 * Obtengo los dos índices de la modificación tipo 1
                 * así obtendré la posición exacta a modificar
                 */
                LinkedList indices = (LinkedList) indiceInterpretado;
                Vector indiceFila = (Vector) indices.getFirst();
                Vector indiceColumna = (Vector) indices.get(1);

                if (!(indiceFila.getFirst() instanceof Integer &
                        indiceColumna.getFirst() instanceof Integer)){
                    return new Excepcion("Semántico", "Error modificando la matriz " +
                            "'" + simbolo.getIdentificador() +"'. " +
                            "Los índices de una modificación de matriz tipo 1, deben de ser de tipo entero.", indice.fila, indice.columna);
                }
                if ( (Integer)indiceFila.getFirst() > filasMatriz
                        | (Integer)indiceColumna.getFirst() > columnasMatriz
                        | (Integer)indiceFila.getFirst() < 1
                        | (Integer)indiceColumna.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() +"'. " +
                            "El índice debe de estar dentro del rango de filas y columnas de la matriz. ",
                            indice.fila, indice.columna);
                }

                /**
                 * Obtengo al valor que deseo colocar en la posición de la matriz
                 */
                Object valorInterpretado =  valor.interpretar(tabla, arbol);
                /**
                 * Si el valor no es un vector, retornar excepción
                 */
                if (!(valorInterpretado instanceof Vector)) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() +"'. " +
                            "El valor para modificar la matriz debe de ser un Vector. ",
                            valor.fila, valor.columna);
                }
                /**
                 * Si el valor no es un vector de una posición, retornar excepción
                 */
                if (((Vector) valorInterpretado).size() > 1) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() +"'. " +
                            "El valor para modificar la matriz debe de ser un Vector de una sola posición. ",
                            valor.fila, valor.columna);
                }

                /**
                 * Calcular prioridades de casteo para la matriz y para el nuevo valor
                 */
                int prioridadCasteoNuevoValor = Utils.definirPrioridadCasteo(valor, arbol);
                int prioridadCasteoMatriz = Utils.definirPrioridadCasteo(simbolo, arbol);

                /**
                 * Calculo la posición a modificar
                 */
                int posicionAmodificar = (((Integer)indiceColumna.getFirst() - 1) * filasMatriz)
                        + (Integer)indiceFila.getFirst();
                /**
                 * Si la prioridad de casteo de la matriz es mayor a la del nuevo valor
                 * se castea el nuevo valor al tipo de la matriz y se inserta en la matriz,
                 * de lo contrario, se castea la matriz y luego se inserta el nuevo valor.
                 */
                LinkedList valorSimbolo = (LinkedList) simbolo.getValor();
                if (prioridadCasteoMatriz >= prioridadCasteoNuevoValor) {
                    modificarValorEstructura(simbolo, valorSimbolo, posicionAmodificar,
                            (LinkedList)valorInterpretado, prioridadCasteoMatriz);
                } else {
                    castearEstructura(simbolo, valorSimbolo, (LinkedList)valorInterpretado,
                            prioridadCasteoNuevoValor);
                    modificarValorEstructura(simbolo, valorSimbolo, posicionAmodificar,
                            (LinkedList)valorInterpretado, prioridadCasteoNuevoValor);
                }
                return null;
            } else if (indice instanceof IndiceTipoDosMatriz) {
                Object indiceInterpretado =  indice.interpretar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }
                LinkedList valorIndice = (LinkedList) indiceInterpretado;

                /**
                 * Verifico que el índice sea de tipo entero
                 */
                if (!((Integer) valorIndice.getFirst() instanceof Integer)) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() + "'. El índice de una modificación a matriz tipo 2, " +
                            "debe de ser de tipo entero.",
                            indice.fila, indice.columna);
                }
                /**
                 * Verifico que el índice esté dentro del rango de filas.
                 */
                if ( (Integer)valorIndice.getFirst() > filasMatriz |
                        (Integer)valorIndice.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() + "'. El índice debe de estar dentro del " +
                            "rango de filas de la matriz. ",
                            indice.fila, indice.columna);
                }
                /**
                 * Obtengo al valor que deseo colocar en la posición de la matriz
                 */
                Object resultValor =  valor.interpretar(tabla, arbol);
                /**
                 * Si el valor no es un vector, retornar excepción
                 */
                if (!(resultValor instanceof Vector)) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() +"'. " +
                            "El valor para modificar la matriz debe de ser un Vector. ",
                            valor.fila, valor.columna);
                }
                /**
                 * Calcular prioridades de casteo para la matriz y para el nuevo valor
                 */
                int prioridadCasteoNuevoValor = Utils.definirPrioridadCasteo(valor, arbol);
                int prioridadCasteoMatriz = Utils.definirPrioridadCasteo(simbolo, arbol);

                /**
                 * Si la prioridad de casteo de la matriz es mayor a la del nuevo valor
                 * se castea el nuevo valor al tipo de la matriz y se inserta en la matriz,
                 * de lo contrario, se castea la matriz y luego se inserta el nuevo valor.
                 */
                LinkedList valorSimbolo = (LinkedList) simbolo.getValor();
                LinkedList valorInterpretado = (LinkedList) resultValor;
                if (prioridadCasteoMatriz >= prioridadCasteoNuevoValor) {
                    castearEstructura(simbolo, valorInterpretado, prioridadCasteoMatriz);
                    modificarMatrizIndiceTipoDos(filasMatriz, valorIndice, valorSimbolo, valorInterpretado);
                } else {
                    castearEstructura(simbolo, valorSimbolo, prioridadCasteoNuevoValor);
                    modificarMatrizIndiceTipoDos(filasMatriz, valorIndice, valorSimbolo, valorInterpretado);
                }
                return null;
            } else if (indice instanceof IndiceTipoTresMatriz) {
                Object indiceInterpretado =  indice.interpretar(tabla, arbol);
                if (indiceInterpretado instanceof Excepcion) {
                    return indiceInterpretado;
                }
                LinkedList valorIndice = (LinkedList) indiceInterpretado;

                /**
                 * Verifico que el índice sea de tipo entero
                 */
                if (!((Integer) valorIndice.getFirst() instanceof Integer)) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() + "'. El índice de una modificación a matriz tipo 3, " +
                            "debe de ser de tipo entero.",
                            indice.fila, indice.columna);
                }
                /**
                 * Verifico que el índice esté dentro del rango de columnas.
                 */
                if ( (Integer)valorIndice.getFirst() > columnasMatriz |
                        (Integer)valorIndice.getFirst() < 1) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() + "'. El índice debe de estar dentro del " +
                            "rango de columnas de la matriz. ",
                            indice.fila, indice.columna);
                }
                /**
                 * Obtengo al valor que deseo colocar en la posición de la matriz
                 */
                Object resultValor =  valor.interpretar(tabla, arbol);
                /**
                 * Si el valor no es un vector, retornar excepción
                 */
                if (!(resultValor instanceof Vector)) {
                    return new Excepcion("Semántico", "Error modificando la matriz. " +
                            "'" + simbolo.getIdentificador() +"'. " +
                            "El valor para modificar la matriz debe de ser un Vector. ",
                            valor.fila, valor.columna);
                }
                /**
                 * Calcular prioridades de casteo para la matriz y para el nuevo valor
                 */
                int prioridadCasteoNuevoValor = Utils.definirPrioridadCasteo(valor, arbol);
                int prioridadCasteoMatriz = Utils.definirPrioridadCasteo(simbolo, arbol);

                /**
                 * Si la prioridad de casteo de la matriz es mayor a la del nuevo valor
                 * se castea el nuevo valor al tipo de la matriz y se inserta en la matriz,
                 * de lo contrario, se castea la matriz y luego se inserta el nuevo valor.
                 */
                LinkedList valorSimbolo = (LinkedList) simbolo.getValor();
                LinkedList valorInterpretado = (LinkedList) resultValor;
                int inicioColumna = (((Integer)valorIndice.getFirst() - 1) * filasMatriz);
                if (prioridadCasteoMatriz >= prioridadCasteoNuevoValor) {
                    castearEstructura(simbolo, valorInterpretado, prioridadCasteoMatriz);
                    modificarMatrizIndiceTipoTres(inicioColumna, filasMatriz,
                            valorIndice, valorSimbolo, valorInterpretado);
                } else {
                    castearEstructura(simbolo, valorSimbolo, prioridadCasteoNuevoValor);
                    modificarMatrizIndiceTipoTres(inicioColumna, filasMatriz,
                            valorIndice, valorSimbolo, valorInterpretado);
                }
                return null;
            }
            return null;
        } else if (simbolo.getValor() instanceof Lista) {
            LinkedList valorSimbolo = (LinkedList) simbolo.getValor();
            /**
             * Recorro cada posición de los índices de asignación para llegar hasta la posición
             * deseada y modificarla
             */
            for (AST posicion : posiciones) {
                Object resultPosicion = posicion.interpretar(tabla, arbol);
                if (resultPosicion instanceof Excepcion) {
                    return resultPosicion;
                }
                /**
                 * Verifico que el índice sea instancia de Vector
                 */
                if (!(resultPosicion instanceof Vector)) {
                    return new Excepcion("Semántico", "Error modificando la lista. " +
                            "El índice debe ser un Vector de tipo entero", posicion.fila, posicion.columna);
                }
                LinkedList valorPosicion = (LinkedList)resultPosicion;
                /**
                 * Verifico que el valor del índice tenga un solo valor
                 */
                if (!((Integer)valorPosicion.getFirst() > 0)) {
                    return new Excepcion("Semántico", "La modificación de vectores vía índice " +
                            "requiere que los índices a modificar sean mayores a '0'", posicion.fila, posicion.columna);
                }
                Integer indice = (Integer)valorPosicion.getFirst();

                /**
                 * Verifico el tipo de acceso, tipo 1 o tipo 2
                 * Si es tipo 1 debo agregar solamente vectores o listas con un solo valor
                 * Si es tipo 2 debo agregar cualquier valor, listas o vectores de cualquier tamaño
                 */
                LinkedList valorInterpretado = (LinkedList) valor.interpretar(tabla, arbol);
                if (posicion instanceof IndiceTipoUno) {
                    /**
                     * Si el valor que se usará para modificar es mayor a uno retornar
                     * una excepción
                     */
                    if (valorInterpretado.size() > 1) {
                        return new Excepcion("Semántico", "La modificación de listas 'Tipo 1' " +
                                "solamente acepta valores de tamaño 1.", posicion.fila, posicion.columna);
                    }
                    /**
                     * Verifico si el índice a modificar es mayor al tamaño de la lista
                     * Si es así agrego valores default
                     */
                    if (agregarValoresDefaultALista(valorSimbolo, indice)) {
                        valorSimbolo.add(valorInterpretado);
                    } else {
                        valorSimbolo.set(indice - 1, valorInterpretado);
                    }
                }
                if (posicion instanceof IndiceTipoDos) {
                    if (agregarValoresDefaultALista(valorSimbolo, indice)) {
                        valorSimbolo.add(valorInterpretado);
                    } else {
                        valorSimbolo.set(indice - 1, valorInterpretado);
                    }
                }
            }
            return null;
        } else {
            /**
             * MODIFICACIÓN A VECTORES
             */
            this.tipo = simbolo.getTipo();
            if (posiciones.size() > 1) {
                return new Excepcion("Semántico", "La modificación de vectores solo admite el acceso " +
                        "a un índice.", fila, columna);
            }
            LinkedList valorSimbolo = (LinkedList) simbolo.getValor();
            Object resultPosicion = posiciones.getFirst().interpretar(tabla, arbol);
            if (resultPosicion instanceof Excepcion) {
                return resultPosicion;
            }
            LinkedList valorPosicion = (LinkedList)resultPosicion;
            if (!(valorPosicion.getFirst() instanceof Integer)) {
                return new Excepcion("Semántico", "La modificación de vectores vía índice " +
                        "requiere que los índices sean de tipo INTEGER.",
                        posiciones.get(0).fila, posiciones.get(0).columna);
            }
            if (!( (Integer)valorPosicion.getFirst() > 0 )) {
                return new Excepcion("Semántico", "La modificación de vectores vía índice " +
                        "requiere que los índices a modificar sean mayores a '0'",
                        posiciones.get(0).fila, posiciones.get(0).columna);
            }
            Object result = valor.interpretar(tabla, arbol);

            if (!(result instanceof LinkedList)) {
                return new Excepcion("Semántico", "Error modificando posición '" + (Integer)valorPosicion.getFirst()
                        + "' de Vector.", posiciones.get(0).fila, posiciones.get(0).columna);
            }
            LinkedList valorIntepretado = (LinkedList)valor.interpretar(tabla, arbol);
            if (valorIntepretado.size() > 1 ) {
                return new Excepcion("Semántico", "La modificación de vectores vía índice " +
                        "requiere que el nuevo valor sea de tamaño 1.",
                        posiciones.get(0).fila, posiciones.get(0).columna);
            }

            //todo convertir todos los elementos del vector al tipo del elemento que se esta asignando basado en reglas de casteo
            int prioridadCasteoSimbolo = definirPrioridadCasteo(simbolo, arbol);
            int prioridadCasteoValor = definirPrioridadCasteo(valor, arbol);

            if (prioridadCasteoValor > 3) {
                return new Excepcion("Semántico", "Un vector solo acepta tipos de datos primitivos y vectores.",
                        valor.fila, valor.columna);
            }

            /**
             * Si la prioridad de casteo del símbolo existente es mayor a la prioridad de casteo
             * del valor a insertar, se castea solamente el valor a insertar, de lo contrario
             * se castean los valores existentes en el simbolo y luego se inserta el nuevo valor.
             */
            if (prioridadCasteoSimbolo >= prioridadCasteoValor) {
                /**
                 * Si se quiere insertar un valor en una posición mayor al tamaño actual del vector
                 * Se insertarán elementos default antes de insertar el nuevo valor
                 */
                if ((Integer)valorPosicion.getFirst() > valorSimbolo.size()) {
                    /**
                     * Insertando elementos default
                     */
                    insertarVaciosBasadoEnTipo(valorSimbolo, valorPosicion, simbolo.getTipo());
                    /**
                     * Insertando nuevo valor al final del vector
                     */
                    insertarNuevoValorVector(simbolo, valorSimbolo, valorIntepretado, prioridadCasteoSimbolo);
                    return null;
                }
                /**
                 * Modificando valor existente
                 */
                modificarValorEstructura(simbolo, valorSimbolo, valorPosicion, valorIntepretado, prioridadCasteoSimbolo);
                return null;
            } else {
                /**
                 * Casteando los valores actuales del vector al tipo del nuevo valor a insertar
                 */
                castearEstructura(simbolo, valorSimbolo, valorIntepretado, prioridadCasteoValor);
                /**
                 * Si se quiere insertar un valor en una posición mayor al tamaño actual del vector
                 * Se insertarán elementos default antes de insertar el nuevo valor
                 */
                if ((Integer)valorPosicion.getFirst() > valorSimbolo.size()) {
                    /**
                     * Insertando elementos default
                     */
                    insertarVaciosBasadoEnTipo(valorSimbolo, valorPosicion, valor.getTipo());
                    /**
                     * Insertando nuevo valor al final del vector
                     */
                    insertarNuevoValorVector(simbolo, valorSimbolo, valorIntepretado, prioridadCasteoValor);
                }
                /**
                 * Modificando valor existente
                 */
                modificarValorEstructura(simbolo, valorSimbolo, valorPosicion, valorIntepretado, prioridadCasteoSimbolo);
                return null;
            }
        }
        return null;
    }

    private void modificarMatrizIndiceTipoDos(int filasMatriz, LinkedList valorIndice,
                                              LinkedList valorSimbolo, LinkedList valorInterpretado) {
        int posicionValor = 0;
        for (int i = ((Integer) valorIndice.getFirst()) - 1;
             i <= valorSimbolo.size() - 1;
             i += filasMatriz) {
            /**
             * Si la posicion actual es menor o igual al tamaño
             * del nuevo valor, sigo jalando datos, cuando llegue al
             * tamaño del valor, reinicio posicionValor a 0
             */
            if (posicionValor <= valorInterpretado.size() - 1) {
                valorSimbolo.set(i, valorInterpretado.get(posicionValor++));
            } else {
                posicionValor = 0;
                valorSimbolo.set(i, valorInterpretado.get(posicionValor++));
            }
        }
    }

    private void modificarMatrizIndiceTipoTres(int inicioColumna, int filasMatriz, LinkedList valorIndice,
                                              LinkedList valorSimbolo, LinkedList valorInterpretado) {
        int posicionValor = 0;
        for (int i = inicioColumna; i <= (inicioColumna + filasMatriz) - 1; i++) {
            if (posicionValor <= valorInterpretado.size() - 1) {
                valorSimbolo.set(i, valorInterpretado.get(posicionValor++));
            } else {
                posicionValor = 0;
                valorSimbolo.set(i, valorInterpretado.get(posicionValor++));
            }
        }
    }

    private boolean agregarValoresDefaultALista(LinkedList valorSimbolo, Integer indice) {
        if (indice > valorSimbolo.size()) {
            for(int i = valorSimbolo.size(); i < indice - 1; i++) {
                valorSimbolo.add(i, new Vector(Arrays.asList("NULL")));
            }
            return true;
        }
        return false;
    }

    private void castearEstructura(Simbolo simbolo, LinkedList valorSimbolo,
                                   LinkedList valorIntepretado, int prioridadCasteo) {
        Object temp;
        if (prioridadCasteo == 3) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.STRING, Tipo.TipoEstructura.VECTOR));
            for (int i = 0; i < valorSimbolo.size(); i++) {
                temp = valorSimbolo.get(i);
                valorSimbolo.set(i, String.valueOf(temp));
            }
        } else if (prioridadCasteo == 2) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.NUMERIC, Tipo.TipoEstructura.VECTOR));
            for (int i = 0; i < valorSimbolo.size(); i++) {
                temp = valorSimbolo.get(i);
                if (temp instanceof  Boolean) {
                    valorSimbolo.set(i, temp == Boolean.TRUE ? "1.0" : "0.0");
                } else if (temp instanceof Integer){
                    valorSimbolo.set(i, Double.valueOf(temp.toString()));
                } else {
                    valorSimbolo.set(i, valorIntepretado.getFirst());
                }
            }
        } else if (prioridadCasteo == 1) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.INTEGER, Tipo.TipoEstructura.VECTOR));
            for (int i = 0; i < valorSimbolo.size(); i++) {
                temp = valorSimbolo.get(i);
                if (temp instanceof  Boolean) {
                    valorSimbolo.set(i, temp == Boolean.TRUE ? "1" : "0");
                } else {
                    valorSimbolo.set(i, temp);
                }
            }
        } else {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.BOOLEAN, Tipo.TipoEstructura.VECTOR));
            for (int i = 0; i < valorSimbolo.size(); i++) {
                temp = valorSimbolo.get(i);
                valorSimbolo.set(i, temp);
            }
        }
    }

    private void castearEstructura(Simbolo simbolo, LinkedList estructura, int prioridadCasteo) {
        Object temp;
        if (prioridadCasteo == 3) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.STRING, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                estructura.set(i, String.valueOf(temp));
            }
        } else if (prioridadCasteo == 2) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.NUMERIC, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                if (temp instanceof  Boolean) {
                    estructura.set(i, temp == Boolean.TRUE ? "1.0" : "0.0");
                } else if (temp instanceof Integer){
                    estructura.set(i, Double.valueOf(temp.toString()));
                } else {
                    estructura.set(i, temp);
                }
            }
        } else if (prioridadCasteo == 1) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.INTEGER, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                if (temp instanceof  Boolean) {
                    estructura.set(i, temp == Boolean.TRUE ? "1" : "0");
                } else {
                    estructura.set(i, temp);
                }
            }
        } else {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.BOOLEAN, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                estructura.set(i, temp);
            }
        }
    }

    private void modificarValorEstructura(Simbolo simbolo, LinkedList valorSimbolo, LinkedList valorPosicion,
                                          LinkedList valorIntepretado, int prioridadCasteo) {
        if (prioridadCasteo == 3) {
            valorSimbolo.set((Integer)valorPosicion.getFirst()-1, String.valueOf(valorIntepretado.getFirst()));
        } else if (prioridadCasteo == 2) {
            if (valorIntepretado.getFirst() instanceof  Boolean) {
                valorSimbolo.set((Integer)valorPosicion.getFirst()-1,valorIntepretado.getFirst() == Boolean.TRUE ? "1.0" : "0.0");
            } else if (valorIntepretado.getFirst() instanceof Integer){
                valorSimbolo.set((Integer)valorPosicion.getFirst()-1,Integer.valueOf(valorIntepretado.getFirst().toString()));
            } else {
                valorSimbolo.set((Integer)valorPosicion.getFirst()-1,valorIntepretado.getFirst());
            }
        } else if (prioridadCasteo == 1) {
            if (valorIntepretado.getFirst() instanceof  Boolean) {
                valorSimbolo.set((Integer)valorPosicion.getFirst()-1,valorIntepretado.getFirst() == Boolean.TRUE ? "1" : "0");
            } else {
                valorSimbolo.set((Integer)valorPosicion.getFirst()-1,valorIntepretado.getFirst());
            }
        } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
            valorSimbolo.set((Integer)valorPosicion.getFirst()-1,valorIntepretado.getFirst());
        }
    }

    private void modificarValorEstructura(Simbolo simbolo, LinkedList valorSimbolo,
                                          int valorPosicion, LinkedList valorIntepretado,
                                          int prioridadCasteo) {
        if (prioridadCasteo == 3) {
            valorSimbolo.set(valorPosicion-1, String.valueOf(valorIntepretado.getFirst()));
        } else if (prioridadCasteo == 2) {
            if (valorIntepretado.getFirst() instanceof  Boolean) {
                valorSimbolo.set(valorPosicion-1,valorIntepretado.getFirst() == Boolean.TRUE ? "1.0" : "0.0");
            } else if (valorIntepretado.getFirst() instanceof Integer){
                valorSimbolo.set(valorPosicion-1,Integer.valueOf(valorIntepretado.getFirst().toString()));
            } else {
                valorSimbolo.set(valorPosicion-1,valorIntepretado.getFirst());
            }
        } else if (prioridadCasteo == 1) {
            if (valorIntepretado.getFirst() instanceof  Boolean) {
                valorSimbolo.set(valorPosicion-1,valorIntepretado.getFirst() == Boolean.TRUE ? "1" : "0");
            } else {
                valorSimbolo.set(valorPosicion-1,valorIntepretado.getFirst());
            }
        } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
            valorSimbolo.set(valorPosicion-1,valorIntepretado.getFirst());
        }
    }

    private void insertarNuevoValorVector(Simbolo simbolo, LinkedList valorSimbolo, LinkedList valorIntepretado, int prioridadCasteo) {
        if (prioridadCasteo == 3) {
            valorSimbolo.add(String.valueOf(valorIntepretado.getFirst()));
        } else if (prioridadCasteo == 2) {
            if (valorIntepretado.getFirst() instanceof Boolean) {
                valorSimbolo.add(valorIntepretado.getFirst() == Boolean.TRUE ? "1.0" : "0.0");
            } else if (valorIntepretado.getFirst() instanceof Integer) {
                valorSimbolo.add(Integer.valueOf(valorIntepretado.getFirst().toString()));
            } else {
                valorSimbolo.add(valorIntepretado.getFirst());
            }
        } else if (prioridadCasteo == 1) {
            if (valorIntepretado.getFirst() instanceof Boolean) {
                valorSimbolo.add(valorIntepretado.getFirst() == Boolean.TRUE ? "1" : "0");
            } else {
                valorSimbolo.add(valorIntepretado.getFirst());
            }
        } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
            valorSimbolo.add(valorIntepretado.getFirst());
        }
    }

    private void insertarVaciosBasadoEnTipo(LinkedList valorSimbolo, LinkedList valorPosicion, Tipo tipo) {
        for (int i = valorSimbolo.size(); i < (Integer)valorPosicion.getFirst()-1; i++) {
            if (tipo.getTipoDato().equals(Tipo.TipoDato.STRING)) {
                valorSimbolo.add(i, "NULL");
            } else if (tipo.getTipoDato().equals(Tipo.TipoDato.NUMERIC)) {
                valorSimbolo.add(i, 0.0);
            } else if (tipo.getTipoDato().equals(Tipo.TipoDato.INTEGER)) {
                valorSimbolo.add(i, 0);
            } else if (tipo.getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                valorSimbolo.add(i, false);
            }
        }
    }


}
