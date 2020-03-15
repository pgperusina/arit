package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class AsignacionIndiceEstructura extends AST {

    private String identificador;
    private ArrayList<AST> posiciones;
    private Object valor;

    public AsignacionIndiceEstructura(String identificador, ArrayList<AST> posiciones, AST valor, int fila, int columna) {
        this.identificador = identificador;
        this.posiciones = posiciones;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {

        Simbolo simbolo = tabla.getVariable(this.identificador);
        if (simbolo == null) {
            Excepcion ex = new Excepcion("Semántico", "Se está tratando de modificar una posición de una variable " +
                    "que no ha sido definida aún. '" + simbolo.getIdentificador() + "'.", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.ARREGLO)) {
            //todo modificación a arreglo
        } else if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
            // todo modificación a matriz
        } else if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
            // TODO modificación a lista
        } else {
            /**
             * MODIFICACIÓN A VECTORES
             */
            this.tipo = simbolo.getTipo();
            LinkedList valor = (LinkedList) simbolo.getValor();
            for (AST posicion : posiciones) {
                Object resultPosicion = posicion.interpretar(tabla, arbol);
                if (resultPosicion instanceof Excepcion) {
                    return resultPosicion;
                }
                LinkedList r = (LinkedList)resultPosicion;
                if (!(r.get(0) instanceof Integer)) {
                    return new Excepcion("Semántico", "El acceso a la posición de un vector " +
                            "debe ser con índices de tipo INTEGER.", fila, columna);
                }
                if ((Integer)r.getFirst() > (Integer)valor.size() ||
                        (Integer)r.getFirst() < 1) {
                    return new Excepcion("Semántico", "Está tratando de acceder a una posición fuera " +
                            "de rango en el VECTOR '" + simbolo.getIdentificador() + "'.", fila, columna);
//
                }
                valor = new LinkedList(Arrays.asList(valor.get((Integer)r.getFirst()-1)));
            }
            return valor;
        }
        return null;
    }
}
