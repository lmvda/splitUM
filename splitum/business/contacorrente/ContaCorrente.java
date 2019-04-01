/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.contacorrente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import splitum.business.estatisticas.EstadoSaldo;
import splitum.business.estatisticas.EstadoSaldoMorador;
import splitum.business.estatisticas.Estatisticas;

public class ContaCorrente {
    
    private String id;
    private float balancoGlobal;
    private List<Movimento> movimentos;
    
    public ContaCorrente(){
        this.id = UUID.randomUUID().toString();
        balancoGlobal = 0;
        this.movimentos = new ArrayList<>();
    }
    
    public ContaCorrente(List<Movimento> movimentos){
        this.id = UUID.randomUUID().toString();
        balancoGlobal = 0;
        this.movimentos = movimentos;
    }

    public ContaCorrente(String id, float balancoGlobal) {
        this.id = id;
        this.balancoGlobal = balancoGlobal;
        this.movimentos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }
    
    public float getBalancoGlobal(){
        return balancoGlobal;
    }

    public List<Movimento> getMovimentos() {
        return movimentos;
    }
    
    public float getBalancoMorador(String morador){
        
        float balanco = 0;
        
        for(Movimento m : movimentos){
            if(morador.equals(m.getMorador())){
                if(m instanceof PagamentoParcela) balanco -= m.getValor();
                else if(m instanceof Deposito) balanco += m.getValor();
            }
        }
        
        return balanco;
    }
    
    public List<Movimento> getMovimentosMorador(String morador) {
        List<Movimento> mov = new ArrayList<>();
        
        movimentos.stream().filter((m) -> (morador.equals(m.getMorador()))).forEach((m) -> {
            mov.add(m.clone());
        });
        
        mov.sort((m1, m2) -> m1.getData().compareTo(m2.getData()));
        
        return mov;
    }
    
    public Estatisticas getEstatisticasMorador(String morador, LocalDate dataInicio, LocalDate dataFim) {
        Estatisticas estatisticas = new Estatisticas();
        
        movimentos.stream().filter((m) -> (m instanceof PagamentoParcela && (m.getData().isAfter(dataInicio) || m.getData().isEqual(dataInicio)) && (m.getData().isBefore(dataFim) || m.getData().isEqual(dataFim)))).forEach((m) -> {
            PagamentoParcela p = (PagamentoParcela) m;
            estatisticas.addDespesa(p.getFatura().getDespesa(), m.getValor());
        });
        
        return estatisticas;
    }
    
    public void depositarValorMorador(String morador, float valor) {
        LocalDate data = LocalDate.now();
        Movimento movimento = new Deposito(morador, valor, data);
        
        movimentos.add(movimento);
        balancoGlobal += valor;
    }
    
    public void pagaDividas(){
        List<PagamentoParcela> parcelasEmDividas = movimentos.stream().filter(m -> m instanceof PagamentoParcela && !((PagamentoParcela)m).estaPaga()).map(p -> (PagamentoParcela) p).collect(Collectors.toList());        
        
        Iterator<PagamentoParcela> it = parcelasEmDividas.iterator();
        
        while (it.hasNext() && balancoGlobal > 0) {
            PagamentoParcela p = it.next();
            
            if (balancoGlobal - p.getValor() > 0) {
                p.setPaga(true);
                this.balancoGlobal -= p.getValor();
            }
        }
    }

    public void addMovimentos(List<Movimento> movimentos) {
        this.movimentos.addAll(movimentos);
    }

    public EstadoSaldo getEstadoSaldoMoradores() {
        EstadoSaldo estado = new EstadoSaldo();
        Map<String, Float> balancoPorMorador = new HashMap<>();
        
        for(Movimento m : movimentos){
            float balanco = 0;
            
            if (balancoPorMorador.containsKey(m.getMorador())) {
                balanco = balancoPorMorador.get(m.getMorador());
            }
            
            if(m instanceof PagamentoParcela) balanco -= m.getValor();
            else if(m instanceof Deposito) balanco += m.getValor();
            
            balancoPorMorador.put(m.getMorador(), balanco);
        }
        
        Map<String, Float> havedores = new HashMap<>();
        Map<String, Float> devedores = new HashMap<>();
        
        balancoPorMorador.forEach((k, v) -> {
            if (v < 0) {
                devedores.put(k, v);
            }
            else if (v > 0) {
                havedores.put(k, v);
                estado.addValorMorador(k, v);
            }
        });
        
        Iterator<Map.Entry<String, Float>> entradasDevedores = devedores.entrySet().iterator();

        while (entradasDevedores.hasNext()) {
            Map.Entry<String, Float> devedor = (Map.Entry<String, Float>) entradasDevedores.next();
            float valorADever = -devedor.getValue();

            if (havedores.isEmpty()) {
                estado.addValorMorador(devedor.getKey(), devedor.getValue());
            }
            else {
                Iterator<Map.Entry<String, Float>> entradasHavedores = havedores.entrySet().iterator();
                while (entradasHavedores.hasNext() && valorADever > 0) {
                    Map.Entry<String, Float> havedor = entradasHavedores.next();
                    float saldo = havedor.getValue() - valorADever;

                    if (saldo > 0) {
                        havedores.put(havedor.getKey(), saldo);
                        estado.addEstadoSaldoMorador(devedor.getKey(), new EstadoSaldoMorador(valorADever, havedor.getKey()));
                        valorADever = 0;
                    }
                    else {
                        entradasHavedores.remove();
                        valorADever = valorADever - havedor.getValue();
                        estado.addEstadoSaldoMorador(devedor.getKey(), new EstadoSaldoMorador(havedor.getValue(), havedor.getKey()));
                        estado.addValorMorador(devedor.getKey(), saldo);
                    }
                }
            }
        }
        
        return estado;
    }
}
