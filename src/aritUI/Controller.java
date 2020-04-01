package aritUI;

import abstracto.AST;
import analizador.descendente.Gramatica;
import analizador.descendente.ParseException;
import analizador.descendente.TokenMgrError;
import excepciones.Excepcion;
import expresiones.Funcion;
import instrucciones.Break;
import instrucciones.Continue;
import instrucciones.Return;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.*;
import javafx.fxml.Initializable;
import tablasimbolos.Arbol;
import tablasimbolos.Tabla;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static utilities.Utils.agregarFuncionesNativas;
import static utilities.Utils.getRandomInRange;

public class Controller implements Initializable {
    @FXML
    private CodeArea archivo;
    @FXML
    private Button botonEjecutar;
    @FXML
    private TextArea consola;

    private Arbol arbol;
    private Tabla tabla;
    private final String sourceCodeUrl = "https://github.com/pgperusina/arit/tree/develop";
    private URI codeUrl;
    Stage stageReporteErrores;
    Scene sceneReporteErrores;
    Stage stageReporteTS;
    Scene sceneReporteTS;
    Stage stageReporteGraficas;
    Scene sceneReporteGraficas;
    private Group groupChart;

    TableView tablaErrores = new TableView();
    TableView tablaSimbolos = new TableView();

    @FXML
    private void Ejecutar(ActionEvent event) {
        consola.clear();
        if (archivo.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("La entrada está vacía.");
            alert.setHeaderText(null);
            alert.setContentText("No ha ingresado código para ejecutar.");
            alert.showAndWait();
            return;
        }
        //EjecutarInstrucciones();
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new StringReader(archivo.getText())));
            arbol = new Arbol(new ArrayList<>());
            ejecutarArbol(arbol, parser);
        } catch (ParseException e) {
            consola.setText(consola.getText() + e.getMessage() + "\n");
            System.err.println(e.getMessage());
        } catch (TokenMgrError e) {
            consola.setText(consola.getText() + e.getMessage() + "\n");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void EjecutarAscendente(ActionEvent event) {
        consola.clear();
        if (archivo.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("La entrada está vacía.");
            alert.setHeaderText(null);
            alert.setContentText("No ha ingresado código para ejecutar.");
            alert.showAndWait();
            return;
        }
        //EjecutarInstrucciones();
        try {
            Gramatica parser = new Gramatica(new BufferedReader(new StringReader(archivo.getText())));
            arbol = new Arbol(new ArrayList<>());
            ejecutarArbol(arbol, parser);
        } catch (ParseException e) {
            consola.setText(consola.getText() + e.getMessage() + "\n");
            System.err.println(e.getMessage());
        } catch (TokenMgrError e) {
            consola.setText(consola.getText() + e.getMessage() + "\n");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void reporteErrores() throws IOException {
        stageReporteErrores = new Stage();
        stageReporteErrores.setWidth(700);
        stageReporteErrores.setHeight(500);
        tablaErrores.setPrefWidth(700);
        tablaErrores.setPrefHeight(500);
        HBox hBox = new HBox(tablaErrores);
        hBox.setPrefWidth(stageReporteErrores.getWidth());
        hBox.setPrefHeight(stageReporteErrores.getHeight());

        sceneReporteErrores = new Scene(hBox);

        stageReporteErrores.setTitle("Reporte de errores");
        stageReporteErrores.setResizable(true);
        stageReporteErrores.setScene(sceneReporteErrores);
        stageReporteErrores.show();

    }

    @FXML
    private void reporteTS() throws IOException {
        if (this.tabla.getTabla().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("La tabla de símbolos está vacía.");
            alert.setHeaderText(null);
            alert.setContentText("Ejecute el código para poder mostrar el reporte de tabla de símbolos..");
            alert.showAndWait();
            return;
        }
        popularTablaReporteTS();
        stageReporteTS = new Stage();
        stageReporteTS.setWidth(650);
        stageReporteTS.setHeight(500);
        tablaSimbolos.setPrefWidth(650);
        tablaSimbolos.setPrefHeight(500);
        HBox hBox = new HBox(tablaSimbolos);
        hBox.setPrefWidth(stageReporteTS.getWidth());
        hBox.setPrefHeight(stageReporteTS.getHeight());

        sceneReporteTS = new Scene(hBox);
        stageReporteTS.setTitle("Reporte de Tabla de Símbolos");
        stageReporteTS.setResizable(true);
        stageReporteTS.setScene(sceneReporteTS);
        stageReporteTS.show();

    }

    @FXML
    private void reportGraficas() {
        stageReporteGraficas = new Stage();
        stageReporteGraficas.setWidth(700);
        stageReporteGraficas.setHeight(500);
        String cssFile = "";

        TabPane tabPane = new TabPane();
        for (Map.Entry<String, Chart> entry : arbol.getListaGraficas().entrySet()) {
            tabPane.getTabs().add(new Tab(entry.getKey(), entry.getValue()));
            if (entry.getValue() instanceof LineChart) {
                if (entry.getValue().getStyleClass().get(1).equalsIgnoreCase("P")) {
                    cssFile = "./lineChartPunto.css";
                } else if (entry.getValue().getStyleClass().get(1).equalsIgnoreCase("I")) {
                    cssFile = "./lineChartLinea.css";
                } else {
                    cssFile = "./lineChartFull.css";
                }
            }
        }

        VBox vBox = new VBox(tabPane);

        vBox.setPrefWidth(stageReporteGraficas.getWidth());
        vBox.setPrefHeight(stageReporteGraficas.getHeight());

        sceneReporteGraficas = new Scene(vBox);
        sceneReporteGraficas.getStylesheets().add(cssFile);

        stageReporteGraficas.setTitle("Reporte de gráficas");
        stageReporteGraficas.setResizable(true);
        stageReporteGraficas.setScene(sceneReporteGraficas);
        stageReporteGraficas.show();
    }

    private void popularTablaReporteTS() {
        this.tabla.getFunciones().forEach(funcion -> {
            if (!funcion.isNativa()) {
                tablaSimbolos.getItems().add(new TSReporte(funcion.getNombre(), "FUNCION",
                        funcion.getParametros().size(), funcion.fila));
            }
        });
        this.tabla.getTabla().forEach((nombre,simbolo) -> {
            tablaSimbolos.getItems().add(new TSReporte(nombre, simbolo.getTipo().toString(),
                    0, simbolo.getFila()));
        });
    }

    @FXML
    private void abrirGitHub(ActionEvent event) {
        try {
            java.awt.Desktop.getDesktop().browse(codeUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivo.setParagraphGraphicFactory(LineNumberFactory.get(archivo));
        try {
            codeUrl = new URI(sourceCodeUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        initTablaErrores();
        initTablaTS();
    }

    private void initTablaTS() {
        TableColumn<TSReporte, String> column1 = new TableColumn<>("Identificador");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<TSReporte, String> column2 = new TableColumn<>("Tipo");
        column2.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<TSReporte, Integer> column3 = new TableColumn<>("Cantidad parámetros");
        column3.setCellValueFactory(new PropertyValueFactory<>("cantidadParametros"));

        TableColumn<TSReporte, Integer> column4 = new TableColumn<>("Fila declaración");
        column4.setCellValueFactory(new PropertyValueFactory<>("filaDeclaracion"));

        tablaSimbolos.getColumns().add(column1);
        tablaSimbolos.getColumns().add(column2);
        tablaSimbolos.getColumns().add(column3);
        tablaSimbolos.getColumns().add(column4);
    }

    private void initTablaErrores() {
        TableColumn<Excepcion, String> column1 = new TableColumn<>("Tipo");
        column1.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Excepcion, String> column2 = new TableColumn<>("Descripción");
        column2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Excepcion, Integer> column3 = new TableColumn<>("Fila");
        column3.setCellValueFactory(new PropertyValueFactory<>("fila"));

        TableColumn<Excepcion, Integer> column4 = new TableColumn<>("Columna");
        column4.setCellValueFactory(new PropertyValueFactory<>("columna"));
        tablaErrores.getColumns().add(column1);
        tablaErrores.getColumns().add(column2);
        tablaErrores.getColumns().add(column3);
        tablaErrores.getColumns().add(column4);
    }

    private void ejecutarArbol(Arbol arbol, Gramatica parser) throws ParseException {
        System.out.println("ejecutandoooo");
        arbol.setInstrucciones(parser.analizar(new ArrayList<>()));
        arbol.setOutput(consola);
        tabla = new Tabla(null);
        /**
         * Agregando funciones nativas
         */
        agregarFuncionesNativas(tabla);
        /**
         * Seteando tabla global
         */
        arbol.setTablaGlobal(tabla);

        // Primer recorrido para agregar funciones a la TS
        for (AST instruccion : arbol.getInstrucciones()) {
            if (instruccion instanceof Funcion) {
                Object result = tabla.setFuncion((Funcion) instruccion);
                if (result instanceof Excepcion) {
                    arbol.getExcepciones().add((Excepcion) result);
                    break;
                }
            }
        }

        /**
         * Segundo recorrido para recorrer instrucciones del árbol
         */
        for (AST m : arbol.getInstrucciones()) {
            if (!(m instanceof Funcion)) {
                Object result = m.ejecutar(tabla, arbol);

                if (result instanceof Excepcion) {
                    arbol.getExcepciones().add((Excepcion) result);
                }
                if (result instanceof Break) {
                    Excepcion ex = new Excepcion("Semántico", "Sentencia 'break' fuera de ciclo.", m.fila, m.columna);
                    arbol.getExcepciones().add(ex);
                    ex.print(arbol.getConsola());
                } else if (result instanceof Continue) {
                    Excepcion ex = new Excepcion("Semántico", "Sentencia 'continue' fuera de ciclo.", m.fila, m.columna);
                    arbol.getExcepciones().add(ex);
                    ex.print(arbol.getConsola());
                } else if (result instanceof Return) {
                    Excepcion ex = new Excepcion("Semántico", "Sentencia 'return' fuera de función.", m.fila, m.columna);
                    arbol.getExcepciones().add(ex);
                    ex.print(arbol.getConsola());
                }
            }
        }

        /**
         * Agrego a la lista de excepciones, las excepciones léxicas y sintácticas
         */
//            arbol.getExcepciones().addAll(parser.token_source.listaExcepciones);
        arbol.getExcepciones().addAll(parser.listaExcepciones);
        if (arbol.getExcepciones().size() != 0) {
            tablaErrores.getItems().clear();
            consola.setText(consola.getText() + "\n\n/******* EXCEPCIONES ******/" + "\n");
            arbol.getExcepciones().forEach(excepcion -> {
                tablaErrores.getItems().add(excepcion);
                consola.setText(consola.getText() + excepcion.toString() + "\n");
                System.out.println(excepcion.toString());
            });
        }
        if (arbol != null) {
            dibujarArbol(arbol);
        }
    }

    private static void dibujarArbol(Arbol arbol) {
        StringBuilder dotBuilder = new StringBuilder();
        dotBuilder.append("digraph G \n");
        dotBuilder.append("{ \n");
        String padre = "";
        String hijo = "";
        try {
            for (AST instruccion : arbol.getInstrucciones()) {
                int random = getRandomInRange(1, 10000);
                hijo = instruccion.getClass().getSimpleName() + random;
                dotBuilder.append("Arbol" + "->" + hijo);
                dotBuilder.append("\n");
                instruccion.crearDotFile(dotBuilder, hijo);
            }
            dotBuilder.append("}");
            System.out.println(dotBuilder.toString());
            System.out.println(System.getProperty("user.dir"));
            crearArchivoDot(dotBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void crearArchivoDot(StringBuilder dotBuilder) {
        try {

            File file = new File(System.getProperty("user.dir")+"/grafo.txt");
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(dotBuilder.toString());
            } finally {
                if (writer != null) writer.close();
            }

            String dotPath = "/usr/local/Cellar/graphviz/2.42.3/bin/dot";

            String fileInputPath = System.getProperty("user.dir")+"/grafo.txt";
            String fileOutputPath = System.getProperty("user.dir")+"/grafo.png";

            String tParam = "-Tpng";
            String tOParam = "-o";

            String[] cmd = new String[5];
            cmd[0] = dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;

            Runtime rt = Runtime.getRuntime();

            rt.exec( cmd );
            System.out.print("Arbol creado");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }
}
