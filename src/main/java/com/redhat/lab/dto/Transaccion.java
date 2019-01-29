package com.redhat.lab.dto;

import java.io.Serializable;
import java.util.Date;

public class Transaccion implements Serializable {
	
	private static final long serialVersionUID = 1234567890L;
	
	Date fechaDeCompra;
	String articulo;
	Integer cantidad;
	Double importe;
	String tarjetaDeCredito;
	
	public Transaccion() {}
	
	public Transaccion(Date fecheDeCompra, String articulo, Integer cantidad, Double importe, String tarjetaDeCredito) {}
	
	public Date getFechaDeCompra() {
		return fechaDeCompra;
	}
	public void setFechaDeCompra(Date fechaDeCompra) {
		this.fechaDeCompra = fechaDeCompra;
	}
	public String getArticulo() {
		return articulo;
	}
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public String getTarjetaDeCredito() {
		return tarjetaDeCredito;
	}
	public void setTarjetaDeCredito(String tarjetaDeCredito) {
		this.tarjetaDeCredito = tarjetaDeCredito;
	}

}
