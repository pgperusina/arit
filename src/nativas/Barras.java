package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.ArrayList;

import static utilities.Constantes.BARRAS_PARAMETRO;

public class Barras extends Funcion {
    public Barras(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simboloDatos = tabla.getVariableLocal(BARRAS_PARAMETRO+1);
        Simbolo simboloLabelX = tabla.getVariableLocal(BARRAS_PARAMETRO+2);
        Simbolo simboloLabelY = tabla.getVariableLocal(BARRAS_PARAMETRO+3);
        Simbolo simboloTitulo = tabla.getVariableLocal(BARRAS_PARAMETRO+4);
        Simbolo simboloNombresBarras = tabla.getVariableLocal(BARRAS_PARAMETRO+5);
        if (simboloDatos == null | simboloLabelX == null | simboloLabelY == null
                | simboloTitulo == null | simboloNombresBarras == null) {
            return new Excepcion("Semántico", "Faltan argumentos para la función " +
                    this.nombre + ".", fila, columna);
        }
        if (!(simboloDatos.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloLabelX.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloLabelY.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloTitulo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloNombresBarras.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
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
        if (!(simboloNombresBarras.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento Names.arg enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloNombresBarras.getFila(), simboloNombresBarras.getColumna());
        }

        Vector labelX = (Vector)simboloLabelX.getValor();
        Vector labelY = (Vector)simboloLabelY.getValor();
        Vector datos = (Vector)simboloDatos.getValor();
        Vector titulo = (Vector)simboloTitulo.getValor();
        Vector nombresBarras = (Vector)simboloNombresBarras.getValor();

        if (datos.size() != nombresBarras.size()) {
            return new Excepcion("Semántico", "Los vectores de datos y nombres de cada barra de la función " +
                    this.nombre.toUpperCase() + " deben de ser del mismo tamaño.", simboloDatos.getFila(), simboloDatos.getColumna());
        }
        
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(String.valueOf(labelX.getFirst().toString()));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(String.valueOf(labelY.getFirst().toString()));

        BarChart barChart = new BarChart(xAxis, yAxis);

        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName(String.valueOf(titulo.getFirst().toString()));

        for (int i = 0; i < datos.size(); i++) {
            dataSeries.getData().add(new XYChart.Data(String.valueOf(nombresBarras.get(i).toString())
                    , Double.valueOf(datos.get(i).toString())));
        }

        barChart.getData().add(dataSeries);
        barChart.setTitle(String.valueOf(titulo.getFirst()));
        barChart.setPrefSize(650, 500);
        arbol.getListaGraficas().put("Bar Chart - " + barChart.getTitle(), barChart);

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
            Simbolo simbolo = new Simbolo(argumento.getTipo(), BARRAS_PARAMETRO + count++, result);
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
