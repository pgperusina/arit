package estructuras;

import java.util.LinkedList;
import java.util.List;

public class Vector extends LinkedList {

    public Vector() {
        super();
    }

    public Vector(List<Object> asList) {
        super(asList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int c = 0;
        sb.append("( ");
        for (Object v : this) {
            if (c > 0) sb.append(", ");
            sb.append(v.toString());
            c++;
        }
        sb.append(" )");

        return sb.toString();
    }
}
