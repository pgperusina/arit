package tablasimbolos;

import excepciones.Excepcion;
import expresiones.Funcion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tabla {

    private Map<String, Simbolo> tabla;
    private Tabla anterior;
    private ArrayList<Funcion> funciones;

    public Tabla(Tabla Anterior) {
        this.tabla = new HashMap<>();
        this.anterior = Anterior;
        this.funciones = new ArrayList<>();
    }

    public Object setVariable(Simbolo simbolo) {
        for (Tabla e = this; e != null; e = e.getAnterior()) {
            Simbolo simboloEnTabla = (e.getTabla().get(simbolo.getIdentificador()));
            if (simboloEnTabla != null) {
                return new Excepcion("Semántico","La variable con el identificador '"
                        + simbolo.getIdentificador() + "' ya ha sido definida.",
                        simbolo.getFila(), simbolo.getColumna());
            }
        }
        this.tabla.put(simbolo.getIdentificador(), simbolo);
        return null;
    }

    public Object setVariableLocal(Simbolo simbolo) {
        Simbolo simboloEnTabla = this.getTabla().get(simbolo.getIdentificador());
        if (simboloEnTabla != null) {
            return new Excepcion("Semántico","La variable con el identificador '"
                    + simbolo.getIdentificador() + "' ya ha sido definida.",
                    simbolo.getFila(), simbolo.getColumna());
        }
        this.tabla.put(simbolo.getIdentificador(), simbolo);
        return null;
    }

    public Simbolo getVariable(String id) {
        for (Tabla e = this; e != null; e = e.getAnterior()) {
            Simbolo simboloEnTabla = (e.getTabla().get(id));
            if (simboloEnTabla != null) {
                return simboloEnTabla;
            }
        }
        return null;
    }

    public Simbolo getVariableLocal(String id) {
        Simbolo simboloEnTabla = this.getTabla().get(id);
        if (simboloEnTabla != null) {
            return simboloEnTabla;
        }
        return null;
    }

    public Object setFuncion(Funcion funcion) {
        for (Funcion f : funciones) {
            if (funcion.getNombre().equalsIgnoreCase(f.getNombre())) {
                return new Excepcion("Semántico","La función con el identificador '"
                        + funcion.getNombre() + "' ya ha sido definida.",
                        funcion.fila, funcion.columna);
            }
        }
        this.funciones.add(funcion);
        return null;
    }

    public Funcion getFuncion(String nombre) {
        for (Tabla e = this; e != null; e = e.getAnterior()) {
            for (Funcion f : e.getFunciones()) {
                if (f.getNombre().equalsIgnoreCase(nombre)) {
                    return f;
                }
            }
        }
        return null;
    }

    public Map<String, Simbolo> getTabla() {
        return tabla;
    }

    public void setTabla(Map<String, Simbolo> Table) {
        this.tabla = Table;
    }

    public Tabla getAnterior() {
        return anterior;
    }

    public void setAnterior(Tabla Anterior) {
        this.anterior = Anterior;
    }

    public ArrayList<Funcion> getFunciones() {
        return funciones;
    }
}
