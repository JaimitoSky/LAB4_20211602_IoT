package com.example.lab4_20211602_iot.data.model;

public class ServiceReminder {
    public long id;
    public String nombre;
    public double monto;
    public long fechaVencimientoMs;
    public Periodicity periodicidad;
    public Importance importancia;
    public boolean activo = true;

    public ServiceReminder() {}
}
