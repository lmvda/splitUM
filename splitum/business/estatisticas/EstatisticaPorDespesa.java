/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.estatisticas;

public class EstatisticaPorDespesa {
    private String nome;
    private float total;
    private int numeroDespesas;

    public EstatisticaPorDespesa(String nome, float total) {
        this.nome = nome;
        this.total = total;
        numeroDespesas = 1;
    }
    
    public void actualiza(float valor) {
        this.total += valor;
        numeroDespesas++;
    }

    public String getNome() {
        return nome;
    }

    public float getTotal() {
        return total;
    }
    
    public float getMedia() {
        return total/numeroDespesas;
    }
}
