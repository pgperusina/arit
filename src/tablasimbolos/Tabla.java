package tablasimbolos;

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

    public String setVariable(Simbolo simbolo) {
        for (Tabla e = this; e != null; e = e.getAnterior()) {
            Simbolo simboloEnTabla = (Simbolo) (e.getTabla().get(simbolo.getIdentificador()));
            if (simboloEnTabla != null) {
                return "La variable con el identificador"
                        + simbolo.getIdentificador() + " ya ha sido definida.";
            }
        }
        this.tabla.put(simbolo.getIdentificador(), simbolo);
        return null;
    }

    public Simbolo getVariable(String id) {
        for (Tabla e = this; e != null; e = e.getAnterior()) {
            Simbolo simboloEnTabla = (Simbolo) (e.getTabla().get(id));
            if (simboloEnTabla != null) {
                return simboloEnTabla;
            }
        }
        return null;
    }

    public String setFuncion(Funcion f) {
        for (Funcion i : funciones) {
            if (f.getNombre().equalsIgnoreCase(i.getNombre())) {
                return "La función con el identificador"
                        + f.getNombre() + " ya ha sido definida.";
            }
        }
        this.funciones.add(f);
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
