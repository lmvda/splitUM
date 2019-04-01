/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business;

import java.time.LocalDate;
import splitum.business.estatisticas.Estatisticas;
import splitum.business.contacorrente.ContaCorrente;
import splitum.business.despesa.Despesa;
import splitum.business.despesa.Divisao;
import splitum.business.despesa.Fixa;
import splitum.business.despesa.Extraordinaria;
import splitum.business.despesa.ExistemDividasExcpetion;
import splitum.business.despesa.Fatura;
import splitum.business.despesa.Periodicidade;
import splitum.business.despesa.Recorrente;
import splitum.business.contacorrente.Movimento;
import splitum.business.despesa.TipoDespesaVariavel;
import splitum.business.despesa.TipoDespesaFixa;
import splitum.business.despesa.Variavel;
import splitum.business.apartamento.PasswordInvalidaException;
import splitum.business.apartamento.MoradorInexistenteException;
import splitum.business.apartamento.Morador;
import splitum.business.apartamento.Ausencia;
import splitum.business.apartamento.Apartamento;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;
import splitum.business.apartamento.MoradorExistenteException;
import splitum.business.estatisticas.EstadoSaldo;
import splitum.data.ApartamentoDAO;
import splitum.data.ContaCorrenteDAO;
import splitum.data.DespesaDAO;
import splitum.data.MoradorDAO;

public class SplitUM extends Observable {
    
    private Map<String, Apartamento> apartamentos;
    private Map<String, Morador> moradores;
    private Map<String, Despesa> despesas;
    private Map<String, ContaCorrente> contasCorrentes;
    
    private String moradorAutenticado;
    private Apartamento apartamento;
    private ContaCorrente conta;

    public SplitUM() {
        apartamentos = new ApartamentoDAO();
        moradores = new MoradorDAO();
        despesas = new DespesaDAO();
        contasCorrentes = new ContaCorrenteDAO(new DespesaDAO());
        
        moradorAutenticado = null;
        
        if (apartamentos.isEmpty()) {
            apartamento = new Apartamento();
        }
        else {
            apartamento = apartamentos.values().iterator().next();
        }
        
        if (contasCorrentes.isEmpty()) {
            conta = new ContaCorrente();
        }
        else {
            conta = contasCorrentes.values().iterator().next();
        }
    }

    public void registaUtilizador(String username, String password, String nome, String email) throws MoradorExistenteException {
        if (moradores.containsKey(username)) {
            throw new MoradorExistenteException("Morador já existe!");
        }
        
        Morador novoMorador = new Morador(username, password, nome, email);
        
        moradores.put(username, novoMorador);
    }
    
    public void login(String username, String password) throws PasswordInvalidaException, MoradorInexistenteException {
    
        Morador morador = moradores.get(username);
        
        if (morador == null) throw new MoradorInexistenteException("O morador inserido nao existe!");
        
        if (!morador.isAtivo()) throw new MoradorInexistenteException("O morador inserido foi desativado!");
        
        boolean valido = morador.loginValido(password);
        
        if (valido == false) throw new PasswordInvalidaException("Password incorreta!");
        
        moradorAutenticado = username;
        
        rotinaReveRecorrentes();
        
        notifyObservers();
    }
    
    
    public Morador getMoradorAutenticado(){
        if(moradorAutenticado == null) {
            return null;
        } else {
            return moradores.get(moradorAutenticado);
        }
    }
    
    public void atualizaDadosMorador(String password, String nome, String email){
        Morador morador = moradores.get(moradorAutenticado);
        morador.atualizaDados(password, nome, email);
        
        moradores.put(moradorAutenticado, morador);
        notifyObservers();
    }
    
    public void eliminaConta() throws ExistemDividasExcpetion {

        float balanco = conta.getBalancoMorador(moradorAutenticado);
        
        if (balanco < 0) throw new ExistemDividasExcpetion("Impossivel eliminar pois existem dividas!");
        
        Morador m = moradores.get(moradorAutenticado);
        m.setAtivo(false);
        moradores.put(moradorAutenticado, m);
        moradorAutenticado = null;
        notifyObservers();
    }
    
