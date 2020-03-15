package nativas;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Constantes.C_PARAMETRO;
import static utilities.Utils.definirPrioridadCasteo;
import static utilities.Utils.definirTipoRetorno;

public class Concatenar extends Funcion {

    public Concatenar(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        int count = 1;
        int prioridad = 0;

        LinkedList<Simbolo> parametros = new LinkedList<>();
        while(tabla.getVariable(C_PARAMETRO + count) != null) {
            parametros.add(tabla.getVariable(C_PARAMETRO + count));
            count++;
        }
        if (parametros.size() == 0) {
            Excepcion ex = new Excepcion("Semántico", "No se envió ningún argumento " +
                    "a la función 'C'.", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }

        /**
         * Definiendo prioridad de casteo
         */
        prioridad = definirPrioridadCasteo(parametros, arbol);
        this.tipo = definirTipoRetorno(prioridad);
        if (this.tipo == null) {
            Excepcion ex = new Excepcion("Semántico","Error definiendo el tipo del Vector.",
                    this.fila, this.columna);
            arbol.getExcepciones().add(ex);
        }

        LinkedList result = new LinkedList();
        for (Simbolo simboloParametro : parametros) {

            if (prioridad == 4) {
                // todo - es cuando hay una lista en los parametros
                // todo pasa a ser lista
            } else if(prioridad == 3) {
                ((LinkedList)simboloParametro.getValor()).forEach(v -> {
                    result.add(String.valueOf(v));
                });
            } else if (prioridad == 2) {
                ((LinkedList)simboloParametro.getValor()).forEach(v -> {
                    if (v instanceof  Boolean) {
                        result.add(v == Boolean.TRUE ? "1.0" : "0.0");
                    } else if (v instanceof Integer){
                        result.add(Integer.valueOf(v.toString()));
                    } else {
                        result.add(v);
                    }
                });
            }  else if (prioridad == 1) {
                ((LinkedList)simboloParametro.getValor()).forEach(v -> {
                    if (v instanceof  Boolean) {
                        result.add(v == Boolean.TRUE ? "1" : "0");
                    } else {
                        result.add(v);
                    }
                });
            } else if (prioridad == 0) {
                ((LinkedList)simboloParametro.getValor()).forEach(v -> {
                    result.add(v);
                });
            }
        }

        return new Simbolo(this.tipo, this.getNombre(), result);
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.interpretar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }

            // Todo - validar que argumentos tambien pueden ser listas
            if (!((argumento.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))
                    || (argumento.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)))) {
                Excepcion ex = new Excepcion("Semántico","La función 'C' solamente acepta" +
                        " argumentos de tipo LISTA, VECTOR o PRIMITIVOS.",
                        argumento.fila, argumento.columna);
                arbol.getExcepciones().add(ex);
                return ex;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), C_PARAMETRO + count++, result);
            result = tabla.setVariable(simbolo);

            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
