package com.redhat.lab.controllers;

import com.redhat.lab.dto.Transaccion;
import com.redhat.lab.service.TransaccionService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaccion")
public class TransaccionController {
	
	@Autowired
	private TransaccionService transaccionService;
	
    @RequestMapping(value = "/list", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public List<Transaccion> getAllTransactions() {
        List<Transaccion> list = transaccionService.getAllTransactions();
        return list;
    }
    
    @RequestMapping(value = "/show/{tarjetaDeCredito}", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public List<Transaccion> getTransactionsByCreditCard(@PathVariable("tarjetaDeCredito") String tarjetaDeCredito) {
        return transaccionService.getTransactionsByCreditCard(tarjetaDeCredito);
    }
    
    @RequestMapping(value = "/create", //
            method = RequestMethod.POST, //
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public HashMap<String, String> addTransaction(@RequestBody Transaccion transaccion) {
 
        System.out.println("(Service Side) Registrando Transaccion: " + transaccion.toString());
 
        return transaccionService.addTransaction(transaccion);
    }

}
