/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

public class Percentual extends Divisao {
    
    private float percentagem;
    
    public Percentual(String username, float percentagem){
        super(username);
        this.percentagem = percentagem;
    }

    public Percentual(float percentagem, String id, String username) {
        super(id, username);
        this.percentagem = percentagem;
    }

    public Percentual(Percentual p) {
        super(p);
        
        this.percentagem = p.getPercentagem();
    }

    public float getPercentagem() {
        return percentagem;
    }

    public void setPercentagem(float percentagem) {
        this.percentagem = percentagem;
    }

    @Override
    public Percentual clone() {
        return new Percentual(this);
    }

    @Override
    public float getValorAposDivisao(float valor) {
        return percentagem * valor;
    }

    @Override
    public float getValorComNovaPopulacao(float valor, float percentagemPopulacao) {
        return (percentagem / percentagemPopulacao) * valor;
    }
}
