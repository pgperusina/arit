package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

public class Asignacion extends AST {

    private String identificador;
    private AST expresionAcceso;
    private AST valor;
    private Object valorExplicito;

    public Asignacion(String identificador, AST expresionAcceso, AST valor, int fila, int columna) {
        this.identificador = identificador;
        this.expresionAcceso = expresionAcceso;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        Simbolo id;
        Object acceso;
        if (tabla.getVariable(identificador) == null) {
            return new Excepcion("Semántico","La variable con el identificador"
                    + identificador + " no ha sido definida.",
                    this.fila, this.columna);
        }

        if (expresionAcceso != null) {
            acceso = expresionAcceso.interpretar(tabla, tree);
            if (acceso instanceof Excepcion) {
                return acceso;
            }


        }

        return null;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setValor(AST valor) {
        this.valor = valor;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setValorExplicito(Object valorExplicito) {
        this.valorExplicito = valorExplicito;
    }
}
