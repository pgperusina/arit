package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;
import tablasimbolos.Tipo.TipoDato;

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
    public Object interpretar(Tabla tabla, Arbol tree) {
        Object izquierdo = null, derecho = null, unario = null;

        if (this.operandoU == null) {
            izquierdo = this.operando1.interpretar(tabla, tree);
            if (izquierdo instanceof Excepcion) return izquierdo;

            derecho = this.operando2.interpretar(tabla, tree);
            if (derecho instanceof Excepcion) return derecho;
        } else {
            unario = this.operandoU.interpretar(tabla, tree);
            if (unario instanceof Excepcion) return unario;
        }

        if (this.operador == OperadorAritmetico.SUMA) {
            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
                this.tipo = new Tipo(TipoDato.INTEGER);
                return (double) izquierdo + (double) derecho;
            } else if (operando1.tipo.getTipoDato() == TipoDato.STRING
                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER
                    || operando1.tipo.getTipoDato() == TipoDato.INTEGER
                    && operando2.tipo.getTipoDato() == TipoDato.STRING
                    || operando1.tipo.getTipoDato() == TipoDato.STRING
                    && operando2.tipo.getTipoDato() == TipoDato.STRING) {
                this.tipo = new Tipo(TipoDato.STRING);
                return "" + izquierdo + derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador +", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorAritmetico.RESTA) {
            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
                this.tipo = new Tipo(TipoDato.INTEGER);
                return (double) izquierdo - (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador -", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorAritmetico.MULTIPLICACION) {
            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
                this.tipo = new Tipo(TipoDato.INTEGER);
                return (double) izquierdo * (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador *", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorAritmetico.DIVISION) {
            if (operando1.tipo.getTipoDato() == TipoDato.INTEGER
                    && operando2.tipo.getTipoDato() == TipoDato.INTEGER) {
                if ((double) derecho == 0) {
                    return new Excepcion("Semántico", "Excepcion aritmetica, division por 0.", fila, columna);
                }
                this.tipo = new Tipo(TipoDato.INTEGER);
                return (double) izquierdo / (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador /", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorAritmetico.MENOSUNARIO) {
            if (operandoU.tipo.getTipoDato() == TipoDato.INTEGER) {
                this.tipo = new Tipo(TipoDato.INTEGER);
                return (double) unario * -1;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador - Unario", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        }
        return null;
    }
}
