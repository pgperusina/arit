package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static utilities.Constantes.MEAN_PARAMETRO;
import static utilities.Constantes.MODE_PARAMETRO;

public class Mode extends Funcion {

    public Mode(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {

        Simbolo datos = tabla.getVariable(MODE_PARAMETRO+1);
        Simbolo trim = tabla.getVariable(MODE_PARAMETRO+2);
        if (datos == null) {
            return new Excepcion("Semántico", "No se han enviado datos a la función " +
                    this.nombre.toUpperCase() + ".", fila, columna);
        }
        if (!(datos.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
            return new Excepcion("Semántico", "El argumento de datos enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Vector.", trim.getFila(), trim.getColumna());
        }
        if (!(datos.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)
                | datos.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER))) {
            return new Excepcion("Semántico", "El argumento de datos enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Numeric o Integer.", trim.getFila(), trim.getColumna());
        }
        if (trim != null) {
            if (((Vector) trim.getValor()).size() > 1) {
                return new Excepcion("Semántico", "El argumento 'trim' enviado a la función " +
                        this.nombre.toUpperCase() + " debe tener una sola posición.", trim.getFila(), trim.getColumna());
            }
            if (!(trim.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)
                    | trim.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER))) {
                return new Excepcion("Semántico", "El argumento 'trim' enviado a la funcion " +
                        this.nombre.toUpperCase() + " debe de ser de tipo Numeric o Integer.", trim.getFila(), trim.getColumna());
            }
        }

        List listaDatos = (LinkedList)datos.getValor();
        Double mode = 0.0;
        int n = 0;
        if (trim != null) {
            int trimValue = (int) ((LinkedList) trim.getValor()).getFirst();
            for (Object listaDato : listaDatos) {
                if (Double.valueOf(listaDato.toString()) >= trimValue) {
                    mode += Double.valueOf(listaDato.toString());
                    n++;
                }
            }
        } else {
            for (Object listaDato : listaDatos) {
                mode += Double.valueOf(listaDato.toString());
                n++;
            }
        }
        mode = mode / n;

        return new Simbolo(new Tipo(Tipo.TipoDato.NUMERIC, Tipo.TipoEstructura.VECTOR)
                , this.nombre, new Vector(Arrays.asList(mode)));
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 2) {
            return new Excepcion("Semántico","La función '"+ this.nombre +"' debe recibir " +
                    " 1 o 2 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 1 o 2 argumentos.",
                    this.fila, this.columna);
        }

        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), MODE_PARAMETRO + count++, result);
            simbolo.setFila(argumento.fila);
            simbolo.setColumna(argumento.columna);
            result = tabla.setVariableLocal(simbolo);

            /**
             * Si el retorno de setVariable no es nulo
             * devuelvo result ya que es una excepción.
             */
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
