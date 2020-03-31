package nativas;

import abstracto.AST;
import estructuras.Vector;
import excepciones.Excepcion;
import expresiones.Funcion;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.*;
import java.util.List;

import static utilities.Constantes.HISTOGRAMA_PARAMETRO;

public class Histograma extends Funcion {
    public Histograma(String nombre, ArrayList<AST> parametros, ArrayList<AST> instrucciones, int fila, int columna) {
        super(nombre, parametros, instrucciones, true, fila, columna);
    }

    @Override
    public Object ejecutar(Tabla tabla, Arbol arbol) {
        Simbolo simboloDatos = tabla.getVariableLocal(HISTOGRAMA_PARAMETRO+1);
        Simbolo simboloLabelX = tabla.getVariableLocal(HISTOGRAMA_PARAMETRO+2);
        Simbolo simboloTitulo = tabla.getVariableLocal(HISTOGRAMA_PARAMETRO+3);
        if (simboloDatos == null | simboloLabelX == null | simboloTitulo == null ) {
            return new Excepcion("Semántico", "Faltan argumentos para la función " +
                    this.nombre + ".", fila, columna);
        }
        if (!(simboloDatos.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloLabelX.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR)
                | simboloTitulo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.VECTOR))) {
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
        if (!(simboloTitulo.getTipo().getTipoDato().equals(Tipo.TipoDato.STRING))) {
            return new Excepcion("Semántico", "El argumento de Main enviado a la funcion " +
                    this.nombre.toUpperCase() + " debe de ser de tipo String.", simboloTitulo.getFila(), simboloTitulo.getColumna());
        }

        Vector labelX = (Vector)simboloLabelX.getValor();
        Vector datos = (Vector) simboloDatos.getValor();
        LinkedList<Double> datosDouble = new LinkedList<>();
        datos.forEach(d -> datosDouble.add(Double.valueOf(d.toString())));
        Vector titulo = (Vector)simboloTitulo.getValor();
        int group[] = new int[10];

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(String.valueOf(labelX.getFirst().toString()));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("");
        groupData(group, datosDouble);

        BarChart histograma = new BarChart(xAxis, yAxis);

        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName(String.valueOf(titulo.getFirst().toString()));
        dataSeries.getData().add(new XYChart.Data("0-10", group[0]));
        dataSeries.getData().add(new XYChart.Data("10-20", group[1]));
        dataSeries.getData().add(new XYChart.Data("20-30", group[2]));
        dataSeries.getData().add(new XYChart.Data("30-40", group[3]));
        dataSeries.getData().add(new XYChart.Data("40-50", group[4]));

        dataSeries.getData().add(new XYChart.Data("50-60", group[5]));
        dataSeries.getData().add(new XYChart.Data("60-70", group[6]));
        dataSeries.getData().add(new XYChart.Data("70-80", group[7]));
        dataSeries.getData().add(new XYChart.Data("80-90", group[8]));
        dataSeries.getData().add(new XYChart.Data("90-100", group[9]));


        histograma.getData().add(dataSeries);
        histograma.setTitle(String.valueOf(titulo.getFirst()));
        histograma.setPrefSize(650, 500);
        histograma.setBarGap(0);
        histograma.setCategoryGap(0);
        arbol.getListaGraficas().put("Histogram - " + histograma.getTitle(), histograma);

        Simbolo s = new Simbolo(null, this.nombre, null);
        s.setFila(simboloDatos.getFila());
        s.setColumna(simboloDatos.getColumna());
        return s;
    }

    private void groupData(int[] group, LinkedList<Double> data){
        for(int i=0; i<10; i++){
            group[i]=0;
        }
        for(int i=0; i<data.size(); i++){
            if(data.get(i)<=10){
                group[0]++;
            }else if(data.get(i)<=20){
                group[1]++;
            }else if(data.get(i)<=30){
                group[2]++;
            }else if(data.get(i)<=40){
                group[3]++;
            }else if(data.get(i)<=50){
                group[4]++;
            }else if(data.get(i)<=60){
                group[5]++;
            }else if(data.get(i)<=70){
                group[6]++;
            }else if(data.get(i)<=80){
                group[7]++;
            }else if(data.get(i)<=90){
                group[8]++;
            }else if(data.get(i)<=100){
                group[9]++;
            }
        }
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
            Simbolo simbolo = new Simbolo(argumento.getTipo(), HISTOGRAMA_PARAMETRO + count++, result);
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
