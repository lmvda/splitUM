/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.util.List;

public class Extraordinaria extends Despesa {
     
    public Extraordinaria(String descricao, float valor, List<Divisao> divisoes){
        super(descricao, valor, divisoes);
    }
    
    public Extraordinaria(String id, String descricao, float valor, boolean ativa){
        super(id, descricao, valor, ativa);
    }

    @Override
    public String getNome() {
        return "Extraordinária";
    }
}
