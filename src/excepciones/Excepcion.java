package excepciones;

import javafx.scene.control.TextArea;

public class Excepcion {
    private String tipo;
    private String descripcion;
    private int fila;
    private int columna;

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getFila() {
        return this.fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return this.columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public Excepcion(String tipo, String descripcion, int fila, int columna) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public String toString() {
        return tipo + " **** " + descripcion + " [Fila: " + fila + ", Columna: " + columna + "]";
    }

    public void print(TextArea consola){
        System.out.println(this.toString() + "\n");
        consola.setText(consola.getText() + this.toString() + "\n");
    }
}
