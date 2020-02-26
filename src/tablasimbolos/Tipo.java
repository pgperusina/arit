package tablasimbolos;

public class Tipo {

    public static enum Tipos {
        INTEGER,
        NUMERIC,
        BOOLEAN,
        STRING
    };

    private Tipos tipo;
    private String tipoObjeto;

    public Tipo(Tipos tipo) {
        this.tipo = tipo;
    }

    public Tipo(Tipos tipo, String tipoObjeto) {
        this.tipo = tipo;
        this.tipoObjeto = tipoObjeto;
    }

    @Override
    public String toString() {
        if (tipoObjeto == null) {
            return this.tipo + "";
        }
        return this.tipo + ":" + this.tipoObjeto;
    }

    public boolean equals(Tipo obj) {
        if (this.tipoObjeto == null && obj.tipoObjeto == null) {
            return this.tipo == obj.tipo;
        } else if (this.tipoObjeto != null && obj.tipoObjeto != null) {
            return this.tipoObjeto.equals(obj.tipoObjeto);
        }
        return false;
    }

    public Tipos getTipo() {
        return tipo;
    }

    public void setTipo(Tipos tipo) {
        this.tipo = tipo;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }
}
