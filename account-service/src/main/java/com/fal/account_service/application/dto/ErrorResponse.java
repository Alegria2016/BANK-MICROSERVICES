package com.fal.account_service.application.dto;

public class ErrorResponse {
    private String error;
    private String mensaje;
    private long timestamp;
    private String codigoError;

    public ErrorResponse(String error) {
        this.error = error;
        this.mensaje = error;
        this.timestamp = System.currentTimeMillis();
        this.codigoError = "MOV_ERROR";
    }

    public ErrorResponse(String error, String codigoError) {
        this.error = error;
        this.mensaje = error;
        this.timestamp = System.currentTimeMillis();
        this.codigoError = codigoError;
    }

    // Getters y Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getCodigoError() { return codigoError; }
    public void setCodigoError(String codigoError) { this.codigoError = codigoError; }
}
