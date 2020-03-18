package estructuras;

import java.util.LinkedList;
import java.util.List;

public class Lista extends LinkedList {

    public Lista() {
        super();
    }

    public Lista(List<Object> asList) {
        super(asList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int c = 0;
        sb.append("[ ");
        for (Object v : this) {
            if (c > 0) sb.append(", ");
            sb.append(v.toString());
            c++;
        }
        sb.append(" ]");

        return sb.toString();
    }
}
