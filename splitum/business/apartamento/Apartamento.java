/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.apartamento;

import java.util.UUID;

public class Apartamento {
    
    private String id;
    private String morada;
    private String codPostal;
    
    public Apartamento(){
        this.id = UUID.randomUUID().toString();
        morada = "";
        codPostal = "";
    }

    public Apartamento(String id, String morada, String codPostal) {
        this.id = id;
        this.morada = morada;
        this.codPostal = codPostal;
    }
    
    public Apartamento(Apartamento a) {
        id = a.getId();
        morada = a.getMorada();
        codPostal = a.getCodPostal();
    }
    
    public void atualizaDados(String morada, String codPostal){
        this.morada = morada;
        this.codPostal = codPostal;
    }

    public String getId() {
        return id;
    }
    
    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }
    
    @Override
    public Apartamento clone() {
        return new Apartamento(this);
    }
}
