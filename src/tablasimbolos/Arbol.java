package tablasimbolos;

import abstracto.AST;
import java.util.ArrayList;

import excepciones.Excepcion;
import javafx.scene.Group;
import javafx.scene.control.TextArea;

public class Arbol {

    private ArrayList<AST> instrucciones;
    private ArrayList<Excepcion> excepciones;
    private TextArea consola;
    private Tabla tablaGlobal;
    private Group grupo;

    public Arbol(ArrayList<AST> instrucciones) {
        this.instrucciones = instrucciones;
        this.excepciones = new ArrayList<>();
    }

    public ArrayList<AST> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<AST> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public ArrayList<Excepcion> getExcepciones() {
        return excepciones;
    }

    public void setExcepciones(ArrayList<Excepcion> excepciones) {
        this.excepciones = excepciones;
    }

    public TextArea getConsola() {
        return consola;
    }

    public void setConsola(TextArea consola) {
        this.consola = consola;
    }

    public Tabla getTablaGlobal() {
        return tablaGlobal;
    }

    public void setTablaGlobal(Tabla tablaGlobal) {
        this.tablaGlobal = tablaGlobal;
    }

    public Group getGrupo() {
        return grupo;
    }

    public void setGrupo(Group grupo) {
        this.grupo = grupo;
    }

}
