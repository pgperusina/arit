package nativas;

import abstracto.AST;
import estructuras.Lista;
import excepciones.Excepcion;
import expresiones.Funcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Constantes.LIST_PARAMETRO;

public class List extends Funcion {

    public List(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
        this.tipo = new Tipo(Tipo.TipoDato.OBJETO, Tipo.TipoEstructura.LISTA);
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        int count = 1;
        int prioridad = 0;

        LinkedList<Simbolo> parametros = new LinkedList<>();
        while(tabla.getVariable(LIST_PARAMETRO + count) != null) {
            parametros.add(tabla.getVariable(LIST_PARAMETRO + count));
            count++;
        }
        if (parametros.size() == 0) {
            Excepcion ex = new Excepcion("Semántico", "No se envió ningún argumento " +
                    "a la función 'List'.", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }

        Lista lista = new Lista();

        for (Simbolo simboloParametro : parametros) {
            lista.add(simboloParametro.getValor());
        }

        System.out.println(lista);

        return new Simbolo(this.tipo, this.getNombre(), lista);
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Tabla t = new Tabla(tabla);
        Object result;
        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.interpretar(t, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            if (!((argumento.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))
                    | (argumento.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)))) {
                Excepcion ex = new Excepcion("Semántico","La función 'List' solamente acepta" +
                        " argumentos de tipo LISTA, VECTOR o PRIMITIVOS.",
                        argumento.fila, argumento.columna);
                arbol.getExcepciones().add(ex);
                return ex;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), LIST_PARAMETRO + count++, result);
            result = tabla.setVariable(simbolo);

            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
