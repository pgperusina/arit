package expresiones;

import abstracto.AST;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;
import tablasimbolos.Tipo.TipoDato;

import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

public class ExpresionAritmetica extends AST {

    public static enum OperadorAritmetico {

        SUMA,
        RESTA,
        MULTIPLICACION,
        DIVISION,
        POTENCIA,
        MODULO,
        MENOSUNARIO
    }

    private AST operando1;
    private AST operando2;
    private AST operandoU;
    private OperadorAritmetico operador;

    public ExpresionAritmetica(AST operando1, AST operando2, OperadorAritmetico operador, int fila, int columna) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    public ExpresionAritmetica(AST operandoU, OperadorAritmetico operador, int fila, int columna) {
        this.operandoU = operandoU;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object izquierdo = null, derecho = null, unario = null;

        /**
         * Verificamos si la ejecución de los operandos es una excepción
         */
        if (this.operandoU == null) {
            izquierdo = this.operando1.ejecutar(tabla, arbol);
            if (izquierdo instanceof Excepcion) return izquierdo;
            derecho = this.operando2.ejecutar(tabla, arbol);
            if (derecho instanceof Excepcion) return derecho;
        } else {
            unario = this.operandoU.ejecutar(tabla, arbol);
            if (unario instanceof Excepcion) return unario;
        }

        int filasMatriz = 0, columnasMatriz = 0;
        if (this.operador == OperadorAritmetico.SUMA) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
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
                LinkedList operando1Valor = new LinkedList();
                LinkedList operando2Valor = new LinkedList();
                Object op1 = operando1.ejecutar(tabla, arbol);
                Object op2 = operando2.ejecutar(tabla, arbol);
                if (!(op1 instanceof LinkedList)) {
                    operando1Valor.add(op1);
                } else {
                    operando1Valor = (LinkedList)operando1.ejecutar(tabla, arbol);
                }
                if (!(op2 instanceof LinkedList)) {
                    operando2Valor.add(op2);
                } else {
                    operando2Valor = (LinkedList)operando2.ejecutar(tabla, arbol);
                }


                /**
                 * Valido que operaciones entre vector y matriz, sean las aceptadas
                 * (Vector de una sola posición).
                 */
                if (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    if ((operando1Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser " +
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
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.INTEGER);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        + Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.get(i).toString()) +
                                        Integer.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.getFirst().toString()) +
                                        Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.NUMERIC))) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.get(i).toString())
                                        + Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.get(i).toString())
                                        + Double.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.getFirst().toString())
                                        + Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.STRING) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.STRING))
                            || (operando1TipoDato.equals(TipoDato.STRING) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.STRING))
                            || (operando1TipoDato.equals(TipoDato.STRING) & operando2TipoDato.equals(TipoDato.BOOLEAN))
                            || (operando1TipoDato.equals(TipoDato.BOOLEAN) & operando2TipoDato.equals(TipoDato.STRING))
                            || (operando1TipoDato.equals(TipoDato.STRING) & operando2TipoDato.equals(TipoDato.STRING))) {
                        this.tipo = new Tipo(TipoDato.STRING);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add( String.valueOf(operando1Valor.get(i))
                                        + String.valueOf(operando2Valor.get(i)));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add( String.valueOf(operando1Valor.get(i))
                                        + String.valueOf(operando2Valor.getFirst()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add( String.valueOf(operando1Valor.getFirst())
                                        + String.valueOf(operando2Valor.get(i)));
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en suma de estructuras, los tipos de dato " +
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
                    return new Excepcion("Semántico", "Error en suma de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en suma, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorAritmetico.RESTA) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
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
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser " +
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
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.INTEGER);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer. valueOf(operando1Valor.get(i).toString())
                                        - Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        - Integer.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.getFirst().toString())
                                        - Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.NUMERIC))) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.get(i).toString())
                                        - Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Double.valueOf(operando1Valor.get(i).toString())
                                        - Double.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.getFirst().toString())
                                        - Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en resta de estructuras, los tipos de dato " +
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
                    return new Excepcion("Semántico", "Error en resta de estructuras, ambas deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en resta, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorAritmetico.MULTIPLICACION) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
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
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser" +
                                " de una sola posición ", fila, columna);
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
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.INTEGER);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        * Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        * Integer.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.getFirst().toString())
                                        * Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.NUMERIC))) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.get(i).toString())
                                        * Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Double.valueOf(operando1Valor.get(i).toString())
                                        * Double.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add( Double.valueOf(operando1Valor.getFirst().toString())
                                        * Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en multiplicación de estructuras, " +
                                "los tipos de dato no son compatibles. (" + operando1TipoDato + " - "
                                + operando2TipoDato + ").", fila, columna);
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
                    return new Excepcion("Semántico", "Error en multiplicación de estructuras, " +
                            "ambas deben ser del mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en multiplicación, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorAritmetico.DIVISION) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
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
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser" +
                                " de una sola posición ", fila, columna);
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
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.INTEGER);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, división por 0"
                                            , fila, columna);
                                }
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        / Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.getFirst().toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, división por 0"
                                            , fila, columna);
                                }
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        / Integer.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, división por 0"
                                            , fila, columna);
                                }
                                result.add(Integer.valueOf(operando1Valor.getFirst().toString())
                                        / Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.NUMERIC))) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, división por 0"
                                            , fila, columna);
                                }
                                result.add( Double.valueOf(operando1Valor.get(i).toString())
                                        / Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.getFirst().toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, división por 0"
                                            , fila, columna);
                                }
                                result.add(Double.valueOf(operando1Valor.get(i).toString())
                                        / Double.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, división por 0"
                                            , fila, columna);
                                }
                                result.add( Double.valueOf(operando1Valor.getFirst().toString())
                                        / Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en división de estructuras, " +
                                "los tipos de dato no son compatibles. (" + operando1TipoDato + " - "
                                + operando2TipoDato + ").", fila, columna);
                    }

                    if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                    |operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, filasMatriz, columnasMatriz);
                    } else {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                        return new Vector(result);
                    }
                } else {
                    return new Excepcion("Semántico", "Error en división de estructuras, " +
                            "ambas deben ser del mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en división, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorAritmetico.POTENCIA) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
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
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser" +
                                " de una sola posición ", fila, columna);
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
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Math.pow(Double.valueOf(operando1Valor.get(i).toString())
                                        ,Double.valueOf(operando2Valor.get(i).toString())));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Math.pow(Double.valueOf(operando1Valor.get(i).toString())
                                        ,Double.valueOf(operando2Valor.getFirst().toString())));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add(Math.pow(Double.valueOf(operando1Valor.getFirst().toString())
                                        ,Double.valueOf(operando2Valor.get(i).toString())));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.NUMERIC))) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Math.pow(Double.valueOf(operando1Valor.get(i).toString())
                                        ,Double.valueOf(operando2Valor.get(i).toString())));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Math.pow(Double.valueOf(operando1Valor.get(i).toString())
                                        ,Double.valueOf(operando2Valor.getFirst().toString())));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                result.add(Math.pow(Double.valueOf(operando1Valor.getFirst().toString())
                                        ,Double.valueOf(operando2Valor.get(i).toString())));
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en potenciación de estructuras, " +
                                "los tipos de dato no son compatibles. (" + operando1TipoDato + " - "
                                + operando2TipoDato + ").", fila, columna);
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
                    return new Excepcion("Semántico", "Error en potenciación de estructuras, " +
                            "ambas deben ser del mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en potenciación, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorAritmetico.MODULO) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
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
                        return new Excepcion("Semántico", "Error en comparación Vector-Matriz, el vector debe ser" +
                                " de una sola posición ", fila, columna);
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
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.INTEGER);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, módulo por 0"
                                            , fila, columna);
                                }
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        % Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.getFirst().toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, módulo por 0"
                                            , fila, columna);
                                }
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
                                        % Integer.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, módulo por 0"
                                            , fila, columna);
                                }
                                result.add(Integer.valueOf(operando1Valor.getFirst().toString())
                                        % Integer.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else if ( (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.NUMERIC))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.INTEGER))
                            || (operando1TipoDato.equals(TipoDato.NUMERIC) & operando2TipoDato.equals(TipoDato.NUMERIC))) {
                        this.tipo = new Tipo(TipoDato.NUMERIC);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, módulo por 0"
                                            , fila, columna);
                                }
                                result.add( Double.valueOf(operando1Valor.get(i).toString())
                                        % Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.getFirst().toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, módulo por 0"
                                            , fila, columna);
                                }
                                result.add(Double.valueOf(operando1Valor.get(i).toString())
                                        % Double.valueOf(operando2Valor.getFirst().toString()));
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                if (Double.valueOf(operando2Valor.get(i).toString()) == 0) {
                                    return new Excepcion("Semántico", "Error aritmético, módulo por 0"
                                            , fila, columna);
                                }
                                result.add( Double.valueOf(operando1Valor.getFirst().toString())
                                        % Double.valueOf(operando2Valor.get(i).toString()));
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en módulo de estructuras, " +
                                "los tipos de dato no son compatibles. (" + operando1TipoDato + " - "
                                + operando2TipoDato + ").", fila, columna);
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
                    return new Excepcion("Semántico", "Error en módulo de estructuras, " +
                            "ambas deben ser del mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en módulo, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorAritmetico.MENOSUNARIO) {
            Tipo.TipoEstructura operandoUnarioTipoEstructura = operandoU.getTipo().getTipoEstructura();
            TipoDato operandoUnarioTipoDato = operandoU.getTipo().getTipoDato();
            LinkedList operandoUnarioValor = (LinkedList) operandoU.ejecutar(tabla, arbol);

            LinkedList result = new LinkedList();
            if (operandoUnarioTipoDato.equals(TipoDato.INTEGER)) {
                this.tipo = new Tipo(TipoDato.INTEGER);
                for (int i = 0; i < operandoUnarioValor.size(); i++) {
                    result.add(Integer.valueOf(operandoUnarioValor.get(i).toString()) * -1);
                }
            } else if ( operandoUnarioTipoDato.equals(TipoDato.NUMERIC) ) {
                this.tipo = new Tipo(TipoDato.NUMERIC);
                for (int i = 0; i < operandoUnarioValor.size(); i++) {
                    result.add(Double.valueOf(operandoUnarioValor.get(i).toString()) * -1);
                }
            } else {
                return new Excepcion("Semántico", "El operador menos unario es solo aplicable " +
                        " a enteros y numericos.", fila, columna);
            }

            if (operandoUnarioTipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                return new Matriz(result, ((Matriz)operandoU.ejecutar(tabla, arbol)).getFilas(),
                        ((Matriz)operandoU.ejecutar(tabla, arbol)).getColumnas());
            } else {
                this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                return new Vector(result);
            }

        }

        return null;
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1,10000);
        if (operando1 != null & operando2 != null) {
            dotBuilder.append(padre + "->" + this.operando1.getClass().getSimpleName() + random);
            dotBuilder.append("\n");
            this.operando1.crearDotFile(dotBuilder, this.operando1.getClass().getSimpleName() + random);
            dotBuilder.append("\n");
            dotBuilder.append(padre + "->" + this.operador.name() + getRandomInRange(1, 10000));
            dotBuilder.append("\n");
            random = getRandomInRange(1, 10000);
            dotBuilder.append(padre + "->" + this.operando2.getClass().getSimpleName() + random);
            dotBuilder.append("\n");
            this.operando2.crearDotFile(dotBuilder, this.operando2.getClass().getSimpleName() + random);
            dotBuilder.append("\n");
        } else {
            random = getRandomInRange(1, 10000);
            dotBuilder.append(padre + "->" + this.operador.name()+ random);
            dotBuilder.append("\n");
            dotBuilder.append(padre + "->" + this.operandoU.getClass().getSimpleName() + random);
            dotBuilder.append("\n");
            this.operandoU.crearDotFile(dotBuilder, this.operandoU.getClass().getSimpleName()+random);
            dotBuilder.append("\n");
        }

        return dotBuilder.toString();
    }
}
