/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.time.LocalDate;
import splitum.business.contacorrente.PagamentoParcela;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Fatura {
    
    private String id;
    private float valor;
    private LocalDate data;
    private Despesa despesa;
    private List<PagamentoParcela> parcelas;
    
    public Fatura(float valor, LocalDate data, Despesa despesa) {
        this.id = UUID.randomUUID().toString();
        this.valor = valor;
        this.data = data;
        this.despesa = despesa;
        parcelas = new ArrayList<>();
    }
    
    public Fatura(String id, float valor, LocalDate data) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        parcelas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean estaPaga() {
        return parcelas.stream().allMatch(p -> p.estaPaga());
    }

    public Despesa getDespesa() {
        return despesa;
    }

    public void setDespesa(Despesa despesa) {
        this.despesa = despesa;
    }
    
    public void addParcela(PagamentoParcela p) {
        parcelas.add(p);
    }

    public List<PagamentoParcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<PagamentoParcela> parcelas) {
        this.parcelas = parcelas;
    }
}
