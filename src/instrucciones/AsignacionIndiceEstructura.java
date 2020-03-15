package instrucciones;

import abstracto.AST;
import excepciones.Excepcion;
import tablasimbolos.Arbol;
import tablasimbolos.Simbolo;
import tablasimbolos.Tabla;
import tablasimbolos.Tipo;

import java.util.LinkedList;

import static utilities.Utils.definirPrioridadCasteo;

public class AsignacionIndiceEstructura extends AST {

    private String identificador;
    private LinkedList<AST> posiciones;
    private AST valor;

    public AsignacionIndiceEstructura(String identificador, LinkedList<AST> posiciones, AST valor, int fila, int columna) {
        this.identificador = identificador;
        this.posiciones = posiciones;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object interpretar(Tabla tabla, Arbol arbol) {

        Simbolo simbolo = tabla.getVariable(this.identificador);
        if (simbolo == null) {
            Excepcion ex = new Excepcion("Semántico", "Se está tratando de modificar una posición de una variable " +
                    "que no ha sido definida aún. '" + simbolo.getIdentificador() + "'.", fila, columna);
            arbol.getExcepciones().add(ex);
            return ex;
        }
        if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.ARREGLO)) {
            //todo modificación a arreglo
        } else if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.MATRIZ)) {
            // todo modificación a matriz
        } else if (simbolo.getTipo().getTipoEstructura().equals(Tipo.TipoEstructura.LISTA)) {
            // TODO modificación a lista
        } else {
            /**
             * MODIFICACIÓN A VECTORES
             */
            this.tipo = simbolo.getTipo();
            if (posiciones.size() > 1) {
                return new Excepcion("Semántico", "La modificación de vectores solo admite el acceso " +
                        "a un índice.", fila, columna);
            }
            LinkedList valorSimbolo = (LinkedList) simbolo.getValor();
            Object resultPosicion = posiciones.getFirst().interpretar(tabla, arbol);
            if (resultPosicion instanceof Excepcion) {
                return resultPosicion;
            }
            LinkedList valorPosicion = (LinkedList)resultPosicion;
            if (!(valorPosicion.getFirst() instanceof Integer)) {
                return new Excepcion("Semántico", "La modificación de vectores vía índice " +
                        "requiere que los índices sean de tipo INTEGER.", fila, columna);
            }
            Object result = valor.interpretar(tabla, arbol);

            if (!(result instanceof LinkedList)) {
                return new Excepcion("Semántico", "Error modificando posición '" + (Integer)valorPosicion.getFirst()
                        + "' de Vector.", fila, columna);
            }
            LinkedList valorIntepretado = (LinkedList)valor.interpretar(tabla, arbol);
            if (valorIntepretado.size() > 1 ) {
                return new Excepcion("Semántico", "La modificación de vectores vía índice " +
                        "requiere que el nuevo valor sea de tamaño 1.", fila, columna);
            }

            //todo convertir todos los elementos del vector al tipo del elemento que se esta asignando basado en reglas de casteo
            int prioridadCasteoSimbolo = definirPrioridadCasteo(simbolo, arbol);
            int prioridadCasteoValor = definirPrioridadCasteo(valor, arbol);

            if (prioridadCasteoValor > 3) {
                return new Excepcion("Semántico", "Un solo acepta tipos de datos primitivos y vectores.",
                        fila, columna);
            }

            /**
             * Si la prioridad de casteo del símbolo existente es mayor a la prioridad de casteo
             * del valor a insertar, se castea solamente el valor a insertar, de lo contrario
             * se castean los valores existentes en el simbolo y luego se inserta el nuevo valor.
             */
            if (prioridadCasteoSimbolo > prioridadCasteoValor) {
                /**
                 * Si se quiere insertar un valor en una posición mayor al tamaño actual del vector
                 * Se insertarán elementos default antes de insertar el nuevo valor
                 */
                if ((Integer)valorPosicion.getFirst() > valorSimbolo.size()) {
                    /**
                     * Insertando elementos default
                     */
                    insertarVaciosBasadoEnTipo(valorSimbolo, valorPosicion, simbolo.getTipo());
                }
                /**
                 * Insertando nuevo valor
                 */
                if (prioridadCasteoSimbolo == 3) {
                    valorSimbolo.add(String.valueOf(valorIntepretado.getFirst()));
                } else if (prioridadCasteoSimbolo == 2) {
                    if (valorIntepretado.getFirst() instanceof  Boolean) {
                        valorSimbolo.add(valorIntepretado.getFirst() == Boolean.TRUE ? "1.0" : "0.0");
                    } else if (valorIntepretado.getFirst() instanceof Integer){
                        valorSimbolo.add(Integer.valueOf(valorIntepretado.getFirst().toString()));
                    } else {
                        valorSimbolo.add(valorIntepretado.getFirst());
                    }
                } else if (prioridadCasteoSimbolo == 1) {
                    if (valorIntepretado.getFirst() instanceof  Boolean) {
                        valorSimbolo.add(valorIntepretado.getFirst() == Boolean.TRUE ? "1" : "0");
                    } else {
                        valorSimbolo.add(valorIntepretado.getFirst());
                    }
                } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                    valorSimbolo.add(valorIntepretado.getFirst());
                }
                return null;
            } else {
                /**
                 * Casteando los valores actuales del vector al tipo del nuevo valor a insertar
                 */
                Object temp;
                if (prioridadCasteoValor == 3) {
                    simbolo.setTipo(new Tipo(Tipo.TipoDato.STRING, Tipo.TipoEstructura.VECTOR));
                    for (int i = 0; i < valorSimbolo.size(); i++) {
                        temp = valorSimbolo.get(i);
                        valorSimbolo.set(i, String.valueOf(temp));
                    }
                } else if (prioridadCasteoValor == 2) {
                    simbolo.setTipo(new Tipo(Tipo.TipoDato.NUMERIC, Tipo.TipoEstructura.VECTOR));
                    for (int i = 0; i < valorSimbolo.size(); i++) {
                        temp = valorSimbolo.get(i);
                        if (temp instanceof  Boolean) {
                            valorSimbolo.set(i, temp == Boolean.TRUE ? "1.0" : "0.0");
                        } else if (temp instanceof Integer){
                            valorSimbolo.set(i, Integer.valueOf(temp.toString()));
                        } else {
                            valorSimbolo.set(i, valorIntepretado.getFirst());
                        }
                    }
                } else if (prioridadCasteoValor == 1) {
                    simbolo.setTipo(new Tipo(Tipo.TipoDato.INTEGER, Tipo.TipoEstructura.VECTOR));
                    for (int i = 0; i < valorSimbolo.size(); i++) {
                        temp = valorSimbolo.get(i);
                        if (temp instanceof  Boolean) {
                            valorSimbolo.set(i, temp == Boolean.TRUE ? "1" : "0");
                        } else {
                            valorSimbolo.set(i, temp);
                        }
                    }
                } else {
                    simbolo.setTipo(new Tipo(Tipo.TipoDato.BOOLEAN, Tipo.TipoEstructura.VECTOR));
                    for (int i = 0; i < valorSimbolo.size(); i++) {
                        temp = valorSimbolo.get(i);
                        valorSimbolo.set(i, temp);
                    }
                }
                /**
                 * Si se quiere insertar un valor en una posición mayor al tamaño actual del vector
                 * Se insertarán elementos default antes de insertar el nuevo valor
                 */
                if ((Integer)valorPosicion.getFirst() > valorSimbolo.size()) {
                    /**
                     * Insertando elementos default
                     */
                    insertarVaciosBasadoEnTipo(valorSimbolo, valorPosicion, valor.getTipo());
                }
                /**
                 * Insertando nuevo valor
                 */
                if (prioridadCasteoValor == 3) {
                    valorSimbolo.add(String.valueOf(valorIntepretado.getFirst()));
                } else if (prioridadCasteoValor == 2) {
                    if (valorIntepretado.getFirst() instanceof  Boolean) {
                        valorSimbolo.add(valorIntepretado.getFirst() == Boolean.TRUE ? "1.0" : "0.0");
                    } else if (valorIntepretado.getFirst() instanceof Integer){
                        valorSimbolo.add(Integer.valueOf(valorIntepretado.getFirst().toString()));
                    } else {
                        valorSimbolo.add(valorIntepretado.getFirst());
                    }
                } else if (prioridadCasteoValor == 1) {
                    if (valorIntepretado.getFirst() instanceof  Boolean) {
                        valorSimbolo.add(valorIntepretado.getFirst() == Boolean.TRUE ? "1" : "0");
                    } else {
                        valorSimbolo.add(valorIntepretado.getFirst());
                    }
                } else if (simbolo.getTipo().getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                    valorSimbolo.add(valorIntepretado.getFirst());
                }
                return null;
            }
        }
        return null;
    }

    private void insertarVaciosBasadoEnTipo(LinkedList valorSimbolo, LinkedList valorPosicion, Tipo tipo) {
        for (int i = valorSimbolo.size(); i < (Integer)valorPosicion.getFirst()-1; i++) {
            if (tipo.getTipoDato().equals(Tipo.TipoDato.STRING)) {
                valorSimbolo.add(i, "NULL");
            } else if (tipo.getTipoDato().equals(Tipo.TipoDato.NUMERIC)) {
                valorSimbolo.add(i, 0.0);
            } else if (tipo.getTipoDato().equals(Tipo.TipoDato.INTEGER)) {
                valorSimbolo.add(i, 0);
            } else if (tipo.getTipoDato().equals(Tipo.TipoDato.BOOLEAN)) {
                valorSimbolo.add(i, false);
            }
        }
    }


}
