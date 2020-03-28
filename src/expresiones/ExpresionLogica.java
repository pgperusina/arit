package expresiones;

import abstracto.AST;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

public class ExpresionLogica extends AST {

    public static enum OperadorLogico {
        AND,
        OR,
        NOT
    }

    private AST operando1;
    private AST operando2;
    private AST operandoU;
    private OperadorLogico operador;

    public ExpresionLogica(AST operando1, AST operando2, OperadorLogico operador, int fila, int columna) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    public ExpresionLogica(AST operandoU, OperadorLogico operador, int fila, int columna) {
        this.operandoU = operandoU;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object izquierdo = null, derecho = null, unario = null;

        if (this.operandoU == null) {
            izquierdo = this.operando1.ejecutar(tabla, arbol);
            if (izquierdo instanceof Excepcion) return izquierdo;

            derecho = this.operando2.ejecutar(tabla, arbol);
            if (derecho instanceof Excepcion) return derecho;
        } else {
            unario = this.operandoU.ejecutar(tabla, arbol);
            if (unario instanceof Excepcion) return unario;
        }

        this.tipo = new Tipo(Tipo.TipoDato.BOOLEAN);

        int filasMatriz = 0, columnasMatriz = 0;
        if (this.operador == OperadorLogico.AND) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ((operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
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
                } else if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) {
                    if ((operando2Valor.size() > 1)) {
                        return new Excepcion("Semántico", "Error en comparación Matriz-Vector, el vector debe ser  " +
                                "de una sola posición ", fila, columna);
                    }
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                } else if (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                        & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    filasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getFilas();
                    columnasMatriz = ((Matriz) operando1.ejecutar(tabla, arbol)).getColumnas();
                }

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    boolean compareResult;
                    if (operando1TipoDato.equals(Tipo.TipoDato.BOOLEAN) & operando2TipoDato.equals(Tipo.TipoDato.BOOLEAN)) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        & Boolean.valueOf(operando2Valor.get(i).toString());
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        & Boolean.valueOf(operando2Valor.getFirst().toString());
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.getFirst().toString())
                                        & Boolean.valueOf(operando2Valor.get(i).toString());
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en operación lógica, los tipos de dato " +
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
                    return new Excepcion("Semántico", "Error en operación lógica, ambas estructuras deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en operación lógica, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorLogico.OR) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
            Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
            Tipo.TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
            Tipo.TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();
            if ((operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
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
                    if (operando1TipoDato.equals(Tipo.TipoDato.BOOLEAN) & operando2TipoDato.equals(Tipo.TipoDato.BOOLEAN)) {
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        || Boolean.valueOf(operando2Valor.get(i).toString());
                                result.add(compareResult);
                            }
                        } else if (operando1Valor.size() > operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.get(i).toString())
                                        || Boolean.valueOf(operando2Valor.getFirst().toString());
                                result.add(compareResult);
                            }
                        } else {
                            for (int i = 0; i < operando2Valor.size(); i++) {
                                compareResult = Boolean.valueOf(operando1Valor.getFirst().toString())
                                        || Boolean.valueOf(operando2Valor.get(i).toString());
                                result.add(compareResult);
                            }
                        }
                    } else {
                        return new Excepcion("Semántico", "Error en operación lógica, los tipos de dato " +
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
                    return new Excepcion("Semántico", "Error en operación lógica, ambas estructuras deben ser del " +
                            " mismo tamaño o una debe ser escalar. ", fila, columna);
                }

            } else {
                return new Excepcion("Semántico", "Error de tipos en operación lógica, ambos operadores deben " +
                        " tener el mismo tipo de estructura. +", fila, columna);
            }
        } else if (this.operador == OperadorLogico.NOT) {
            Tipo.TipoEstructura operandoUnarioTipoEstructura = operandoU.getTipo().getTipoEstructura();
            Tipo.TipoDato operandoUnarioTipoDato = operandoU.getTipo().getTipoDato();

            if ( operandoUnarioTipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                   || operandoUnarioTipoEstructura.equals(Tipo.TipoEstructura.MATRIZ) ) {
                LinkedList operandoUnarioValor = (LinkedList) operandoU.ejecutar(tabla, arbol);
                LinkedList result = new LinkedList();
                if (operandoUnarioTipoDato.equals(Tipo.TipoDato.BOOLEAN)) {
                    for (int i = 0; i < operandoUnarioValor.size(); i++) {
                        result.add(!Boolean.valueOf(operandoUnarioValor.get(i).toString()));
                    }
                } else {
                    return new Excepcion("Semántico", "Error en operación lógica, el operando " +
                            " debe de ser de tipo booleano. (" + operandoUnarioTipoDato + ").", fila, columna);
                }

                if (operandoUnarioTipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)) {
                    this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                    return new Matriz(result, ((Matriz) operandoU.ejecutar(tabla, arbol)).getFilas(),
                            ((Matriz) operandoU.ejecutar(tabla, arbol)).getColumnas());
                } else {
                    this.tipo.setTipoEstructura(Tipo.TipoEstructura.VECTOR);
                    return new Vector(result);
                }
            } else {
                return new Excepcion("Semántico", "Error de tipos en operación lógica unaria, el operador " +
                        " debe de ser de tipo Vector o Matriz. +", fila, columna);
            }
        }
        return null;
    }
}
