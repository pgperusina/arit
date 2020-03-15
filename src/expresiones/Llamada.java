package expresiones;

import abstracto.AST;
import excepciones.Excepcion;
import instrucciones.Declaracion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;

import java.util.ArrayList;

public class Llamada extends AST {

    private String nombre;
    private ArrayList<AST> argumentos;

    public Llamada(String nombre, ArrayList<AST> argumentos, int fila, int columna) {
        this.nombre = nombre;
        this.argumentos = argumentos;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        Object result = tabla.getFuncion(nombre);
        if (result == null) {
            Excepcion ex = new Excepcion("Semántico", "No se ha encontrado la función '" + this.nombre +"'", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }

        Funcion funcion = (Funcion) result;
        /**
         * Si es función nativa, solamente verificar que los argumentos
         * esten correctos, luego interpretar función.
         */
        if (funcion.isNativa()){
            Tabla t = new Tabla(tabla);
//            for (AST argumento : this.argumentos) {
//                result = argumento.interpretar(t, arbol);
//                if (result instanceof Excepcion) {
//                    return result;
//                }
//            }
            result = funcion.cargarTabla(t, arbol, this.argumentos);
            if (result instanceof Excepcion) {
                return result;
            }
            if (!(funcion.getNombre().equalsIgnoreCase("print"))) {
                Object o = funcion.interpretar(t, arbol);
                if (o instanceof Simbolo) {
                    this.tipo = ((Simbolo) o).getTipo();
                    return ((Simbolo) o).getValor();
                } else {
                    Excepcion ex = new Excepcion("Semántico", "Error ejecutando la función 'C'.", fila, columna);
                    arbol.getExcepciones().add(ex);
                    return ex;
                }
            }
            return funcion.interpretar(t, arbol);
        }

        /**
         * Si no es nativa, verificar cantidad de argumentos, si están correctos
         * e interpretar función.
         */
        if (funcion.getParametros().size() == this.argumentos.size()) {
//            Tabla t = new Tabla(arbol.getTablaGlobal());
            Tabla t = new Tabla(tabla);
            for (int i = 0; i < funcion.getParametros().size(); i++) {
                result = this.argumentos.get(i).interpretar(tabla, arbol);
                Object parametro = funcion.getParametros().get(i);
                if (result instanceof Excepcion) {
                    return result;
                }
                if (result instanceof ArgumentoDefault) {
                    if (parametro instanceof Declaracion) {
                        result = ((Declaracion) parametro).interpretar(t, arbol);
                    } else {
                        Excepcion ex = new Excepcion("Semántico", "El parámetro "
                                + ((Identificador) parametro).getIdentificador()
                                + " de la función no tiene valor 'default'.", fila, columna);
                        arbol.getExcepciones().add(ex);
                        return ex;
                    }
                } else {
                    if (parametro instanceof Declaracion) {
                        Simbolo simbolo = new Simbolo(this.argumentos.get(i).getTipo(),
                                ((Declaracion) parametro).getIdentificador(), result);
                        result = t.setVariable(simbolo);
                    } else {
                        Simbolo simbolo = new Simbolo(this.argumentos.get(i).getTipo(),
                                ((Identificador) parametro).getIdentificador(), result);
                        result = t.setVariable(simbolo);
                    }
                }
                if (result != null) {
                    return result;
                }

//                if (funcion.getParametros().get(i) instanceof Identificador) {
//                    Identificador parametro = (Identificador) funcion.getParametros().get(i);
//                    if (result instanceof  Default) {
//                        Excepcion ex = new Excepcion("Semántico", "El argumento " + parametro.getIdentificador() + " " +
//                                "de la función no tiene valor 'default'.", fila, columna);
//                        arbol.getExcepciones().add(ex);
//                        return ex;
//                    }
//                    Simbolo simbolo = new Simbolo(parametro.getTipo(), parametro.getIdentificador(), parametro);
//                    result = t.setVariable(simbolo);
//                } else {
//                    Declaracion parametro = (Declaracion) funcion.getParametros().get(i);
//                    if (result instanceof Default) {
//                        result = parametro.getValor();
//                    }
//                    Simbolo simbolo = new Simbolo(parametro.getTipo(), parametro.getIdentificador(), result);
//                    result = t.setVariable(simbolo);
//                }

            }
            return funcion.interpretar(t, arbol);
        } else {
            Excepcion ex = new Excepcion("Semántico", "La cantidad de argumentos "
                    + "enviados (" + this.argumentos.size() + ") no coincide con la cantidad de parámetros "
                    + "de la función (" + funcion.getParametros().size() + ")." + this.nombre, fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }
    }
}
