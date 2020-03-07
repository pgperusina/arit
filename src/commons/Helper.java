package commons;

import abstracto.AST;
import expresiones.Identificador;
import nativas.*;
import tablasimbolos.Tabla;

import java.util.ArrayList;

import static commons.Constantes.*;

public class Helper {


    public static void agregarFuncionesNativas(Tabla tabla) {
        ArrayList<AST> parametrosPrint = new ArrayList<>();

        parametrosPrint.add(new Identificador(PRINT_PARAMETRO, -1,-1));
        Print nPrint = new Print(PRINT, parametrosPrint, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(nPrint);

        ArrayList<AST> parametrosC = new ArrayList<>();
        Concatenar nC = new Concatenar(C, parametrosC, new ArrayList<AST>(), -1, -1);
        tabla.setFuncion(nC);

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

    }

}
