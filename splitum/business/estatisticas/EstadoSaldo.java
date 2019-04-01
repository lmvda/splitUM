/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.estatisticas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EstadoSaldo {
    private Map<String, List<EstadoSaldoMorador>> estados;
    private Map<String, Float> valores;

    public EstadoSaldo() {
        estados = new TreeMap<>();
        valores = new TreeMap<>();
    }

    public Map<String, List<EstadoSaldoMorador>> getEstados() {
        return estados;
    }

    public void setEstados(Map<String, List<EstadoSaldoMorador>> estados) {
        this.estados = estados;
    }

    public Map<String, Float> getValores() {
        return valores;
    }
    
    public void addEstadoSaldoMorador(String morador, EstadoSaldoMorador estado) {
        if (!estados.containsKey(morador)) {
            estados.put(morador, new ArrayList<>());
            valores.put(morador, 0f);
        }
        
        estados.get(morador).add(estado);
        
        Float valor = valores.get(morador);
        valor -= estado.getValor();
        valores.put(morador, valor);
    }

    public void addValorMorador(String morador, float valor) {
        if (!valores.containsKey(morador)) {
            valores.put(morador, 0f);
            estados.put(morador, new ArrayList<>());
        }
        
        Float v = valores.get(morador);
        v += valor;
        valores.put(morador, v);
    }
    
    public void addAllMoradores(Collection<String> moradores, float valor) {
        moradores.forEach(m -> {
            if (!valores.containsKey(m)) 
                valores.put(m, valor);
        });
    }
}
