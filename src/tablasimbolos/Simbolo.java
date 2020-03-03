package tablasimbolos;

public class Simbolo {

    private Tipo tipo;
    private String identificador;
    private Object valor;
    private int fila;
    private int columna;

    public Simbolo(Tipo tipo, String identificador, Object valor) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.valor = valor;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getFila() {
        return this.fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getColumna() {
        return this.columna;
    }
}
