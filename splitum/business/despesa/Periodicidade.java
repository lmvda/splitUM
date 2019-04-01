/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

import java.time.Period;
import java.time.temporal.TemporalAmount;

public enum Periodicidade {
    DIARIAMENTE(Period.ofDays(1)),
    SEMANALMENTE(Period.ofDays(7)),
    QUINZENALMENTE(Period.ofDays(7)),
    MENSALMENTE(Period.ofMonths(1)),
    TRIMESTRALMENTE(Period.ofMonths(3)),
    SEMESTRALMENTE(Period.ofMonths(6)),
    ANUALMENTE(Period.ofYears(1));
    
    private final TemporalAmount dias;
    Periodicidade(TemporalAmount dias) { this.dias = dias; }
    public TemporalAmount getValue() { return dias; }
    
    public static String[] getArrayString() {
        String[] periodicidades = new String[7];
        
        periodicidades[0] = Periodicidade.DIARIAMENTE.toString();
        periodicidades[1] = Periodicidade.SEMANALMENTE.toString();
        periodicidades[2] = Periodicidade.QUINZENALMENTE.toString();
        periodicidades[3] = Periodicidade.MENSALMENTE.toString();
        periodicidades[4] = Periodicidade.TRIMESTRALMENTE.toString();
        periodicidades[5] = Periodicidade.SEMESTRALMENTE.toString();
        periodicidades[6] = Periodicidade.ANUALMENTE.toString();
        
        return periodicidades;
    }
    
    
}
