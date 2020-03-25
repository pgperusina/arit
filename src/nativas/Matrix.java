package nativas;

import abstracto.AST;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Constantes.MATRIX_PARAMETRO;

public class Matrix extends Funcion {
    public Matrix(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        int count = 1;
        LinkedList<Simbolo> parametros = new LinkedList<>();
        while(tabla.getVariableLocal(MATRIX_PARAMETRO + count) != null) {
            parametros.add(tabla.getVariable(MATRIX_PARAMETRO + count));
            count++;
        }

        Simbolo simboloData = parametros.get(0);
        Simbolo simboloNrow = parametros.get(1);
        Simbolo simboloNcol = parametros.get(2);

        Vector valorData = (Vector)simboloData.getValor();
        int filas = (Integer) ((Vector)simboloNrow.getValor()).getFirst();
        int columnas = (Integer) ((Vector)simboloNcol.getValor()).getFirst();

        /**
         * Creo una nueva estructura de tipo matriz con los datos de 'data' y con
         * la información de filas y columnas
         */
        Matriz result = new Matriz();
        int posicionData = 0;
        for (int c = 0; c < columnas; c++) {
            for (int f = 0; f < filas; f++) {
                /**
                 * Si la posicion actual es menor o igual al tamaño del
                 * vector de data, sigo jalando datos de data, cuando
                 * llegue al tamaño, reinicio posicionData a 0
                 */
                if (posicionData <= valorData.size()-1) {
                    result.add(valorData.get(posicionData++));
                } else {
                    posicionData = 0;
                    result.add(valorData.get(posicionData++));
                }
            }
        }
        result.setFilas(filas);
        result.setColumnas(columnas);
        return new Simbolo(new Tipo(parametros.get(0).getTipo().getTipoDato(),
                Tipo.TipoEstructura.MATRIZ), this.getNombre(), result);

//        if (((Integer)valorData.getFirst() %
//                ((Integer)valorNrow.getFirst() * (Integer)valorNcol.getFirst())) != 0) {
//            return new Excepcion("Semántico","La cantidad de elementos a insertar en la " +
//                    " matriz debe de ser múltiplo o submúltiplo de la cantidad de posiciones de la matriz.",
//                    simboloData.getFila(), simboloData.getColumna());
//        }
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        int count = 1;
        if (!(argumentos.size() == 3)) {
            return new Excepcion("Semántico","La función 'Matrix' debe recibir " +
                    " 3 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }

            if (!(argumento.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
                return new Excepcion("Semántico","La función 'Matrix' solamente acepta" +
                        " argumentos de tipo VECTOR o PRIMITIVOS.",
                        argumento.fila, argumento.columna);
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), MATRIX_PARAMETRO + count++, result);
            result = tabla.setVariableLocal(simbolo);

            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
