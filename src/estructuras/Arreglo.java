package estructuras;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import estructuras.Vector;

public class Arreglo extends LinkedList {


    private Vector dimensiones;

    public Vector getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(Vector dimensiones) {
        this.dimensiones = dimensiones;
    }

    public Arreglo() {
        super();
    }

    public Arreglo(List<Object> asList) {
        super(asList);
    }

    public Arreglo(List<Object> asList, Vector dimensiones) {
        super(asList);
        this.dimensiones = dimensiones;
    }

    private void construirListaDimensiones(List dimensiones, LinkedList<LinkedList<Integer>> encabezados) {

        int cantidadDimensionesN = 1;
        for (Object dimension : dimensiones) {
            cantidadDimensionesN *= (Integer)dimension;
        }
        /**
         * Creo una lista de listas con valores iniciales para cada
         * dimension que representará.
         */
        for (int i = 0; i < cantidadDimensionesN; i++) {
            LinkedList a = new LinkedList();

            for(int j=0; j<dimensiones.size(); j++) {
                a.add(1);
            }
            encabezados.add(a); }

        crearDimensiones(dimensiones, cantidadDimensionesN, encabezados);
    }

    private void crearDimensiones(List dimensiones, int cantidadDimensionesN,
                                  LinkedList<LinkedList<Integer>> encabezados) {

        int dimensionPrevia = 1;
        int dimensionActual;
        int factorAumento = 1;
        int counter = 0;
        int counterDimensionActual = 0;
        int valor = 1;
        for (int dims = 0; dims < dimensiones.size(); dims++) {
            if(dims > 0) {
                dimensionActual = (Integer) dimensiones.get(dims);
                dimensionPrevia = (Integer) dimensiones.get(dims-1);
                factorAumento = factorAumento * dimensionPrevia;
            } else {
                dimensionActual = (Integer) dimensiones.get(dims)+1;
                dimensionPrevia = (Integer) dimensiones.get(dims);
                factorAumento = 1;
            }
            counter = 0;
            counterDimensionActual = 0;
            valor = 1;
            for (int d = 0; d < cantidadDimensionesN; d++) {
                /**
                 * Para la primera dimensión las reglas son diferentes
                 */
                if (dims == 0) {
                    if (counter <= dimensionPrevia) {
                        if (counter == dimensionPrevia) {
                            valor = 1;
                            encabezados.get(d).set(dims, valor++);
                            counter = 1;
                            continue;
                        }
                        encabezados.get(d).set(dims, valor++);
                        counter++;
                        continue;
                    }
                }

                if (counter <= factorAumento) {
                    /**
                     * Si el counter es igual al factor de aumento
                     * sumo uno a valor
                     */
                    if (counter == factorAumento) {
                        counterDimensionActual++;
                        /**
                         * Si el contador de dimension actual es igual a la dimension actual
                         * reiniciar contadores, set valor a 1 y set valor a lista
                         */
                        if (counterDimensionActual == dimensionActual) {
                            valor = 1;
                            encabezados.get(d).set(dims, valor);
                            counterDimensionActual = 0;
                            counter = 1;
                            continue;
                        }
                        encabezados.get(d).set(dims, ++valor);
                        counter = 1;
                        continue;
                    }
                    encabezados.get(d).set(dims, valor);
                    counter++;

                }

            }
        }

    }

    private void populateStringBuilder(int filas, int columnas,
                                       LinkedList<LinkedList<Integer>> encabezados, StringBuilder sb) {
        int headerCounter = 0;
        int indice;
        int nextPos = 0;
        Arreglo copy = new Arreglo(this);
        String nombreEstructura;
        int cantidadElementosEstructura;

        for (LinkedList<Integer> encabezado : encabezados) {
            /**
             * Agrego los headers de los índices 3 en adelante
             */
            sb.append(", ,");
            encabezado.forEach(e -> {
                sb.append(" ").append(e.toString()).append(",");
            });
            sb.append("\n");
            sb.append("    ");
            /**
             * Imprimo el encabezado de la matriz (número de columna)
             */
            for (int i = 1; i <= columnas; i++) {
                indice = i;
                sb.append("[," + indice + "]    ");
            }
            sb.append("\n");
            /**
             * Recorro las filas
             */
            indice = 1;

            for (int row = 0; row < filas; row++) {
                nextPos = 0;
                /**
                 * Recorro las columnas
                 */
                for (int col = 0; col < columnas; col++) {
                    indice = row + 1;
                    if (col == 0) {
                        sb.append("[" + indice + ",]  ");
                    }

                    /**
                     * Imprimo la posición indicada para la columna actual
                     */
                    Object val = copy.get(row + nextPos);
                    if (val instanceof Lista) {
                        Lista l = (Lista)val;
                        sb.append("List,"+l.size());
                    } else if( val instanceof Vector) {
                        Vector v = (Vector) val;
                        if (((Vector) val).size() > 1) {
                            sb.append(v.getFirst().getClass().getSimpleName() + "," + v.size());
                        } else {
                            sb.append(v.getFirst());
                        }
                    } else {
                        sb.append(val+"  ");
                    }
                    sb.append("    ");
                    nextPos += filas;
                }
                sb.append("\n");
            }
            sb.append("\n");
            copy.removeRange(0, nextPos);
        }
    }

    private void populateStringBuilder(int filas, int columnas, StringBuilder sb) {
        int headerCounter = 0;
        int indice;
        int nextPos = 0;
        Arreglo copy = new Arreglo(this);

        sb.append("    ");
        /**
         * Imprimo el encabezado de la matriz (número de columna)
         */
        for (int i = 1; i <= columnas; i++) {
            indice = i;
            sb.append("[," + indice + "]  ");
        }
        sb.append("\n");
        /**
         * Recorro las filas
         */
        indice = 1;

        for (int row = 0; row < filas; row++) {
            nextPos = 0;
            /**
             * Recorro las columnas
             */
            for (int col = 0; col < columnas; col++) {
                indice = row + 1;
                if (col == 0) {
                    sb.append("[" + indice + ",]  ");
                }

                /**
                 * Imprimo la posición indicada para la columna actual
                 */
                sb.append(copy.get(row + nextPos) + "     ");
                nextPos += filas;
            }
            sb.append("\n");
        }
        copy.removeRange(0, nextPos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        /**
         * Si las dimensiones son mas de 2, la impresión es
         * mas compleja, de lo contrario será una impresión igual a la
         * de una matriz
         */
        if (dimensiones.size() > 2) {

            List multipleDims = dimensiones.subList(2, dimensiones.size());
            LinkedList<LinkedList<Integer>> dims = new LinkedList<>();

           construirListaDimensiones(multipleDims, dims);
           populateStringBuilder((Integer)dimensiones.get(0), (Integer)dimensiones.get(1), dims, sb);
           return sb.toString();
        } else {
            populateStringBuilder((Integer)dimensiones.get(0), (Integer)dimensiones.get(1), sb);
        }

        return sb.toString();
    }

}
