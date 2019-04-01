/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.contacorrente;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Movimento {
    
    private String id;
    private String morador;
    private float valor;
    private LocalDate data;

    public Movimento(String morador, float valor, LocalDate data) {
        this.id = UUID.randomUUID().toString();
        this.morador = morador;
        this.valor = valor;
        this.data = data;
    }
    
    public Movimento(String id, String morador, float valor, LocalDate data){
        this.id = id;
        this.morador = morador;
        this.valor = valor;
        this.data = data;
    }
    
    public Movimento(Movimento m){
        morador = m.getMorador();
        valor = m.getValor();
        data = m.getData();
    }

    public String getId() {
        return id;
    }
    
    public String getMorador() {
        return morador;
    }

    public void setMorador(String morador) {
        this.morador = morador;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
    
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public abstract Movimento clone();
}
