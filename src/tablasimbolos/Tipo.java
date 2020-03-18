package tablasimbolos;

public class Tipo {

    public Tipo(String tipoDato, TipoEstructura tipoEstructura) {
        if (tipoDato.equalsIgnoreCase(TipoDato.INTEGER.toString())) {
            this.tipoDato = TipoDato.INTEGER;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.NUMERIC.toString())) {
            this.tipoDato = TipoDato.NUMERIC;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.BOOLEAN.toString())) {
            this.tipoDato = TipoDato.BOOLEAN;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.STRING.toString())) {
            this.tipoDato = TipoDato.STRING;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.OBJETO.toString())) {
            this.tipoDato = TipoDato.OBJETO;
        }
        this.tipoEstructura = tipoEstructura;
    }

    public enum TipoDato {
        INTEGER("integer"),
        NUMERIC("numeric"),
        BOOLEAN("boolean"),
        STRING("string"),
        OBJETO("objeto");

        private String tipoDato;

        private TipoDato (String tipoDato) {
            this. tipoDato = tipoDato;
        }

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
