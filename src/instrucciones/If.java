package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Utils.getRandomInRange;

public class If extends AST {

    private AST condicion;
    private ArrayList<AST> instruccionesIf;
    private ArrayList<AST> instruccionesElse;

    public If(AST condicion, ArrayList<AST> instruccionesIf, ArrayList<AST> instruccionesElse, int fila, int columna) {
        this.condicion = condicion;
        this.instruccionesIf = instruccionesIf;
        this.instruccionesElse = instruccionesElse;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Object valorCondicion;

        Tabla childTable = new Tabla(tabla);
        valorCondicion = condicion.ejecutar(childTable, arbol);

        if (valorCondicion instanceof Excepcion) {
            return valorCondicion;
        }

        if (!( (this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                || this.condicion.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ))
                & this.condicion.getTipo().getTipoDato() == Tipo.TipoDato.BOOLEAN) ) {
            return new Excepcion("Semántico", "La condición de la sentencia IF " +
                    "debe de ser un Vector o Matriz booleano.", fila, columna);
        }

        Object result;
        if ((Boolean) ((LinkedList)valorCondicion).getFirst()) {
            for (AST instruccionIf : instruccionesIf) {
                result = instruccionIf.ejecutar(childTable, arbol);
                if (result instanceof Return) {
                    return result;
                }
                if (result instanceof Excepcion
                        || result instanceof Break || result instanceof Continue) {
                    return result;
                }
            }
        } else {
            for (AST instruccionElse : instruccionesElse) {
                result = instruccionElse.ejecutar(childTable, arbol);
                if (result instanceof Return || result instanceof Excepcion
                        || result instanceof Break || result instanceof Continue) {
                    return result;
                }
            }
        }

        return null;
    }

    @Override
    public String crearDotFile(StringBuilder dotBuilder, String padre) {
        int random = getRandomInRange(1, 10000);
        dotBuilder.append(padre+"->"+condicion.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        condicion.crearDotFile(dotBuilder, condicion.getClass().getSimpleName()+random);
        dotBuilder.append("\n");
        dotBuilder.append(padre+"->"+"InstruccionesIf"+random);
        dotBuilder.append("\n");
        dotBuilder.append(padre+"->"+"InstruccionesElse"+random);
        dotBuilder.append("\n");
        for (AST instruccion : instruccionesIf) {
            dotBuilder.append("InstruccionesIf"+random+"->"+instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");
            instruccion.crearDotFile(dotBuilder, instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

        }
        for (AST instruccion : instruccionesElse) {
            random = getRandomInRange(1, 10000);
            dotBuilder.append("InstruccionesElse"+random+"->"+instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");
            instruccion.crearDotFile(dotBuilder, instruccion.getClass().getSimpleName()+random);
            dotBuilder.append("\n");

        }

        return dotBuilder.toString();
    }
}
