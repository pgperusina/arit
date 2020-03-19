package utilities;

import abstracto.AST;
import excepciones.Excepcion;
import expresiones.Identificador;
import nativas.*;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Constantes.*;

public class Utils {


    public static void agregarFuncionesNativas(Tabla tabla) {
        ArrayList<AST> parametrosPrint = new ArrayList<>();

        parametrosPrint.add(new Identificador(PRINT_PARAMETRO, -1,-1));
        Print nPrint = new Print(PRINT, parametrosPrint, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(nPrint);

        ArrayList<AST> parametrosC = new ArrayList<>();
        Concatenar nC = new Concatenar(C, parametrosC, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(nC);

        ArrayList<AST> parametrosList = new ArrayList<>();
        List nList = new List(LIST, parametrosList, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(nList);

        ArrayList<AST> parametrosTypeOf = new ArrayList<>();
        parametrosTypeOf.add(new Identificador(TYPEOF_PARAMETRO, -1, -1));
        TypeOf nTypeOf = new TypeOf(TYPEOF, parametrosTypeOf, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(nTypeOf);

        ArrayList<AST> parametrosLength = new ArrayList<>();
        parametrosLength.add(new Identificador(LENGTH_PARAMETRO, -1, -1));
        Length length = new Length(LENGTH, parametrosLength, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(length);

        ArrayList<AST> parametrosNcol = new ArrayList<>();
        parametrosNcol.add(new Identificador(NCOL_PARAMETRO, -1, -1));
        Ncol nNcol = new Ncol(NCOL, parametrosNcol, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nNcol);

        ArrayList<AST> parametrosNrow = new ArrayList<>();
        parametrosNrow.add(new Identificador(NROW_PARAMETRO, -1, -1));
        Nrow nNrow = new Nrow(NROW, parametrosNrow, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nNrow);

        ArrayList<AST> parametrosStringLength = new ArrayList<>();
        parametrosStringLength.add(new Identificador(STRING_LENGTH_PARAMETRO, -1, -1));
        StringLength nSlength = new StringLength(STRING_LENGTH, parametrosStringLength, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nSlength);

        ArrayList<AST> parametrosRemove = new ArrayList<>();
        parametrosRemove.add(new Identificador(REMOVE_PARAMETRO_1, -1, -1));
        parametrosRemove.add(new Identificador(REMOVE_PARAMETRO_2, -1, -1));
        Remove nRemove = new Remove(REMOVE, parametrosRemove, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nRemove);

        ArrayList<AST> parametrosToLowerCase = new ArrayList<>();
        parametrosToLowerCase.add(new Identificador(TO_LOWER_CASE_PARAMETRO, -1, -1));
        ToLowerCase nToLowerCase = new ToLowerCase(TO_LOWER_CASE, parametrosToLowerCase, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nToLowerCase);

        ArrayList<AST> parametrosToUpperCase = new ArrayList<>();
        parametrosToUpperCase.add(new Identificador(TO_UPPER_CASE_PARAMETRO, -1, -1));
        ToUpperCase nToUpperCase = new ToUpperCase(TO_UPPER_CASE, parametrosToUpperCase, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nToUpperCase);

        ArrayList<AST> parametrosTrunk = new ArrayList<>();
        parametrosTrunk.add(new Identificador(TRUNK_PARAMETRO, -1, -1));
        Trunk nTrunk = new Trunk(TRUNK, parametrosTrunk, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nTrunk);

        ArrayList<AST> parametrosRound = new ArrayList<>();
        parametrosRound.add(new Identificador(ROUND_PARAMETRO, -1, -1));
        Round nRound = new Round(ROUND, parametrosRound, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nRound);

        ArrayList<AST> parametrosMean = new ArrayList<>();
        parametrosMean.add(new Identificador(MEAN_PARAMETRO, -1, -1));
        Mean nMean = new Mean(MEAN, parametrosMean, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nMean);

        ArrayList<AST> parametrosMedian = new ArrayList<>();
        parametrosMedian.add(new Identificador(MEDIAN_PARAMETRO, -1, -1));
        Median nMedian = new Median(MEDIAN, parametrosMedian, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nMedian);

        ArrayList<AST> parametrosMode = new ArrayList<>();
        parametrosMode.add(new Identificador(MODE_PARAMETRO, -1, -1));
        Mode nMode = new Mode(MODE, parametrosMode, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nMode);

        ArrayList<AST> parametrosMatrix = new ArrayList<>();
        parametrosMatrix.add(new Identificador(MATRIX_PARAMETRO + 1, -1, -1));
        parametrosMatrix.add(new Identificador(MATRIX_PARAMETRO + 2, -1, -1));
        parametrosMatrix.add(new Identificador(MATRIX_PARAMETRO + 3, -1, -1));
        Matrix nMatrix = new Matrix(MATRIX, parametrosMatrix, new ArrayList<>(), -1, -1);
        tabla.setFuncion(nMatrix);

    }

    public static int definirPrioridadCasteo(LinkedList<Simbolo> parametros, Arbol arbol) {
        int prioridad = 0;
        for (Simbolo p : parametros) {
            if (p.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
                prioridad = 4;

            } else if (p.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)) {
                if (prioridad > 3) continue;
                if (p.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING)) {
                    prioridad = 3;
                } else if (p.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)) {
                    if (prioridad > 2) continue;
                    prioridad = 2;
                } else if (p.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER)) {
                    if (prioridad > 1) continue;
                    prioridad = 1;
                } else if (p.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                    if (prioridad > 0) continue;
                    prioridad = 0;
                }
            } else {
                Excepcion ex = new Excepcion("Semántico", "Error de casteo.", p.getFila(), p.getColumna());
                arbol.getExcepciones().add(ex);
            }
        }
        return prioridad;
    }

    public static int definirPrioridadCasteo(Simbolo simbolo, Arbol arbol) {
        int prioridad = 0;
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
            prioridad = 4;

        } else if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)) {
            if (prioridad < 4) {
                if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING)) {
                    prioridad = 3;
                } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)) {
                    if (prioridad < 3) {
                        prioridad = 2;
                    } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER)) {
                        if (prioridad < 2) {
                            prioridad = 1;
                        } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                            if (prioridad < 1) {
                                prioridad = 0;
                            }
                        }
                    }
                }
            }
        } else {
            Excepcion ex = new Excepcion("Semántico", "Error de casteo.", simbolo.getFila(), simbolo.getColumna());
            arbol.getExcepciones().add(ex);
        }
        return prioridad;
    }

    public static int definirPrioridadCasteo(AST valor, Arbol arbol) {
        int prioridad = 0;
        if (valor.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
            prioridad = 4;

        } else if (valor.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)) {
            if (prioridad < 4) {
                if (valor.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING)) {
                    prioridad = 3;
                } else if (valor.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)) {
                    if (prioridad < 3) {
                        prioridad = 2;
                    }
                } else if (valor.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER)) {
                    if (prioridad < 2) {
                        prioridad = 1;
                    }
                } else if (valor.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                    if (prioridad < 1) {
                        prioridad = 0;
                    }
                }
            }
        } else {
            Excepcion ex = new Excepcion("Semántico", "Error de casteo.", valor.fila, valor.columna);
            arbol.getExcepciones().add(ex);
        }
        return prioridad;
    }

    public static Tipo definirTipoRetorno(int prioridad) {
        switch(prioridad) {
            case 0:
                return new Tipo(Tipo.TipoDato.BOOLEAN, Tipo.TipoEstructura.VECTOR);
            case 1:
                return new Tipo(Tipo.TipoDato.INTEGER, Tipo.TipoEstructura.VECTOR);
            case 2:
                return new Tipo(Tipo.TipoDato.NUMERIC, Tipo.TipoEstructura.VECTOR);
            case 3:
                return new Tipo(Tipo.TipoDato.STRING, Tipo.TipoEstructura.VECTOR);
            case 4:
                return new Tipo(Tipo.TipoDato.OBJETO, Tipo.TipoEstructura.LISTA);
        }
        return null;
    }

}
