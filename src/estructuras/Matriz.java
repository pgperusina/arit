package estructuras;

import java.util.LinkedList;
import java.util.List;

public class Matriz extends LinkedList {
    private int filas;
    private int columnas;

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public Matriz() {
        super();
    }

    public Matriz(List<Object> asList) {
        super(asList);
    }

    public Matriz(List<Object> asList, int filas, int columnas) {
        super(asList);
        this.filas = filas;
        this.columnas = columnas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int indice;
        double tempDouble;

        sb.append("    ");
        /**
         * Imprimo el encabezado del arreglo (número de columna)
         */
        for (int i = 1; i <= columnas; i++) {
            indice = i;
            sb.append("[,"+ indice +"]  ");
        }
        sb.append("\n");
        /**
         * Recorro las filas
         */
        indice = 1;
        for (int row = 0; row < filas; row++) {
            int nextPos = 0;
            /**
             * Recorro las columnas
             */
            for (int col = 0; col < columnas; col++) {
                indice = row+1;
                if (col == 0) {
                    sb.append("[" + indice + ",]  ");
                }

                /**
                 * Imprimo la posición indicada para la columna actual
                 */
                if (this.get(row + nextPos) instanceof Double) {
                    tempDouble = Math.round((Double)this.get(row + nextPos) * 100.0) / 100.0;
                    sb.append(tempDouble + "     ");
                } else {
                    sb.append(this.get(row + nextPos) + "     ");
                }
                nextPos += filas;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
