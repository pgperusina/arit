package instrucciones;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Identificador;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;
import utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static utilities.Utils.definirPrioridadCasteo;

public class For extends AST {

    private String identificador;
    private AST expresion;
    private ArrayList<AST> instrucciones;

    public For(String identificador, AST expresion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.identificador = identificador;
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object result;

        Tabla t = new Tabla(tabla);
        result = expresion.ejecutar(t, arbol);

        if (result instanceof Excepcion) {
            return result;
        }

        if (!(result instanceof Vector || result instanceof Lista
                || result instanceof Matriz ||result instanceof Arreglo)) {
            return new Excepcion("Semántico", "La condición de For debe " +
                    "ser una estructura tipo Vector, Lista, Matriz o Arreglo.", expresion.fila, expresion.columna);
        }

        result = tabla.getVariable(this.identificador);

        if (result == null) {
            t.setVariable(new Simbolo(expresion.getTipo(), this.identificador, new LinkedList<>()));
        }

        LinkedList estructura = (LinkedList) expresion.ejecutar(t, arbol);


        for (int i = 0; i < estructura.size(); i++) {
            Object iterador = estructura.get(i);
            if (!(iterador instanceof LinkedList)) {
                t.getVariable(this.identificador).setValor(new Vector(Arrays.asList(iterador)));
            } else {
                setTipoIterador(t, iterador);
                t.getVariable(this.identificador).setValor(iterador);
            }

            for (AST instruccion : instrucciones) {
                result = instruccion.ejecutar(t, arbol);
                if (result instanceof Return || result instanceof Excepcion) {
                    if (expresion instanceof Identificador) {
                        castearEstructura(arbol, t, estructura);
                    }
                    return result;
                }
                if(result instanceof Break){
                    if (expresion instanceof Identificador) {
                        castearEstructura(arbol, t, estructura);
                    }
                    return null;
                }
                if(result instanceof Continue){
                    if (expresion instanceof Identificador) {
                        castearEstructura(arbol, t, estructura);
                    }
                    break;
                }
                ((LinkedList)estructura).set(i, t.getVariable(this.identificador).getValor());
            }
            if (expresion instanceof Identificador) {
                castearEstructura(arbol, t, estructura);
            }
        }

        return null;
    }

    private void setTipoIterador(Tabla t, Object estructuraPos) {
        if (estructuraPos instanceof Vector) {
            t.getVariable(this.identificador).setTipo(new Tipo(estructuraPos.getClass().getSimpleName()
                    , Tipo.TipoEstructura.VECTOR));
        } else if (estructuraPos instanceof Lista) {
            t.getVariable(this.identificador).setTipo(new Tipo(estructuraPos.getClass().getSimpleName()
                    , Tipo.TipoEstructura.LISTA));
        } else if (estructuraPos instanceof Matriz) {
            t.getVariable(this.identificador).setTipo(new Tipo(estructuraPos.getClass().getSimpleName()
                    , Tipo.TipoEstructura.MATRIZ));
        } else if (estructuraPos instanceof Arreglo) {
            t.getVariable(this.identificador).setTipo(new Tipo(estructuraPos.getClass().getSimpleName()
                    , Tipo.TipoEstructura.ARREGLO));
        }
    }

    private void castearEstructura(Arbol arbol, Tabla t, LinkedList estructura) {
        int prioridadCasteoValor = Utils.definirPrioridadCasteo(t.getVariable(this.identificador)
                , arbol);
        int prioridadCasteoEstructura = definirPrioridadCasteo(t.getVariable(((Identificador) expresion)
                .getIdentificador())
                , arbol);
        if (prioridadCasteoEstructura >= prioridadCasteoValor) {
            castearEstructura(t.getVariable(((Identificador) expresion).getIdentificador())
                    , estructura
                    , prioridadCasteoEstructura);
        } else {
            castearEstructura(t.getVariable(((Identificador) expresion).getIdentificador())
                    , estructura
                    , prioridadCasteoValor);
        }
    }

    private void castearEstructura(Simbolo simbolo, LinkedList estructura, int prioridadCasteo) {
        Object temp;
        if (prioridadCasteo == 3) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.STRING, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                estructura.set(i, String.valueOf(temp));
            }
        } else if (prioridadCasteo == 2) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.NUMERIC, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                if (temp instanceof  Boolean) {
                    estructura.set(i, temp == Boolean.TRUE ? "1.0" : "0.0");
                } else if (temp instanceof Integer){
                    estructura.set(i, Double.valueOf(temp.toString()));
                } else {
                    estructura.set(i, temp);
                }
            }
        } else if (prioridadCasteo == 1) {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.INTEGER, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                if (temp instanceof  Boolean) {
                    estructura.set(i, temp == Boolean.TRUE ? "1" : "0");
                } else {
                    estructura.set(i, temp);
                }
            }
        } else {
            simbolo.setTipo(new Tipo(Tipo.TipoDato.BOOLEAN, simbolo.getTipo().getTipoEstructura()));
            for (int i = 0; i < estructura.size(); i++) {
                temp = estructura.get(i);
                estructura.set(i, temp);
            }
        }
    }
}
