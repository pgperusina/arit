package excepciones;

import javafx.scene.control.TextArea;

public class Excepcion {
    private String tipo;
    private String descripcion;
    private int fila;
    private int columna;

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
        consola.setText(consola.getText() + this.toString() + "\n");
    }
}
