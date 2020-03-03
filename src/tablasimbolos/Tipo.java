package tablasimbolos;

public class Tipo {

    public static enum Tipos {
        INTEGER,
        NUMERIC,
        BOOLEAN,
        STRING
    };

    public static enum TipoEstructura {
        VECTOR,
        LISTA,
        MATRIZ,
        ARREGLO
    };

    private Tipos tipo;
    private TipoEstructura tipoEstructura;

    public Tipo(Tipos tipo) {
        this.tipo = tipo;
    }

    public Tipo(Tipos tipo, TipoEstructura tipoEstructura) {
        this.tipo = tipo;
        this.tipoEstructura = tipoEstructura;
    }

    @Override
    public String toString() {
        if (tipoEstructura == null) {
            return this.tipo + "";
        }
        return this.tipo + ":" + this.tipoEstructura;
    }

    public boolean equals(Tipo obj) {
        if (this.tipoEstructura == null && obj.tipoEstructura == null) {
            return this.tipo == obj.tipo;
        } else if (this.tipoEstructura != null && obj.tipoEstructura != null) {
            return this.tipoEstructura.equals(obj.tipoEstructura);
        }
        return false;
    }

    public Tipos getTipo() {
        return tipo;
    }

    public void setTipo(Tipos tipo) {
        this.tipo = tipo;
    }

    public TipoEstructura getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(TipoEstructura tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }
}
