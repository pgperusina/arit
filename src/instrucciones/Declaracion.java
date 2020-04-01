package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import static utilities.Utils.getRandomInRange;

public class Declaracion extends AST {

    /**
     * Valores AST graph
     */
    int countPadre = 1;
    int countHijo = 1;

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
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object value = null;
//        if (valor != null) {
        value = this.valor.ejecutar(tabla, arbol);
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
            simbolo.setTipo(this.tipo);
            simbolo.setValor(value);
            return null;
        }
        /**
         * Si símbolo no existe en tabla de símbolos
         * crear nuevo símbolo y agregarlo a la tabla
         */
        Simbolo nuevoSimbolo = new Simbolo(this.tipo, this.identificador, value);
        nuevoSimbolo.setFila(this.fila);
        nuevoSimbolo.setColumna(this.columna);
        Object result = tabla.setVariable(nuevoSimbolo);
        if (result instanceof Excepcion) {
            arbol.getExcepciones().add((Excepcion)result);
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

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1, 10000);
        dotBuilder.append(padre+"->Identificador"+random);
        dotBuilder.append("\n");
        random = getRandomInRange(1, 10000);
        dotBuilder.append(padre+"->"+valor.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        padre = valor.getClass().getSimpleName()+random;

        valor.crearDotFile(dotBuilder, padre);
        dotBuilder.append("\n");

        return dotBuilder.toString();
    }
}
