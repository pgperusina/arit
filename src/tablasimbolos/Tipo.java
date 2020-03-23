package tablasimbolos;

public class Tipo {

    public Tipo(String tipoDato, TipoEstructura tipoEstructura) {
        if (tipoDato.equalsIgnoreCase(TipoDato.INTEGER.tipoDato)) {
            this.tipoDato = TipoDato.INTEGER;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.NUMERIC.tipoDato)) {
            this.tipoDato = TipoDato.NUMERIC;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.BOOLEAN.tipoDato)) {
            this.tipoDato = TipoDato.BOOLEAN;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.STRING.tipoDato)) {
            this.tipoDato = TipoDato.STRING;
        } else if (tipoDato.equalsIgnoreCase(TipoDato.OBJETO.tipoDato)) {
            this.tipoDato = TipoDato.OBJETO;
        } else if (tipoDato.equalsIgnoreCase(TipoEstructura.ARREGLO.tipoEstructura)) {
            this.tipoDato = TipoDato.OBJETO;
        }else if (tipoDato.equalsIgnoreCase(TipoEstructura.MATRIZ.tipoEstructura)) {
            this.tipoDato = TipoDato.OBJETO;
        }else if (tipoDato.equalsIgnoreCase(TipoEstructura.LISTA.tipoEstructura)) {
            this.tipoDato = TipoDato.OBJETO;
        } else if (tipoDato.equalsIgnoreCase(TipoEstructura.VECTOR.tipoEstructura)) {
            this.tipoDato = TipoDato.OBJETO;
        }
        this.tipoEstructura = tipoEstructura;
    }

    public enum TipoDato {
        INTEGER("integer"),
        NUMERIC("double"),
        BOOLEAN("boolean"),
        STRING("string"),
        OBJETO("object");

        TipoDato(String tipoDato) {
            this.tipoDato = tipoDato;
        }

        private String tipoDato;


    }

    public enum TipoEstructura {
        VECTOR("vector"),
        LISTA("lista"),
        MATRIZ("matriz"),
        ARREGLO("arreglo");

        private String tipoEstructura;

        TipoEstructura(String tipoEstructura) {
            this. tipoEstructura = tipoEstructura;
        }

    }

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
