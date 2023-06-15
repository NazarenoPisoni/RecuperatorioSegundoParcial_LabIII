package org.example.exceptions;

public class CantidadSuperadaException extends Exception{
    private String nombreColeccion;
    private String clave;
    private Integer diferencia;


    public CantidadSuperadaException(String nombreColeccion, String clave, Integer diferencia) {
        this.nombreColeccion = nombreColeccion;
        this.clave = clave;
        this.diferencia = diferencia;
    }

    public String getNombreColeccion() {
        return nombreColeccion;
    }

    public String getClave() {
        return clave;
    }

    public Integer getDiferencia() {
        return diferencia;
    }
}
