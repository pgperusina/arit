package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

public class Declaracion extends AST {

    private String identificador;
    private AST valor;
    private Object valorExplicito;

    public Declaracion(String identificador, AST valor, int fila, int columna) {
        this.identificador = identificador;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol tree) {
        Object value = null;
//        if (valor != null) {
        value = this.valor.interpretar(tabla, tree);
        if (value instanceof Excepcion) {
            return value;
        }

        // Definir tipo de ID basado en tipo de valor.
        this.tipo = this.valor.getTipo();

//        } else if (valorExplicito != null) {
//            value = valorExplicito;
//        }
        /**
         * Si identificador ya existe en tabla de símbolos
         * solamente asignar nuevo valor
         */
        Simbolo simbolo = tabla.getVariable(this.identificador);
        if (simbolo != null) {
            simbolo.setValor(value);
            return null;
        }
        /**
         * Si símbolo no existe en tabla de símbolos
         * crear nuevo símbolo y agregarlo a la tabla
         */
        Simbolo nuevoSimbolo = new Simbolo(this.tipo, this.identificador, value);
        Object result = tabla.setVariable(nuevoSimbolo);
        if (result instanceof Excepcion) {
            tree.getExcepciones().add((Excepcion)result);
            return result;
        }
        return null;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setValor(AST valor) {
        this.valor = valor;
    }

    public AST getValor() {
        return this.valor;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setValorExplicito(Object valorExplicito) {
        this.valorExplicito = valorExplicito;
    }

    private Tipo setTipo() {
        return null;
    }
}
