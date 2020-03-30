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

import static utilities.Constantes.*;

public class Remove extends Funcion {

    public Remove(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {

        int count = 1;
        LinkedList<Simbolo> parametros = new LinkedList<>();
        while(tabla.getVariableLocal(REMOVE_PARAMETRO + count) != null) {
            parametros.add(tabla.getVariableLocal(REMOVE_PARAMETRO + count++));
        }
        if (parametros.size() != 2) {
            return new Excepcion("Semántico", "No se han enviado argumentos a la función " +
                    this.nombre + ".", fila, columna);
        }
        Simbolo param1 = parametros.getFirst();
        Simbolo param2 = parametros.get(1);

        if (!(param1.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                & param2.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
            return new Excepcion("Semántico", "Los argumentos enviados a la funcion " +
                    this.nombre.toUpperCase() + " deben de ser de tipo Vector o Primitivo.", param1.getFila(), param1.getColumna());
        }
        if (!(param1.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING)
                & param2.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING)) ) {
            return new Excepcion("Semántico", "Los argumentos enviados a la funcion " +
                    this.nombre.toUpperCase() + " deben de ser de tipo Cadena.", param1.getFila(), param1.getColumna());
        }
        if ( ((Vector)param1.getValor()).size() > 1 | ((Vector)param2.getValor()).size() > 1) {
            return new Excepcion("Semántico", "Los Vectores de tipo cadena enviados a la función " +
                    this.nombre.toUpperCase() + " deben tener una sola posición.", param1.getFila(), param1.getColumna());
        }
        String cadenaUno = ((Vector)param1.getValor()).getFirst().toString();
        String cadenaDos = ((Vector)param2.getValor()).getFirst().toString();
        String resultado = cadenaUno.replace(cadenaDos, "");

        return new Simbolo(new Tipo(Tipo.TipoDato.STRING, Tipo.TipoEstructura.VECTOR)
                , this.nombre, new Vector(Arrays.asList(resultado)));
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 2) {
            return new Excepcion("Semántico","La función '"+ this.nombre +"' debe recibir " +
                    " 2 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 2 argumentos.",
                    this.fila, this.columna);
        }

        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), REMOVE_PARAMETRO + count++, result);
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
