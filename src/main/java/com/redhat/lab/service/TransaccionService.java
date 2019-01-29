package com.redhat.lab.service;

import com.redhat.lab.dto.Transaccion;
import com.redhat.lab.cache.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TransaccionService {
	
	  private static final String tarjetasKey = "tdc";

	private RestCache<String, Object> cache;

	public TransaccionService() {
		cache = new RestCache<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public List<Transaccion> getAllTransactions() {
		List<Transaccion> lista = new ArrayList<Transaccion>();
        List<String> tarjetas = (List<String>) cache.get(tarjetasKey);
        if (tarjetas != null) {
            for (String tarjeta : tarjetas) {
                List<Transaccion> listaTemporal = (List<Transaccion>) cache.get(encode(tarjeta));
                lista.addAll(listaTemporal);
            }
        }
        return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Transaccion> getTransactionsByCreditCard(String tarjetaDeCredito) {
		return (List<Transaccion>) cache.get(tarjetaDeCredito);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> addTransaction(Transaccion transaccion) {
		HashMap<String, String> respuesta = new HashMap<String, String>();
		respuesta.put("registrado", "false");
		List<Transaccion> lista = getTransactionsByCreditCard(transaccion.getTarjetaDeCredito());
		if (lista == null) {
			lista = new ArrayList<Transaccion>();
		}
		lista.add(transaccion);
		cache.put(encode(transaccion.getTarjetaDeCredito()), lista);
		
		List<String> tarjetas = (List<String>) cache.get(tarjetasKey);
		if(tarjetas == null) {
			tarjetas = new ArrayList<String>();
		}
		
		if (!tarjetas.contains(transaccion.getTarjetaDeCredito())){
			tarjetas.add(transaccion.getTarjetaDeCredito());
			cache.put(tarjetasKey, tarjetas);
		}
		respuesta.put("registrado", "true");
		respuesta.put("mensaje", "Transaccion cargada con exito");
		return respuesta;
	}

	public static String encode(String key) {
		try {
			return URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
