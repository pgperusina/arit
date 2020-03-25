package aritUI;

public class TSReporte {
    private String id;
    private String tipo;
    private int cantidadParametros;
    private int filaDeclaracion;

    public TSReporte(String id, String tipo, int cantidadParametros, int filaDeclaracion) {
        this.id = id;
        this.tipo = tipo;
        this.cantidadParametros = cantidadParametros;
        this.filaDeclaracion = filaDeclaracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidadParametros() {
        return cantidadParametros;
    }

    public void setCantidadParametros(int cantidadParametros) {
        this.cantidadParametros = cantidadParametros;
    }

    public int getFilaDeclaracion() {
        return filaDeclaracion;
    }

    public void setFilaDeclaracion(int filaDeclaracion) {
        this.filaDeclaracion = filaDeclaracion;
    }
}
