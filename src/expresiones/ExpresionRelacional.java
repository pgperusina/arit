package expresiones;

import abstracto.AST;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

public class ExpresionRelacional extends AST {

    public static enum OperadorRelacional {

        MAYORQUE,
        MENORQUE,
        MAYORIGUAL,
        MENORIGUAL,
        IGUALA,
        DIFERENTEDE
    }

    private AST operando1;
    private AST operando2;
    private OperadorRelacional operador;

    public ExpresionRelacional(AST operando1, AST operando2, OperadorRelacional operador, int fila, int columna) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object izquierdo = null, derecho = null;

        /**
         * Verificamos si la ejecución de los operandos es una excepción
         */
        izquierdo = this.operando1.ejecutar(tabla, arbol);
        if (izquierdo instanceof Excepcion) return izquierdo;

        derecho = this.operando2.ejecutar(tabla, arbol);
        if (derecho instanceof Excepcion) return derecho;

        /**
         * De cajón, defino el tipo Booleano
         */
        this.tipo = new Tipo(Tipo.TipoDato.BOOLEAN);
        int filasMatriz = 0, columnasMatriz = 0;

        if (this.operador == OperadorRelacional.DIFERENTEDE) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if ( operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER) ){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.getFirst().toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        }
                    } else if ( (operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                 compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                         .compareTo(Double.valueOf(operando2Valor.getFirst().toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.STRING) & operando2TipoDato.equals(Tipo.TipoDato.STRING) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.get(i))) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.getFirst())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.getFirst())
                                        .compareTo(String.valueOf(operando2Valor.get(i))) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.BOOLEAN) & operando2TipoDato.equals(Tipo.TipoDato.BOOLEAN) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Boolean.valueOf(operando2Valor.get(i).toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Boolean.valueOf(operando2Valor.getFirst().toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Boolean.valueOf(operando2Valor.get(i).toString())) == 0 ? false : true;
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en comparación de estructuras, los tipos de dato " +
                                " no son compatibles. (" + operando1TipoDato + " - " + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ) |
                        operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en comparación de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en comparación (!=), ambos operadores deben " +
                        " tener el mismo tipo de estructura. '" +operando1TipoEstructura.toString()+ "" +
                        "- " + operando2TipoEstructura.toString()+ "'.", fila, columna);
            }
        } else if (this.operador == OperadorRelacional.IGUALA) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if ( operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER) ){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.getFirst().toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( (operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.getFirst().toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.STRING) & operando2TipoDato.equals(Tipo.TipoDato.STRING) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.get(i))) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.getFirst())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.getFirst())
                                        .compareTo(String.valueOf(operando2Valor.get(i))) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.BOOLEAN) & operando2TipoDato.equals(Tipo.TipoDato.BOOLEAN) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Boolean.valueOf(operando2Valor.get(i).toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Boolean.valueOf(operando2Valor.getFirst().toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Boolean.valueOf(operando2Valor.get(i).toString())) == 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en comparación de estructuras, los tipos de dato " +
                                " no son compatibles. (" + operando1TipoDato + " - " + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            | operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ) ) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en comparación de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en comparación (==), ambos operadores deben " +
                        " tener el mismo tipo de estructura. '" +operando1TipoEstructura.toString()+ "" +
                        "- " + operando2TipoEstructura.toString()+ "'.", fila, columna);
            }
        } else if (this.operador == OperadorRelacional.MAYORQUE) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if ( operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER) ){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.getFirst().toString())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( (operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.getFirst().toString())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.STRING) & operando2TipoDato.equals(Tipo.TipoDato.STRING) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.get(i))) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.getFirst())) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.getFirst())
                                        .compareTo(String.valueOf(operando2Valor.get(i))) > 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en comparación de estructuras, los tipos de dato " +
                                " no son compatibles. (" + operando1TipoDato + " - " + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            | operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en comparación de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en comparación (>), ambos operadores deben " +
                        " tener el mismo tipo de estructura. '" +operando1TipoEstructura.toString()+ "" +
                        "- " + operando2TipoEstructura.toString()+ "'.", fila, columna);
            }
        } else if (this.operador == OperadorRelacional.MENORQUE) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if ( operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER) ){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.getFirst().toString())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( (operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.getFirst().toString())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.STRING) & operando2TipoDato.equals(Tipo.TipoDato.STRING) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.get(i))) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.getFirst())) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.getFirst())
                                        .compareTo(String.valueOf(operando2Valor.get(i))) < 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en comparación de estructuras, los tipos de dato " +
                                " no son compatibles. (" + operando1TipoDato + " - " + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            | operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ) ) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en comparación de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en comparación (<), ambos operadores deben " +
                        " tener el mismo tipo de estructura. '" +operando1TipoEstructura.toString()+ "" +
                        "- " + operando2TipoEstructura.toString()+ "'.", fila, columna);
            }
        } else if (this.operador == OperadorRelacional.MAYORIGUAL) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if ( operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER) ){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.getFirst().toString())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( (operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.getFirst().toString())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.STRING) & operando2TipoDato.equals(Tipo.TipoDato.STRING) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.get(i))) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.getFirst())) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.getFirst())
                                        .compareTo(String.valueOf(operando2Valor.get(i))) >= 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en comparación de estructuras, los tipos de dato " +
                                " no son compatibles. (" + operando1TipoDato + " - " + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            | operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ) ) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en comparación de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en comparación (>=), ambos operadores deben " +
                        " tener el mismo tipo de estructura. '" +operando1TipoEstructura.toString()+ "" +
                        "- " + operando2TipoEstructura.toString()+ "'.", fila, columna);
            }
        } else if (this.operador == OperadorRelacional.MENORIGUAL) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))
                    ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando2.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if ( operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER) ){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Integer.valueOf(operando2Valor.getFirst().toString())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Integer.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Integer.valueOf(operando2Valor.get(i).toString())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( (operando1TipoDato.equals(Tipo.TipoDato.INTEGER) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.INTEGER))
                            || (operando1TipoDato.equals(Tipo.TipoDato.NUMERIC) & operando2TipoDato.equals(Tipo.TipoDato.NUMERIC))){
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.get(i).toString())
                                        .compareTo(Double.valueOf(operando2Valor.getFirst().toString())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Double.valueOf(operando1Valor.getFirst().toString())
                                        .compareTo(Double.valueOf(operando2Valor.get(i).toString())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else if ( operando1TipoDato.equals(Tipo.TipoDato.STRING) & operando2TipoDato.equals(Tipo.TipoDato.STRING) ) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.get(i))) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.get(i))
                                        .compareTo(String.valueOf(operando2Valor.getFirst())) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = String.valueOf(operando1Valor.getFirst())
                                        .compareTo(String.valueOf(operando2Valor.get(i))) <= 0 ? true : false;
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en comparación de estructuras, los tipos de dato " +
                                " no son compatibles. (" + operando1TipoDato + " - " + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            | operando2TipoDato.equals(Tipo.TipoEstructura.MATRIZ) ) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en comparación de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en comparación (<=), ambos operadores deben " +
                        " tener el mismo tipo de estructura. '" +operando1TipoEstructura.toString()+ "" +
                        "- " + operando2TipoEstructura.toString()+ "'.", fila, columna);
            }
        }
        return null;
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1,10000);
        dotBuilder.append(padre+"->"+this.operando1.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        this.operando1.crearDotFile(dotBuilder, this.operando1.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        dotBuilder.append(padre+"->"+this.operador.name()+getRandomInRange(1,10000));
        dotBuilder.append("\n");
        random = getRandomInRange(1,10000);
        dotBuilder.append(padre+"->"+this.operando2.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        this.operando2.crearDotFile(dotBuilder, this.operando2.getClass().getSimpleName()+random);
        dotBuilder.append("\n");

        return dotBuilder.toString();
    }
}
