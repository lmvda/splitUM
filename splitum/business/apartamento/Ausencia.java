/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.apartamento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Ausencia {
    private String id;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Ausencia(LocalDate dataInicio, LocalDate dataFim) {
        this.id = UUID.randomUUID().toString();
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Ausencia(String id, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String getId() {
        return id;
    }
    
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
    
    public boolean novoIntervaloNecessario(LocalDate dataInicioIntervalo, LocalDate dataFimIntervalo) {
        if (dataInicio.isAfter(dataFimIntervalo) || dataFim.isBefore(dataInicioIntervalo)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void atualiza(LocalDate dataInicioIntervalo, LocalDate dataFimIntervalo) {
        dataInicio = dataInicio.isBefore(dataInicioIntervalo) ? dataInicio : dataInicioIntervalo;
        dataFim = dataFim.isAfter(dataFimIntervalo) ? dataFim : dataFimIntervalo;
    }
    
    public float percentagemTempoPresente(LocalDate dataInicioIntervalo, LocalDate dataFimIntervalo) {
        long diasTotalIntervalo = ChronoUnit.DAYS.between(dataInicioIntervalo, dataFimIntervalo);

        if (dataInicio.isAfter(dataFimIntervalo) || dataFim.isBefore(dataInicioIntervalo)) {
            return 1;
        }
        
        LocalDate d1 = dataInicio.isBefore(dataInicioIntervalo) ? dataInicioIntervalo : dataInicio;
        LocalDate d2 = dataFim.isAfter(dataFimIntervalo) ? dataFimIntervalo : dataFim;
        
        long diasPresente = ChronoUnit.DAYS.between(dataInicioIntervalo, d1) + ChronoUnit.DAYS.between(d2, dataFimIntervalo);
        
        return diasPresente*1.0f / diasTotalIntervalo;
    }
}
