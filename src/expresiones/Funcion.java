package expresiones;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import instrucciones.Return;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

public class Funcion extends AST {
    protected String nombre;
    protected ArrayList<AST> parametros;
    private ArrayList<AST> instrucciones;
    private boolean nativa;

    public Funcion(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        this.nombre = nombre;
        this.parametros = parametros;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    public Funcion(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, boolean nativa, int fila, int columna) {
        this.nombre = nombre;
        this.parametros = parametros;
        this.instrucciones = instrucciones;
        this.nativa = nativa;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {
        for (AST instruccion : instrucciones) {
            Object result = instruccion.interpretar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            if (result instanceof Return) {
                Return r = (Return) result;
                if (r.getExpresion() == null) {
                    return null;
                }
                result = r.getExpresion().interpretar(tabla, arbol);
                String tipoDatoExpresion = ((LinkedList)result).getFirst().getClass()
                        .getSimpleName().toLowerCase();
                Tipo.TipoEstructura tipoEstructuraExpresion = getTipoEstructura(result, arbol);
                this.tipo = new Tipo(tipoDatoExpresion, tipoEstructuraExpresion);
                return result;
            }
        }
        return null;
    }

    private Tipo.TipoEstructura getTipoEstructura(Object estructura, Arbol arbol) {
        LinkedList e = (LinkedList) estructura;
        if (e instanceof Arreglo) {
            return Tipo.TipoEstructura.ARREGLO;
        } else if (e instanceof Matriz) {
            return Tipo.TipoEstructura.MATRIZ;
        } else if (e instanceof Lista) {
            return Tipo.TipoEstructura.LISTA;
        } else if (e instanceof Vector) {
            return Tipo.TipoEstructura.VECTOR;
        } else {
            Excepcion ex = new Excepcion("Semántico", "El tipo de la expresión retornada " +
                    "no existe en los tipos aceptados por el lenguaje",
                    this.fila, this.columna);
            arbol.getExcepciones().add(ex);
            return null;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<AST> getParametros() {
        return parametros;
    }

    public boolean isNativa() {
        return this.nativa;
    }

    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        return null;
    }
}
