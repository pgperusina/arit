package tablasimbolos;

public class Tipo {

    public static enum TipoDato {
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

    private TipoDato tipo;
    private TipoEstructura tipoEstructura;

    public Tipo(TipoDato tipo) {
        this.tipo = tipo;
    }

    public Tipo(TipoDato tipo, TipoEstructura tipoEstructura) {
        this.tipo = tipo;
        this.tipoEstructura = tipoEstructura;
    }

    @Override
    public String toString() {
        if (tipoEstructura == null) {
            return this.tipo + "";
        }
        return this.tipoEstructura + ":" + this.tipo;
    }

    public boolean equals(Tipo obj) {
        if (this.tipoEstructura == null && obj.tipoEstructura == null) {
            return this.tipo == obj.tipo;
        } else if (this.tipoEstructura != null && obj.tipoEstructura != null) {
            return this.tipoEstructura.equals(obj.tipoEstructura);
        }
        return false;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public void setTipo(TipoDato tipo) {
        this.tipo = tipo;
    }

    public TipoEstructura getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(TipoEstructura tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }
}
