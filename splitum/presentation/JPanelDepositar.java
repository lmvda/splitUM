/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.presentation;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JPanel;
import splitum.business.SplitUM;

public class JPanelDepositar extends javax.swing.JPanel {

    
    private SplitUM splitUM;
    private JPanel panelCC;
    
    /**
     * Creates new form JPanelDepositar
     */
    public JPanelDepositar(JPanel cc, SplitUM f) {
        initComponents();
        
        this.jLabelErro.setForeground(Color.WHITE);
        this.splitUM = f;
        this.panelCC = cc;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelDepositar = new javax.swing.JLabel();
        jTextFieldDepositar = new javax.swing.JTextField();
        jButtonDepositarOK = new javax.swing.JButton();
        jButtonDepositarCancelar = new javax.swing.JButton();
        jLabelErro = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.GridBagLayout());

        jLabelDepositar.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jLabelDepositar.setText("Insira o valor a depositar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(40, 20, 20, 20);
        add(jLabelDepositar, gridBagConstraints);

        jTextFieldDepositar.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 40, 20);
        add(jTextFieldDepositar, gridBagConstraints);

        jButtonDepositarOK.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jButtonDepositarOK.setText("OK");
        jButtonDepositarOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDepositarOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jButtonDepositarOK, gridBagConstraints);

        jButtonDepositarCancelar.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jButtonDepositarCancelar.setText("Cancelar");
        jButtonDepositarCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDepositarCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jButtonDepositarCancelar, gridBagConstraints);

        jLabelErro.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabelErro.setForeground(new java.awt.Color(255, 0, 0));
        jLabelErro.setText("ERRO: insira uma número válido.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        add(jLabelErro, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDepositarOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDepositarOKActionPerformed
        String valor = jTextFieldDepositar.getText();
        try {
            Float f = Float.parseFloat(valor);
            splitUM.depositarValor(f);
            CardLayout cl = (CardLayout) panelCC.getLayout();
            cl.show(panelCC, "card_cc_inicial");
        }
        catch(NumberFormatException nfe) {
            this.jLabelErro.setForeground(Color.RED);
        }
    }//GEN-LAST:event_jButtonDepositarOKActionPerformed

    private void jButtonDepositarCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDepositarCancelarActionPerformed
            CardLayout cl = (CardLayout) panelCC.getLayout();
            cl.show(panelCC, "card_cc_inicial");
    }//GEN-LAST:event_jButtonDepositarCancelarActionPerformed

    public void init() {
        this.jLabelErro.setForeground(Color.WHITE);
        this.jTextFieldDepositar.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDepositarCancelar;
    private javax.swing.JButton jButtonDepositarOK;
    private javax.swing.JLabel jLabelDepositar;
    private javax.swing.JLabel jLabelErro;
    private javax.swing.JTextField jTextFieldDepositar;
    // End of variables declaration//GEN-END:variables
}
