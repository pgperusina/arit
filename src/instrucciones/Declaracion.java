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
        if (valor != null) {
            value = this.valor.interpretar(tabla, tree);
            if (value instanceof Excepcion) {
                return value;
            }

            // Definir tipo de ID basado en tipo de valor.
            this.tipo.setTipo(this.valor.tipo.getTipo());
            this.tipo.setTipoEstructura(this.valor.getTipo().getTipoEstructura());


        } else if (valorExplicito != null) {
            value = valorExplicito;
        }
        Simbolo simbolo = new Simbolo(this.tipo, this.identificador, value);
        Object result = tabla.setVariable(simbolo);
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