    public float getContaCorrente() {
        float balanco = conta.getBalancoMorador(moradorAutenticado);
        
        return balanco;
    }
    
    
    public List<Movimento> getMovimentosMorador(){
        return conta.getMovimentosMorador(moradorAutenticado);
    }
    
    public Estatisticas getEstatisticasMorador(LocalDate dataInicio, LocalDate dataFim){
        return conta.getEstatisticasMorador(moradorAutenticado, dataInicio, dataFim);
    }
    
    public void depositarValor(float valor){
        conta.depositarValorMorador(moradorAutenticado, valor);
        
        rotinaLiquidaDividas();
        notifyObservers();
    }
    
    public Apartamento getApartamento() {
        Apartamento ap = apartamento.clone();
        
        return ap;
    }
    
    public void logout(){
        moradorAutenticado = null;
        notifyObservers();
    }
    
    public void atualizaDadosApartamento(String morada, String codPostal){
        apartamento.atualizaDados(morada, codPostal);
        
        apartamentos.put(apartamento.getId(), apartamento);
        notifyObservers();
    }
    
    public void apagaDespesa(String idDespesa){    
        Despesa despesa = despesas.get(idDespesa);
        
        despesa.inativaDespesa();
        
        despesas.put(idDespesa, despesa);
        notifyObservers();
    }
    
    public void registaIntervaloAusencia(LocalDate dataInicio, LocalDate dataFim){
        Morador morador = moradores.get(moradorAutenticado);
        morador.registaIntervaloAusencia(dataInicio, dataFim);
        
        moradores.put(moradorAutenticado, morador);
        notifyObservers();
    }
    
    //public Collection<Morador> getMoradores() {
    //    return moradores.values();
    //}
    public List<Morador> getMoradores() {
        return moradores.values().stream().filter(d -> d.isAtivo()).collect(Collectors.toList());
    }
    
    public void adicionaDespesaRecorrenteFixa(TipoDespesaFixa tipo, String descricao, float valor, List<Divisao> divisoes, Periodicidade periodicidade, boolean ausencia){
        Despesa despesa = new Fixa(tipo, descricao, valor, divisoes, periodicidade, ausencia);
        
        despesas.put(despesa.getId(), despesa);
        geraFatura(despesa, LocalDate.now());
        notifyObservers();
    }
    
    public void adicionaDespesaRecorrenteVariavel(TipoDespesaVariavel tipo, String descricao, float valor, List<Divisao> divisao, Periodicidade periodicidade, boolean ausencia){
        Despesa despesa = new Variavel(tipo, descricao, valor, divisao, periodicidade, ausencia);
        
        despesas.put(despesa.getId(), despesa);
        geraFatura(despesa, LocalDate.now());
        notifyObservers();
    }
    
    public void adicionaDespesaExtraordinaria(String descricao, float valor, List<Divisao> divisao){
        Despesa despesa = new Extraordinaria(descricao, valor, divisao);
        
        despesas.put(despesa.getId(), despesa);
        geraFatura(despesa, LocalDate.now());
        notifyObservers();
    }
    
    public Fixa getDespesaRecorrenteFixa(String idDespesa) {
        Fixa despesaFixa = (Fixa) despesas.get(idDespesa);
        
        return despesaFixa;
    }
    
    public Variavel getDespesaRecorrenteVariavel(String idDespesa){
        Variavel despesaVariavel = (Variavel) despesas.get(idDespesa);
        
        return despesaVariavel;
    }
    
    public Extraordinaria getDespesaExtraordinaria(String idDespesa) {
        Extraordinaria despesaExtraordinaria = (Extraordinaria) despesas.get(idDespesa);
        return despesaExtraordinaria;
    }
    
