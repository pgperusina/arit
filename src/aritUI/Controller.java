package aritUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea entrada;
    @FXML
    private Button btnEjecutar;
    @FXML
    private TextArea consola;

    @FXML
    private void Ejecutar(ActionEvent event) {
        consola.clear();
        if (entrada.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Entrada vacia");
            alert.setHeaderText(null);
            alert.setContentText("La entrada a ejecutar se encuentra vacia!");
            alert.showAndWait();
            return;
        }
        EjecutarInstrucciones();
//        try {
//            Gramatica parser = new Gramatica(new BufferedReader(new StringReader(entrada.getText())));
//            Arbol arbol = parser.Analizar();
//            EjecutarInstrucciones(arbol);
//        } catch (ParseException e) {
//            consola.setText(consola.getText() + e.getMessage() + "\n");
//            System.err.println(e.getMessage());
//        } catch (TokenMgrError e) {
//            consola.setText(consola.getText() + e.getMessage() + "\n");
//            System.err.println(e.getMessage());
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void EjecutarInstrucciones() {
        System.out.println("ejecutandoooo");
//        tree.setConsola(consola);
//        Tabla tabla = new Tabla(null);
//
//        tree.getInstrucciones().forEach(m -> {
//            Object result = m.interpretar(tabla, tree);
//            if(result instanceof Excepcion){
//                ((Excepcion)result).imprimir(tree.getConsola());
//            }
//        });
    }
}
