/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

public class PorValor extends Divisao {
    
    private float valor;
    
    public PorValor(String username, float valor){
        super(username);
        
        this.valor = valor;
    }

    public PorValor(float valor, String id, String username) {
        super(id, username);
        this.valor = valor;
    }

    public PorValor(PorValor d) {
        super(d);
        
        this.valor = d.getValor();
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @Override
    public PorValor clone() {
        return new PorValor(this);
    }

    @Override
    public float getValorAposDivisao(float valor) {
        return this.valor;
    }

    @Override
    public float getValorComNovaPopulacao(float valor, float percentagemPopulacao) {
        return valor;
    }
}
