package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import javafx.scene.chart.*;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;
import java.util.LinkedList;

import static utilities.Constantes.BARRAS_PARAMETRO;
import static utilities.Constantes.LINEA_PARAMETRO;

public class Linea extends Funcion {
    public Linea(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simboloDatos = tabla.getVariableLocal(LINEA_PARAMETRO+1);
        Simbolo simboloTipo = tabla.getVariableLocal(LINEA_PARAMETRO+2);
        Simbolo simboloLabelX = tabla.getVariableLocal(LINEA_PARAMETRO+3);
        Simbolo simboloLabelY = tabla.getVariableLocal(LINEA_PARAMETRO+4);
        Simbolo simboloTitulo = tabla.getVariableLocal(LINEA_PARAMETRO+5);
        if (simboloDatos == null | simboloLabelX == null | simboloLabelY == null
                | simboloTipo == null | simboloTitulo == null) {
            return new Excepcion("Semántico", "Faltan argumentos para la función " +
                    this.nombre + ".", fila, columna);
        }
        if (!(simboloDatos.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloDatos.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ))) {
            return new Excepcion("Semántico", "El argumento de de datos de la función " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Vector o Matriz.", simboloDatos.getFila(), simboloDatos.getColumna());
        }
        if (!( simboloLabelX.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloLabelY.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloTitulo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloTipo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
            return new Excepcion("Semántico", "Los argumentos enviados a la función " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Vector.", simboloDatos.getFila(), simboloDatos.getColumna());
        }
        if (!(simboloDatos.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)
                | simboloDatos.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER))) {
            return new Excepcion("Semántico", "El argumento de datos enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Numeric o Integer.", simboloDatos.getFila(), simboloDatos.getColumna());
        }
        if (!(simboloLabelX.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento de XLab enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloLabelX.getFila(), simboloLabelX.getColumna());
        }
        if (!(simboloLabelY.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento de YLab enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloLabelY.getFila(), simboloLabelY.getColumna());
        }
        if (!(simboloTitulo.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento de Main enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloTitulo.getFila(), simboloTitulo.getColumna());
        }
        if (!(simboloTipo.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento Type enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloTipo.getFila(), simboloTipo.getColumna());
        }

        LinkedList datos = (LinkedList)simboloDatos.getValor();
        Vector tipo = (Vector)simboloTipo.getValor();
        Vector labelX = (Vector)simboloLabelX.getValor();
        Vector labelY = (Vector)simboloLabelY.getValor();
        Vector titulo = (Vector)simboloTitulo.getValor();

        if (!(String.valueOf(tipo.getFirst().toString()).equalsIgnoreCase("P")
                | String.valueOf(tipo.getFirst().toString()).equalsIgnoreCase("I")
                | String.valueOf(tipo.getFirst().toString()).equalsIgnoreCase("O")) ) {
            Excepcion ex = new Excepcion("Semántico", "El argumento 'Type' debe de ser 'P', 'I' u 'O' para la función " +
                    this.nombre.toUpperCase() + ".", simboloTipo.getFila(), simboloTipo.getColumna());
            arbol.getExcepciones().add(ex);
            tipo.set(0, "O");

        }

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(String.valueOf(labelX.getFirst().toString()));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(String.valueOf(labelY.getFirst().toString()));

        LineChart lineChart = new LineChart(xAxis, yAxis);

        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName(String.valueOf(titulo.getFirst().toString()));

        for (int i = 0; i < datos.size(); i++) {
            dataSeries.getData().add(new XYChart.Data(i+1
                    , Double.valueOf(datos.get(i).toString())));
        }


        lineChart.getData().add(dataSeries);
        if (String.valueOf(tipo.getFirst().toString()).equalsIgnoreCase("P")) {
            lineChart.getStyleClass().add("P");
        } else if (String.valueOf(tipo.getFirst().toString()).equalsIgnoreCase("I")) {
            lineChart.getStyleClass().add("I");
        } else if (String.valueOf(tipo.getFirst().toString()).equalsIgnoreCase("O")) {
            lineChart.getStyleClass().add("O");
        }


        lineChart.setTitle(String.valueOf(titulo.getFirst()));
        lineChart.setPrefSize(650, 500);
        arbol.getListaGraficas().put("Line Chart P- " + lineChart.getTitle(), lineChart);

        Simbolo s = new Simbolo(null, this.nombre, null);
        s.setFila(simboloDatos.getFila());
        s.setColumna(simboloDatos.getColumna());
        return s;
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 5) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 5 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 5 argumentos.",
                    this.fila, this.columna);
        }

        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), LINEA_PARAMETRO + count++, result);
            simbolo.setFila(argumento.fila);
            simbolo.setColumna(argumento.columna);
            result = tabla.setVariableLocal(simbolo);

            /**
             * Si el retorno de setVariable no es nulo
             * devuelvo result ya que es una excepción.
             */
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
