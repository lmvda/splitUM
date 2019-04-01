/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.util.UUID;

public abstract class Divisao {
    
    private String id;
    private String username;
    
    public Divisao(String username){
        this.id = UUID.randomUUID().toString();
        this.username = username;
    }

    public Divisao(String id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public Divisao(Divisao d){
        id = d.getId();
        username = d.getUsername();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public abstract Divisao clone();
    
    public abstract float getValorAposDivisao(float valor);
    
    public abstract float getValorComNovaPopulacao(float valor, float percentagemPopulacao);
}
