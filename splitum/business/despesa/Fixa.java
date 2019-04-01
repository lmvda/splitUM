/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.util.List;

public class Fixa extends Recorrente {
    
    private TipoDespesaFixa tipo;
    
    public Fixa(TipoDespesaFixa tipo, String descricao, float valor, List<Divisao> divisoes, Periodicidade periodicidade, boolean ausencia){
        super(descricao, valor, divisoes, periodicidade, ausencia);
        this.tipo = tipo;
    }
    
    public Fixa(String id, TipoDespesaFixa tipo, String descricao, float valor, boolean ativa, Periodicidade periodicidade, boolean ausencia){
        super(id, descricao, valor, ativa, periodicidade, ausencia);
        this.tipo = tipo;
    }

    public TipoDespesaFixa getTipo() {
        return tipo;
    }

    public void setTipo(TipoDespesaFixa tipo) {
        this.tipo = tipo;
    }

    @Override
    public String getNome() {
        return tipo.toString();
    }
}


