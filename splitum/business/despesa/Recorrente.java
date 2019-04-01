/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.time.LocalDate;
import splitum.business.contacorrente.PagamentoParcela;
import splitum.business.apartamento.Ausencia;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Recorrente extends Despesa {
    
   private Periodicidade periodicidade;
   private boolean ausencia;
   
    public Recorrente(String descricao, float valor, List<Divisao> divisoes, Periodicidade periodicidade, boolean ausencia){
        super(descricao, valor, divisoes);
        this.periodicidade = periodicidade;
        this.ausencia = ausencia;
    }
    
    public Recorrente(String id, String descricao, float valor, boolean ativa, Periodicidade periodicidade, boolean ausencia){
        super(id, descricao, valor, ativa);
        this.periodicidade = periodicidade;
        this.ausencia = ausencia;
    }

    public Periodicidade getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Periodicidade periodicidade) {
        this.periodicidade = periodicidade;
    }

    public boolean isAusencia() {
        return ausencia;
    }

    public void setAusencia(boolean ausencia) {
        this.ausencia = ausencia;
    }
    
    public LocalDate getDataUltimaFatura() {
        Fatura fatura = super.getUltimaFatura();
        LocalDate dataUltimaFatura = null;
        
        if (fatura != null) {
            dataUltimaFatura = fatura.getData();
        }
        
        return dataUltimaFatura;
    }
    
    public boolean deveSerGeradaFatura() {
        if (!isAtiva()) {
            return false;
        }
        
        LocalDate dataUltimaFatura = getDataUltimaFatura();
        boolean result = false;
        
        if (dataUltimaFatura != null) {
            LocalDate agora = LocalDate.now();
            LocalDate dataSupostaDaUltimaFatura = agora.minus(periodicidade.getValue());

            if (dataSupostaDaUltimaFatura.isAfter(dataUltimaFatura) || dataSupostaDaUltimaFatura.isEqual(dataUltimaFatura)) {
                result = true;
            }
        }
        
        return result;
    }
    
    @Override
    public Fatura gerarFatura(LocalDate data, Map<String, List<Ausencia>> ausencias) {
        if (!ausencia) {
            return super.gerarFatura(data, ausencias);
        }
        
        float valor = super.getValor();
        float valorComAusencias = super.getValor();
        List<Divisao> divisoes = super.getDivisoes();
        Fatura fatura = new Fatura(valor, data, this);
        
        List<Divisao> divisoesMoradoresComAusencias = divisoes.stream().filter(d -> percentagemTempoPresente(ausencias.get(d.getUsername()), data) < 1).collect(Collectors.toList());
        List<Divisao> divisoesMoradoresSemAusencias = divisoes.stream().filter(d -> percentagemTempoPresente(ausencias.get(d.getUsername()), data) == 1).collect(Collectors.toList());

        for (Divisao divisao : divisoesMoradoresComAusencias) {
            float valorAposDivisao = divisao.getValorAposDivisao(valor);
            
            if (valorAposDivisao > 0) {
                float valorAPagar = valorAposDivisao;
                
                if (!divisoesMoradoresSemAusencias.isEmpty()) {
                    float percentagemTempoPresente = percentagemTempoPresente(ausencias.get(divisao.getUsername()), data);
                    valorAPagar = valorAposDivisao * percentagemTempoPresente;
                }
                
                PagamentoParcela p = new PagamentoParcela(divisao.getUsername(), valorAPagar, data, fatura);
                fatura.addParcela(p);
                
                valorComAusencias = valorComAusencias - valorAPagar;
            }
        }
        
        for (Divisao divisao : divisoesMoradoresSemAusencias) {
            float percentagemMoradoresParaDivisao = divisoes.size()*1.0f/divisoesMoradoresSemAusencias.size();
            float valorAposDivisao = divisao.getValorComNovaPopulacao(valorComAusencias, percentagemMoradoresParaDivisao);
            
            if (valorAposDivisao > 0) {
                float valorAPagar = valorAposDivisao;
                
                if (divisao instanceof PorValor) {
                    valorAPagar = valorComAusencias / divisoesMoradoresSemAusencias.size();
                }
                
                PagamentoParcela p = new PagamentoParcela(divisao.getUsername(), valorAPagar, data, fatura);
                fatura.addParcela(p);
            }
        }
        
        super.addFatura(fatura);
        
        return fatura;
    }
    
    private float percentagemTempoPresente(List<Ausencia> ausencias, LocalDate data) {
        if (ausencias == null) return 1;
        
        float percentagem = 1;
        LocalDate dataInicio = data;
        LocalDate dataFim = data.plus(periodicidade.getValue());
        
        Iterator<Ausencia> it = ausencias.iterator();
        
        while (it.hasNext() && percentagem == 1) {
            Ausencia a = it.next();
            
            percentagem = a.percentagemTempoPresente(dataInicio, dataFim);
        }
        
        return percentagem;
    }
}
