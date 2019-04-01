/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.time.LocalDate;
import splitum.business.apartamento.Ausencia;
import java.util.List;
import java.util.Map;

public class Variavel extends Recorrente {
    
    private boolean novoValorDefinido;
    private TipoDespesaVariavel tipo;
    
    public Variavel(TipoDespesaVariavel tipo, String descricao, float valor, List<Divisao> divisoes, Periodicidade periodicidade, boolean ausencia){
        super(descricao, valor, divisoes, periodicidade, ausencia);
        novoValorDefinido = true;
        this.tipo = tipo;
    }
    
    public Variavel(String id, TipoDespesaVariavel tipo, String descricao, float valor, boolean ativa, Periodicidade periodicidade, boolean ausencia, boolean novoValorDefinido){
        super(id, descricao, valor, ativa, periodicidade, ausencia);
        this.novoValorDefinido = novoValorDefinido;
        this.tipo = tipo;
    }

    public boolean isNovoValorDefinido() {
        return novoValorDefinido;
    }

    public TipoDespesaVariavel getTipo() {
        return tipo;
    }

    public void setTipo(TipoDespesaVariavel tipo) {
        this.tipo = tipo;
    }
    

    @Override
    public void setValor(float valor) {
        super.setValor(valor);
        
        novoValorDefinido = true;
    }

    @Override
    public Fatura gerarFatura(LocalDate data, Map<String, List<Ausencia>> ausencias) {
        novoValorDefinido = false;
        
        return super.gerarFatura(data, ausencias);
    }
    
    @Override
    public boolean deveSerGeradaFatura() {
        if (novoValorDefinido) {
            return super.deveSerGeradaFatura();
        }
        else {
            return false;
        }
    }

    @Override
    public String getNome() {
        return tipo.toString();
    }
}
