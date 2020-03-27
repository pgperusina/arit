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

public class ExpresionAritmetica extends AST {

    public static enum OperadorAritmetico {

        SUMA,
        RESTA,
        MULTIPLICACION,
        DIVISION,
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
         * Verificamos si la ejecución de los operadores es una excepción
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

        Tipo.TipoEstructura operando1TipoEstructura = operando1.getTipo().getTipoEstructura();
        Tipo.TipoEstructura operando2TipoEstructura = operando2.getTipo().getTipoEstructura();
        TipoDato operando1TipoDato = operando1.getTipo().getTipoDato();
        TipoDato operando2TipoDato = operando2.getTipo().getTipoDato();

        if (this.operador == OperadorAritmetico.SUMA) {
            /**
             * Si el operando 1 y el 2 son vectores o matrices
             */
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

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

                    if (operando1.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, ((Matriz)operando1.ejecutar(tabla, arbol)).getFilas(),
                                ((Matriz)operando1.ejecutar(tabla, arbol)).getColumnas());
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
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

                if (operando1Valor.size() == operando2Valor.size()
                        || (operando1Valor.size() == 1 & operando2Valor.size() > 1)
                        || (operando2Valor.size() == 1 & operando1Valor.size() > 1)) {
                    LinkedList result = new LinkedList();
                    if (operando1TipoDato.equals(TipoDato.INTEGER) & operando2TipoDato.equals(TipoDato.INTEGER)) {
                        this.tipo = new Tipo(TipoDato.INTEGER);
                        if (operando1Valor.size() == operando2Valor.size()) {
                            for (int i = 0; i < operando1Valor.size(); i++) {
                                result.add(Integer.valueOf(operando1Valor.get(i).toString())
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

                    if (operando1.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, ((Matriz)operando1.ejecutar(tabla, arbol)).getFilas(),
                                ((Matriz)operando1.ejecutar(tabla, arbol)).getColumnas());
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
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

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

                    if (operando1.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, ((Matriz)operando1.ejecutar(tabla, arbol)).getFilas(),
                                ((Matriz)operando1.ejecutar(tabla, arbol)).getColumnas());
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
            if ( (operando1TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)
                    & operando2TipoEstructura.equals(Tipo.TipoEstructura.VECTOR)) ||
                    (operando1TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ)
                            & operando2TipoEstructura.equals(Tipo.TipoEstructura.MATRIZ))) {
                LinkedList operando1Valor = (LinkedList) operando1.ejecutar(tabla, arbol);
                LinkedList operando2Valor = (LinkedList) operando2.ejecutar(tabla, arbol);

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

                    if (operando1.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
                        this.tipo.setTipoEstructura(Tipo.TipoEstructura.MATRIZ);
                        return new Matriz(result, ((Matriz)operando1.ejecutar(tabla, arbol)).getFilas(),
                                ((Matriz)operando1.ejecutar(tabla, arbol)).getColumnas());
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
        }

//            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
//                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
//                this.tipo = new Tipo(TipoDato.INTEGER);
//                return (double) izquierdo + (double) derecho;
//            } else if (operando1.tipo.getTipoDato() == TipoDato.STRING
//                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER
//                    || operando1.tipo.getTipoDato() == TipoDato.INTEGER
//                    && operando2.tipo.getTipoDato() == TipoDato.STRING
//                    || operando1.tipo.getTipoDato() == TipoDato.STRING
//                    && operando2.tipo.getTipoDato() == TipoDato.STRING) {
//                this.tipo = new Tipo(TipoDato.STRING);
//                return "" + izquierdo + derecho;
//            } else {
//                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador +", fila, columna);
//                arbol.getExcepciones().add(ex);
//                return ex;
//            }
//        } else if (this.operador == OperadorAritmetico.RESTA) {
//            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
//                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
//                this.tipo = new Tipo(TipoDato.INTEGER);
//                return (double) izquierdo - (double) derecho;
//            } else {
//                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador -", fila, columna);
//                arbol.getExcepciones().add(ex);
//                return ex;
//            }
//        } else if (this.operador == OperadorAritmetico.MULTIPLICACION) {
//            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
//                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
//                this.tipo = new Tipo(TipoDato.INTEGER);
//                return (double) izquierdo * (double) derecho;
//            } else {
//                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador *", fila, columna);
//                arbol.getExcepciones().add(ex);
//                return ex;
//            }
//        } else if (this.operador == OperadorAritmetico.DIVISION) {
//            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
//                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
//                if ((double) derecho == 0) {
//                    return new Excepcion("Semántico", "Excepcion aritmetica, division por 0.", fila, columna);
//                }
//                this.tipo = new Tipo(TipoDato.INTEGER);
//                return (double) izquierdo / (double) derecho;
//            } else {
//                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador /", fila, columna);
//                arbol.getExcepciones().add(ex);
//                return ex;
//            }
//        } else if (this.operador == OperadorAritmetico.MENOSUNARIO) {
//            if (operandoU.tipo.getTipoDato() == TipoDato.INTEGER) {
//                this.tipo = new Tipo(TipoDato.INTEGER);
//                return (double) unario * -1;
//            } else {
//                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador - Unario", fila, columna);
//                arbol.getExcepciones().add(ex);
//                return ex;
//            }
//        }
//        return null;
        return null;
    }
}
