package nativas;

import abstracto.AST;
import estructuras.Lista;
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

import static utilities.Constantes.TYPEOF_PARAMETRO;

public class TypeOf extends Funcion {

    public TypeOf(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simbolo = tabla.getVariableLocal(TYPEOF_PARAMETRO);
        if (simbolo == null) {
            return new Excepcion("Semántico", "No se ha enviado argumento a la función " +
                    this.nombre + ".", fila, columna);
        }
        String tipo = "";
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
            tipo = "list";
        } else if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.ARREGLO)
                | simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
            if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.OBJETO)) {
                tipo = "list";
            } else {
                tipo = simbolo.getTipo().getTipoDato().toString().toLowerCase();
            }
        }

        return new Simbolo(new Tipo(Tipo.TipoDato.STRING, Tipo.TipoEstructura.VECTOR)
                , simbolo.getIdentificador(), new Vector(Arrays.asList(tipo)));
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 1) {
            return new Excepcion("Semántico","La función 'TypeOf' debe recibir " +
                    " 1 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función 'TypeOf' debe recibir " +
                    " 1 argumentos.",
                    this.fila, this.columna);
        }

        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), TYPEOF_PARAMETRO, result);
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
