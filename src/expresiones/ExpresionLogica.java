package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

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
        if (this.operador == OperadorLogico.AND) {
            if (operando1.tipo.getTipoDato() == Tipo.TipoDato.BOOLEAN
                    && operando2.tipo.getTipoDato() == Tipo.TipoDato.BOOLEAN) {
                return (boolean) izquierdo && (boolean) derecho;
            } else {
                Excepcion ex = new Excepcion("Semantico", "Error de tipos con el operador AND", fila, columna);
                arbol.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorLogico.OR) {
            if (operando1.tipo.getTipoDato() == Tipo.TipoDato.BOOLEAN
                    && operando2.tipo.getTipoDato() == Tipo.TipoDato.BOOLEAN) {
                return (boolean) izquierdo || (boolean) derecho;
            } else {
                Excepcion ex = new Excepcion("Semantico", "Error de tipos con el operador OR", fila, columna);
                arbol.getExcepciones().add(ex);
                return ex;
            }
        } else if (this.operador == OperadorLogico.NOT) {
            if (operandoU.tipo.getTipoDato() == Tipo.TipoDato.BOOLEAN) {
                return !((boolean) unario);
            } else {
                Excepcion ex = new Excepcion("Semantico", "Error de tipos con el operador NOT", fila, columna);
                arbol.getExcepciones().add(ex);
                return ex;
            }
        }
        return null;
    }
}
