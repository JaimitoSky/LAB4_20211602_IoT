package com.example.lab4_20211602_iot.data.model;

public class PaymentHistoryItem {
    public long id;
    public long serviceId;
    public String nombreServicio;
    public double montoPagado;
    public long fechaPagoMs;
    public long fechaVencOriginalMs;
    public int anticipacionDias;

    public PaymentHistoryItem() {}
}
