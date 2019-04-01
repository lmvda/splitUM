/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.estatisticas;

import splitum.business.despesa.Despesa;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Estatisticas {
    
    private float totalDespesas;
    private Map<String, EstatisticaPorDespesa> estatisticasPorDespesa;
    
    public Estatisticas(){
        totalDespesas = 0;
        estatisticasPorDespesa = new HashMap<>();
    }
    
    public void addDespesa(Despesa despesa, float valor){
        totalDespesas += valor;
        String nome = despesa.getNome();
        
        if (estatisticasPorDespesa.containsKey(nome)) {
            EstatisticaPorDespesa estatisticaDespesa = estatisticasPorDespesa.get(nome);
            
            estatisticaDespesa.actualiza(valor);
        }
        else {
            EstatisticaPorDespesa estatisticaDespesa = new EstatisticaPorDespesa(nome, valor);
            estatisticasPorDespesa.put(nome, estatisticaDespesa);
        }
    }
    
    public Collection<EstatisticaPorDespesa> getEstatisticasPorDespesa() {
        return estatisticasPorDespesa.values();
    }

    public float getTotalDespesas() {
        return totalDespesas;
    }
}
