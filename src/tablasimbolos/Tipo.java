package tablasimbolos;

public class Tipo {

    public static enum TipoDato {
        INTEGER,
        NUMERIC,
        BOOLEAN,
        STRING,
        OBJETO
    };

    public static enum TipoEstructura {
        VECTOR,
        LISTA,
        MATRIZ,
        ARREGLO
    };

    private TipoDato tipoDato;
    private TipoEstructura tipoEstructura;

    public Tipo(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Tipo(TipoDato tipoDato, TipoEstructura tipoEstructura) {
        this.tipoDato = tipoDato;
        this.tipoEstructura = tipoEstructura;
    }

    @Override
    public String toString() {
        if (tipoEstructura == null) {
            return this.tipoDato + "";
        }
        return this.tipoEstructura + "=>" + this.tipoDato;
    }

    public boolean equals(Tipo obj) {
        if (this.tipoEstructura == null && obj.tipoEstructura == null) {
            return this.tipoDato == obj.tipoDato;
        } else if (this.tipoEstructura != null && obj.tipoEstructura != null) {
            return this.tipoEstructura.equals(obj.tipoEstructura);
        }
        return false;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipo) {
        this.tipoDato = tipo;
    }

    public TipoEstructura getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(TipoEstructura tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }
}