    public void alteraDespesaRecorrenteFixa(String idDespesa, TipoDespesaFixa tipo, String descricao, float valor, List<Divisao> divisoes, Periodicidade periodicidade, boolean ausencia){
        Fixa despesa = getDespesaRecorrenteFixa(idDespesa);
        
        despesa.setDescricao(descricao);
        despesa.setValor(valor);
        despesa.setDivisoes(divisoes);
        despesa.setPeriodicidade(periodicidade);
        despesa.setAusencia(ausencia);
        despesa.setTipo(tipo);
        
        despesas.put(idDespesa, despesa);
        notifyObservers();
    }
    
    public void alteraDespesaRecorrenteVariavel(String idDespesa, TipoDespesaVariavel tipo, String descricao, float valor, List<Divisao> divisoes, Periodicidade periodicidade, boolean ausencia){
        Variavel despesa = getDespesaRecorrenteVariavel(idDespesa);
        
        despesa.setDescricao(descricao);
        despesa.setValor(valor);
        despesa.setDivisoes(divisoes);
        despesa.setPeriodicidade(periodicidade);
        despesa.setAusencia(ausencia);
        despesa.setTipo(tipo);
        
        despesas.put(idDespesa, despesa);
        notifyObservers();
    }
    
    public void alteraDespesaExtraordinaria(String idDespesa, String descricao, float valor, List<Divisao> divisoes){
        Extraordinaria despesa = getDespesaExtraordinaria(idDespesa);
        
        despesa.setDescricao(descricao);
        despesa.setValor(valor);
        despesa.setDivisoes(divisoes);
        
        despesas.put(idDespesa, despesa);
        notifyObservers();
    }
    
    public void defineValorDespesaRecorrenteVariavel(String idDespesa, float valor){
        Variavel despesa = getDespesaRecorrenteVariavel(idDespesa);
        despesa.setValor(valor);
        
        LocalDate dataUltimaFatura = despesa.getDataUltimaFatura();
        LocalDate dataNovaFatura = dataUltimaFatura == null ? LocalDate.now() : dataUltimaFatura;
        
        despesas.put(idDespesa, despesa);
        geraFatura(despesa, dataNovaFatura);
        notifyObservers();
    }
    
    public void inativaDespesa(String idDespesa){
        Despesa despesa = despesas.get(idDespesa);
        despesa.inativaDespesa();   
        
        despesas.put(idDespesa, despesa);
        notifyObservers();
    }
    
    public List<Despesa> getDespesas() {
        return despesas.values().stream().filter(d -> d.isAtiva()).collect(Collectors.toList());
    }
    
    public float getBalancoGlobal() {
        return conta.getBalancoGlobal();
    }
    
    public EstadoSaldo getEstadoSaldoMoradores() {
        EstadoSaldo estado = conta.getEstadoSaldoMoradores();
        Set<String> moradores = this.moradores.keySet();
        
        estado.addAllMoradores(moradores, 0);
        
        return estado;
    }
    
    private void rotinaLiquidaDividas(){
        conta.pagaDividas();
        
        contasCorrentes.put(conta.getId(), conta);
        notifyObservers();
    }
    
    private void geraFatura(Despesa despesa, LocalDate data) {
        Map<String, List<Ausencia>> ausencias = moradores.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, 
                                          e -> e.getValue().getAusencias()));
        
        Fatura fatura = despesa.gerarFatura(data, ausencias);
        despesas.put(despesa.getId(), despesa);

        List<Movimento> parcelas = fatura.getParcelas().stream().map(m -> (Movimento) m).collect(Collectors.toList());
        
        conta.addMovimentos(parcelas);

        rotinaLiquidaDividas();
    }
    
    private void rotinaReveRecorrentes() {
        for (Despesa despesa : despesas.values()) {
            if (despesa instanceof Recorrente) {
                Recorrente recorrente = (Recorrente) despesa;
                
                if (recorrente.deveSerGeradaFatura()) {
                    LocalDate dataUltimaFatura = recorrente.getDataUltimaFatura();
                    LocalDate dataProximaFatura = dataUltimaFatura.plus(recorrente.getPeriodicidade().getValue());
                    
                    geraFatura(despesa, dataProximaFatura);
                }
            }
        }
    }
    
    @Override
    public void notifyObservers(){
        setChanged();
        super.notifyObservers();
    }
}
