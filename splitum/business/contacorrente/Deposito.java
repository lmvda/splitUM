/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.contacorrente;

import java.time.LocalDate;

public class Deposito extends Movimento {
    public Deposito(String morador, float valor, LocalDate data){
        super(morador, valor, data);
    }
    
    public Deposito(String id, String morador, float valor, LocalDate data){
        super(id, morador, valor, data);
    }
    
    public Deposito(Deposito d) {
        super(d);
    }

    @Override
    public Deposito clone() {
        return new Deposito(this);
    }
}
