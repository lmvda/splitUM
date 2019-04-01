/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum;

import splitum.business.SplitUM;
import splitum.presentation.JFramePrincipal;

public class Main {
    public static void main(String[] args) {
        
        // inicializacao
        SplitUM splitUM = new SplitUM();
        
        // janelas
        JFramePrincipal view = new JFramePrincipal(splitUM);
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }
}
