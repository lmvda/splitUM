/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.apartamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Morador {
    
    private String username;
    private String password;
    private String nome;
    private String email;
    private Boolean ativo;
    private List<Ausencia> ausencias;
    
    
    public Morador(String username, String password, String nome, String email, Boolean ativo){
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
        ausencias = new ArrayList<>();
    }

    public Morador(String username, String password, String nome, String email) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
        this.ativo = true;
        this.ausencias = new ArrayList<>();
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ausencia> getAusencias() {
        return ausencias;
    }

    public void setAusencias(List<Ausencia> ausencias) {
        this.ausencias = ausencias;
    }
    
    public boolean loginValido(String password){
        return this.password.equals(password);
    }
    
    public void atualizaDados(String password, String nome, String email){
        this.password = password;
        this.nome = nome;
        this.email = email;
    }
    
    public void registaIntervaloAusencia(LocalDate dataInicio, LocalDate dataFim) {
        Iterator<Ausencia> it = ausencias.iterator();
        boolean encontrado = false;
        
        while (it.hasNext() && !encontrado) {
            Ausencia ausencia = it.next();
            
            if (!ausencia.novoIntervaloNecessario(dataInicio, dataFim)) {
                ausencia.atualiza(dataInicio, dataFim);
                encontrado = true;
            }
        }
        
        if (!encontrado) {
            Ausencia ausencia = new Ausencia(dataInicio, dataFim);
            ausencias.add(ausencia);
        }
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    
    
}
