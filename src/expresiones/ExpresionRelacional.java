package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

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
    public Object interpretar(Tabla tabla, Arbol tree) {
        Object izquierdo = null, derecho = null;

        izquierdo = this.operando1.interpretar(tabla, tree);
        if (izquierdo instanceof Excepcion) return izquierdo;

        derecho = this.operando2.interpretar(tabla, tree);
        if (derecho instanceof Excepcion) return derecho;

        this.tipo = new Tipo(Tipo.TipoDato.BOOLEAN);
        if (this.operador == OperadorRelacional.MENORQUE) {
            if (operando1.tipo.getTipo() == Tipo.TipoDato.INTEGER
                    && operando2.tipo.getTipo() == Tipo.TipoDato.INTEGER) {
                return (double) izquierdo < (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador <", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorRelacional.MAYORQUE) {
            if (operando1.tipo.getTipo() == Tipo.TipoDato.INTEGER
                    && operando2.tipo.getTipo() == Tipo.TipoDato.INTEGER) {
                return (double) izquierdo > (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador >", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorRelacional.MAYORIGUAL) {
            if (operando1.tipo.getTipo() == Tipo.TipoDato.INTEGER
                    && operando2.tipo.getTipo() == Tipo.TipoDato.INTEGER) {
                return (double) izquierdo >= (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador >=", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorRelacional.MENORIGUAL) {
            if (operando1.tipo.getTipo() == Tipo.TipoDato.INTEGER
                    && operando2.tipo.getTipo() == Tipo.TipoDato.INTEGER) {
                return (double) izquierdo <= (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador <=", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorRelacional.IGUALA) {
            if (operando1.tipo.getTipo() == Tipo.TipoDato.INTEGER
                    && operando2.tipo.getTipo() == Tipo.TipoDato.INTEGER) {
                return (double) izquierdo == (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador ==", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorRelacional.DIFERENTEDE) {
            if (operando1.tipo.getTipo() == Tipo.TipoDato.INTEGER
                    && operando2.tipo.getTipo() == Tipo.TipoDato.INTEGER) {
                return (double) izquierdo != (double) derecho;
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de tipos con el operador !=", fila, columna);
                tree.getExcepciones().add(ex);
                return ex;
            }
        }
        return null;
    }
}
