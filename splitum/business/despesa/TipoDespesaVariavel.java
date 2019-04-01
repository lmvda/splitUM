/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.business.despesa;

public enum TipoDespesaVariavel {
    AGUA,
    LUZ,
    GAS,
    INTERNET,
    ALIMENTACAO,
    OUTRA;
    
    
    public static String[] getArrayString() {
        String[] tipos = new String[6];
        
        tipos[0] = TipoDespesaVariavel.AGUA.toString();
        tipos[1] = TipoDespesaVariavel.LUZ.toString();
        tipos[2] = TipoDespesaVariavel.GAS.toString();
        tipos[3] = TipoDespesaVariavel.INTERNET.toString();
        tipos[4] = TipoDespesaVariavel.ALIMENTACAO.toString();
        tipos[5] = TipoDespesaVariavel.OUTRA.toString();
        
        return tipos;
    }
}
