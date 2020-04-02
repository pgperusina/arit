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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static utilities.Utils.agregarFuncionesNativas;
import static utilities.Utils.getRandomInRange;

public class Controller implements Initializable {

    public CodeArea archivo;
    @FXML
    private MenuItem menuAbrir;
    @FXML
    private MenuItem menuGuardar;
    @FXML
    private MenuItem menuGuardarComo;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextArea consola;

    private Arbol arbol;
    private Tabla tabla;
    private final String sourceCodeUrl = "https://github.com/pgperusina/arit/tree/develop";
    private URI codeUrl;
    private URI grafoUrl;
    Stage stageReporteErrores;
    Scene sceneReporteErrores;
    Stage stageReporteTS;
    Scene sceneReporteTS;
    Stage stageReporteGraficas;
    Scene sceneReporteGraficas;
    private Group groupChart;
    File openedFile;

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
        stageReporteErrores.setWidth(900);
        stageReporteErrores.setHeight(500);
        tablaErrores.setPrefWidth(900);
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

    @FXML
    private void abrirArbol(ActionEvent event) {
        try {
            String openInstruction = "/usr/bin/open";
            String graphFile = "file:///"+System.getProperty("user.dir")+"/grafo.png";

            String[] cmd = new String[2];
            cmd[0] = openInstruction;
            cmd[1] = graphFile;

            Runtime rt = Runtime.getRuntime();

            rt.exec( cmd );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivo.setParagraphGraphicFactory(LineNumberFactory.get(archivo));
        try {
            grafoUrl = new URI("file:///"+ System.getProperty("user.dir")+"/grafo.png");
            codeUrl = new URI(sourceCodeUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        initTablaErrores();
        initTablaTS();
        initMenuItemAbrir();
        initMenuItemGuardar();
        initMenuItemGuardarComo();
    }

    private void initMenuItemGuardarComo() {
        menuGuardarComo.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar como...");

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Arit and txt files (*.txt, *.arit)", "*.txt", "*.arit");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(AnchorPane.getScene().getWindow());

            if(file != null){
                SaveFile(archivo.getText(), file);
            }
        });
    }

    private void initMenuItemGuardar() {
        menuGuardar.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Arit and txt files (*.txt, *.arit)", "*.txt", "*.arit");
            fileChooser.getExtensionFilters().add(extFilter);

            if(openedFile != null){
                SaveFile(archivo.getText(), openedFile);
            }
        });
    }

    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error guardando el archivo.");
            alert.showAndWait();
            return;
        }

    }

    private void initMenuItemAbrir() {
        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            FileChooser.ExtensionFilter extFilter1 =
                    new FileChooser.ExtensionFilter("Arit Documents (*.arit, *.txt)", "*.txt","*.arit");
            FileChooser.ExtensionFilter extFilter2 =
                    new FileChooser.ExtensionFilter("All Files", "*.*");
            fileChooser.getExtensionFilters().addAll(extFilter1, extFilter2);
            fileChooser.getExtensionFilters().addAll(extFilter1, extFilter2);
            fileChooser.setTitle("Abrir archivo...");


            // create an Event Handler
            EventHandler<ActionEvent> event =
                    e -> {
                        File file = fileChooser.showOpenDialog(AnchorPane.getScene().getWindow());
                        if (file != null) {
                            List<String> lines = new ArrayList<String>();
                            String line;
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                while ((line = br.readLine()) != null) {
                                    lines.add(line);
                                }
                                br.close();
                                archivo.clear();
                                for (String linea : lines) {
                                    archivo.appendText(linea);
                                    archivo.appendText("\n");
                                    archivo.appendText("\n");
                                }
                                this.openedFile = file;
                            } catch (IOException ex) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Error leyendo el archivo para ser cargado.");
                                alert.showAndWait();
                                return;
                            }
                        } else {

                            return;
                        }
                    };

            menuAbrir.setOnAction(event);

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error cargando el archivo.");
            alert.showAndWait();
            return;
        }
    }

    private void initTablaTS() {
        tablaSimbolos.getItems().clear();
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
