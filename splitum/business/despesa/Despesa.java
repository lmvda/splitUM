/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.time.LocalDate;
import splitum.business.contacorrente.PagamentoParcela;
import splitum.business.apartamento.Ausencia;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Despesa {
    
    private String id;
    private String descricao;
    private float valor;
    private boolean ativa;
    private List<Divisao> divisoes;
    private List<Fatura> faturas;
    
    public Despesa(String descricao, float valor, List<Divisao> divisoes){
        this.id = UUID.randomUUID().toString();
        this.descricao = descricao;
        this.valor = valor;
        this.divisoes = divisoes;
        faturas = new ArrayList<>();
        ativa = true;
    }
    
    public Despesa(String id, String descricao, float valor, boolean ativa){
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.divisoes = new ArrayList<>();
        faturas = new ArrayList<>();
        this.ativa = ativa;
    }
    
    //Gets
    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public float getValor() {
        return valor;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public List<Divisao> getDivisoes() {
        List<Divisao> div = new ArrayList<>();
        
        for(Divisao d : divisoes){
            div.add(d.clone());
        }
        
        return div;
    }

    public List<Fatura> getFaturas() {
        List<Fatura> fat = new ArrayList<>();
        for(Fatura f : faturas){
            fat.add(f);
        }
        
        return fat;
    }

    public void addFatura(Fatura fatura) {
        faturas.add(fatura);
    }
    
    //Sets
    public void setId(String id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void setDivisoes(List<Divisao> divisoes) {
        this.divisoes = divisoes;
    }

    public void setFaturas(List<Fatura> faturas) {
        faturas.sort((f1, f2) -> f1.getData().compareTo(f2.getData()));
        
        this.faturas = faturas;
    }
    
    //Métodos
    public void inativaDespesa(){
        ativa = false;
    }
    
    public abstract String getNome();
    
    public Fatura gerarFatura(LocalDate data, Map<String, List<Ausencia>> ausencias) {
        Fatura fatura = new Fatura(valor, data, this);
        
        for (Divisao divisao : divisoes) {
            float valorAposDivisao = divisao.getValorAposDivisao(valor);
            if (valorAposDivisao > 0) {
                PagamentoParcela p = new PagamentoParcela(divisao.getUsername(), valorAposDivisao, data, fatura);
                fatura.addParcela(p);
            }
        }
        
        faturas.add(fatura);
        
        return fatura;
    }
    
    
    public Fatura getUltimaFatura() {
        
        Fatura f = null;
        if (faturas.size() > 0) {
            f = faturas.get(faturas.size()-1);
        }
        
        return f;
    }
    
}
