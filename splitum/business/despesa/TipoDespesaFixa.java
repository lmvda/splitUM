/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

public enum TipoDespesaFixa {
    RENDA,
    NETFLIX,
    OUTRA;
    
    public static String[] getArrayString() {
        String[] tipos = new String[3];
        
        tipos[0] = TipoDespesaFixa.RENDA.toString();
        tipos[1] = TipoDespesaFixa.NETFLIX.toString();
        tipos[2] = TipoDespesaFixa.OUTRA.toString();
        
        return tipos;
    }
    
}
