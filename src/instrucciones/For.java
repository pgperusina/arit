package instrucciones;

import abstracto.AST;
import estructuras.Arreglo;
import estructuras.Lista;
import estructuras.Matriz;
import estructuras.Vector;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

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

        result = tabla.getVariableLocal(this.identificador);

        if (result == null) {
            tabla.setVariableLocal(new Simbolo(expresion.getTipo(), this.identificador, new LinkedList<>()));
        }

        LinkedList estructura = (LinkedList) expresion.ejecutar(t, arbol);


        for (int i = 0; i < estructura.size(); i++) {
            Object estructuraPos = estructura.get(i);
            t.getVariable(this.identificador).setValor(estructuraPos);
            if (estructuraPos instanceof Vector) {
                Vector es = (Vector)estructuraPos;
                String tipoDato ="";
                if (es.getFirst() instanceof Vector) {
                    tipoDato = ((Vector) es.getFirst()).getFirst().getClass().getSimpleName();
                } else {
                    tipoDato = es.getFirst().getClass().getSimpleName();
                }
                t.getVariable(this.identificador).setTipo(new Tipo(tipoDato, Tipo.TipoEstructura.VECTOR));
            } else if (estructuraPos instanceof Lista) {
                Lista l = (Lista)estructuraPos;
                if (l.getFirst() instanceof Lista) {
                    t.getVariable(this.identificador).setTipo(new Tipo(Tipo.TipoDato.OBJETO, Tipo.TipoEstructura.LISTA));
                } else {
                    Vector es = (Vector)l.getFirst();
                    String tipoDato ="";
                    if (es.getFirst() instanceof Vector) {
                        tipoDato = ((Vector) es.getFirst()).getFirst().getClass().getSimpleName();
                    } else {
                        tipoDato = es.getFirst().getClass().getSimpleName();
                    }
                    t.getVariable(this.identificador).setTipo(new Tipo(tipoDato, Tipo.TipoEstructura.VECTOR));
                }
            } else if (estructuraPos instanceof Matriz) {
                Matriz m = (Matriz)estructuraPos;
                String tipoDato ="";
                if (m.getFirst() instanceof Vector) {
                    tipoDato = ((Vector) m.getFirst()).getFirst().getClass().getSimpleName();
                } else {
                    tipoDato = m.getFirst().getClass().getSimpleName();
                }
                t.getVariable(this.identificador).setTipo(new Tipo(tipoDato, Tipo.TipoEstructura.VECTOR));
            }

            for (AST instruccion : instrucciones) {
                result = instruccion.ejecutar(t, arbol);
                if (result instanceof Return || result instanceof Excepcion) {
                    return result;
                }
                if(result instanceof Break){
                    return null;
                }
                if(result instanceof Continue){
                    break;
                }
                t.getVariable(this.identificador).setValor(estructuraPos);
            }

        }

        return null;
    }
    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1, 10000);
        dotBuilder.append(padre+"->"+identificador+random);
        dotBuilder.append("\n");
        dotBuilder.append(padre+"->"+expresion.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        expresion.crearDotFile(dotBuilder, expresion.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        for (AST instruccion : instrucciones) {
            random = getRandomInRange(1, 10000);
            dotBuilder.append(padre+"->"+instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

            instruccion.crearDotFile(dotBuilder, instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

        }

        return dotBuilder.toString();
    }
}
