/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.estatisticas;

public class EstadoSaldoMorador {
    private float valor;
    private String morador;

    public EstadoSaldoMorador(float valor, String morador) {
        this.valor = valor;
        this.morador = morador;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getMorador() {
        return morador;
    }

    public void setMorador(String morador) {
        this.morador = morador;
    }
}
