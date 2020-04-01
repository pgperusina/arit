package instrucciones;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Case;
import expresiones.Default;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

public class Switch extends AST {

    private AST condicion;
    private ArrayList<AST> listaCases;

    public Switch(AST condicion, ArrayList<AST> listaCases, int fila, int columna) {
        this.condicion = condicion;
        this.listaCases = listaCases;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Tabla t = new Tabla(tabla);

        Object result = condicion.ejecutar(t, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        /**
         * Verifico que la condición sea un vector
         */
        if (!(result instanceof Vector)) {
            return new Excepcion("Semántico", "La condición de Switch debe " +
                    "ser de tipo Vector", condicion.fila, condicion.columna);
        }

        LinkedList condicionValor = (LinkedList) result;
        Case casoDefault = null;

        for (AST caso : listaCases) {
            /**
             * Ejecuto expresion de case para verificar que no sea excepción
             */
            result = ((Case)caso).getExpresion().ejecutar(t, arbol);
            if (result instanceof Excepcion) {
                return result;
            }

            /**
             * Si el caso es default, lo guardo para después
             * y continuo con el siguiente case
             */
            if (((Case) caso).getExpresion() instanceof Default)
            {
                casoDefault =  (Case)caso;
                continue;
            }

            /**
             * Verifico que la expresión del case sea un vector
             */
            if (!(result instanceof Vector)) {
                return new Excepcion("Semántico", "La expresión de Case debe " +
                        "ser de tipo Vector", condicion.fila, condicion.columna);
            }

            LinkedList caseValorExpresion = (LinkedList) result;


            /**
             * Comparo la condición con cada caso
             */
            if (condicionValor.getFirst().equals(caseValorExpresion.getFirst())) {
                result = caso.ejecutar(t, arbol);
                if (result instanceof Excepcion) {
                    return result;
                }
                if (result instanceof Break) {
                    break;
                }
            }
        }

        /**
         * Si no encuentra match con ningún case
         * ejecuto el caso default
         */
        if (casoDefault != null) {
            result = casoDefault.ejecutar(t, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            if (result instanceof Break) {
                return null;
            }
        }

        return null;
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        for (AST instruccion : listaCases) {
            int random = getRandomInRange(1, 10000);
            dotBuilder.append(padre+"->"+instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

            instruccion.crearDotFile(dotBuilder, instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");
        }

        return dotBuilder.toString();
    }

}
