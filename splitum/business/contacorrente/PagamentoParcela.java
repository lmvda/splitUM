/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.contacorrente;

import java.time.LocalDate;
import splitum.business.despesa.Fatura;

public class PagamentoParcela extends Movimento {
    
    private Fatura fatura;
    private boolean paga;
    
    public PagamentoParcela(String morador, float valor, LocalDate data, Fatura fatura) {
        super(morador, valor, data);
        this.fatura = fatura;
        paga = false;
    }

    public PagamentoParcela(String id, String morador, float valor, LocalDate data, Fatura fatura, boolean paga) {
        super(id, morador, valor, data);
        this.fatura = fatura;
        this.paga = paga;
    }
    
    public PagamentoParcela(PagamentoParcela p) {
        super(p);
        
        fatura = p.getFatura();
        paga = p.estaPaga();
    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public boolean estaPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }
    
    @Override
    public PagamentoParcela clone() {
        return new PagamentoParcela(this);
    }
}
