package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.*;

import static utilities.Constantes.PIE_PARAMETRO;

public class Pie extends Funcion {
    public Pie(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simboloDatos = tabla.getVariableLocal(PIE_PARAMETRO+1);
        Simbolo simboloLabels = tabla.getVariableLocal(PIE_PARAMETRO+2);
        Simbolo simboloTitulo = tabla.getVariableLocal(PIE_PARAMETRO+3);
        if (simboloDatos == null | simboloLabels == null | simboloTitulo == null) {
            return new Excepcion("Semántico", "Faltan argumentos para la función " +
                    this.nombre + ".", fila, columna);
        }
        if (!(simboloDatos.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloLabels.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloTitulo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
            return new Excepcion("Semántico", "Los argumentos enviados a la función " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Vector.", simboloDatos.getFila(), simboloDatos.getColumna());
        }
        if (!(simboloDatos.getTipo().getTipoDato().equals(Tipo.TipoDato.NUMERIC)
                | simboloDatos.getTipo().getTipoDato().equals(Tipo.TipoDato.INTEGER))) {
            return new Excepcion("Semántico", "El argumento de datos enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo Numeric o Integer.", simboloDatos.getFila(), simboloDatos.getColumna());
        }
        if (!(simboloLabels.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento de labels enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloLabels.getFila(), simboloLabels.getColumna());
        }
        if (!(simboloTitulo.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento de título enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloTitulo.getFila(), simboloTitulo.getColumna());
        }

        Vector labels = (Vector)simboloLabels.getValor();
        Vector datos = (Vector)simboloDatos.getValor();
        Vector titulo = (Vector)simboloTitulo.getValor();

        if (datos.size() != labels.size()) {
            return new Excepcion("Semántico", "Los vectores de datos y labels de la función " +
                    this.nombre.toUpperCase() + " deben de ser del mismo tamaño.", simboloDatos.getFila(), simboloDatos.getColumna());
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < labels.size(); i++) {
            pieChartData.add(new PieChart.Data(labels.get(i) + "", Double.valueOf(datos.get(i).toString())));
        }
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(String.valueOf(titulo.getFirst()));
        pieChart.setPrefSize(650, 500);
        pieChart.setStartAngle(90d);
        pieChart.setLabelsVisible(true);
        pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), ": ", data.pieValueProperty(), ""))
        );

        arbol.getListaGraficas().put("Pie Chart - " + pieChart.getTitle(), pieChart);

        Simbolo s = new Simbolo(null, this.nombre, null);
        s.setFila(simboloDatos.getFila());
        s.setColumna(simboloDatos.getColumna());
        return s;
    }

    @Override
    public Object cargarTabla(Tabla tabla, Arbol arbol, ArrayList<AST> argumentos) {
        Object result;
        if (argumentos.size() > 3) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 3 argumentos.",
                    argumentos.get(0).fila, argumentos.get(0).columna);
        }
        if (argumentos.size() < 1) {
            return new Excepcion("Semántico","La función '"+ this.nombre + "' debe recibir " +
                    " 3 argumentos.",
                    this.fila, this.columna);
        }

        int count = 1;
        for (AST argumento : argumentos) {
            result = argumento.ejecutar(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Simbolo simbolo = new Simbolo(argumento.getTipo(), PIE_PARAMETRO + count++, result);
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
