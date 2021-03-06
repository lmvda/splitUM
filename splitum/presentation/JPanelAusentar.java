/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.presentation;

import java.awt.CardLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JSpinner;
import splitum.business.SplitUM;

public class JPanelAusentar extends javax.swing.JPanel {

    private SplitUM facade;
    private JPanelContaCorrente panelCC;
    private LocalDate inicioIntervalo;
    private LocalDate fimIntervalo;
    
    /**
     * Creates new form JPanelAusentar
     */
    public JPanelAusentar(JPanelContaCorrente panelCC, SplitUM f) {
        initComponents();
        this.facade = f;
        this.panelCC = panelCC;
        
        
        JSpinner.DateEditor di = new JSpinner.DateEditor(jSpinnerDataInicio, "dd-MM-yyyy");
        jSpinnerDataInicio.setEditor(di);
        JSpinner.DateEditor df = new JSpinner.DateEditor(jSpinnerDataFim, "dd-MM-yyyy");
        jSpinnerDataFim.setEditor(df);
    }
    
    void init() {
        jLabelErro.setVisible(false);
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

        jSpinnerDataInicio = new javax.swing.JSpinner();
        jLabelIntervaloInicio = new javax.swing.JLabel();
        jLabelIntervaloFim = new javax.swing.JLabel();
        jButtonIntervaloOK = new javax.swing.JButton();
        jButtonIntervaloCancelar = new javax.swing.JButton();
        jSpinnerDataFim = new javax.swing.JSpinner();
        jLabelIntervaloTitulo = new javax.swing.JLabel();
        jLabelErro = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.GridBagLayout());

        jSpinnerDataInicio.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jSpinnerDataInicio.setModel(new javax.swing.SpinnerDateModel());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jSpinnerDataInicio, gridBagConstraints);

        jLabelIntervaloInicio.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jLabelIntervaloInicio.setText("Início");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jLabelIntervaloInicio, gridBagConstraints);

        jLabelIntervaloFim.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jLabelIntervaloFim.setText("Fim");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jLabelIntervaloFim, gridBagConstraints);

        jButtonIntervaloOK.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jButtonIntervaloOK.setText("OK");
        jButtonIntervaloOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIntervaloOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jButtonIntervaloOK, gridBagConstraints);

        jButtonIntervaloCancelar.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jButtonIntervaloCancelar.setText("Cancelar");
        jButtonIntervaloCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIntervaloCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jButtonIntervaloCancelar, gridBagConstraints);

        jSpinnerDataFim.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jSpinnerDataFim.setModel(new javax.swing.SpinnerDateModel());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jSpinnerDataFim, gridBagConstraints);

        jLabelIntervaloTitulo.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jLabelIntervaloTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIntervaloTitulo.setText("Insira o intervalo da ausência:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(jLabelIntervaloTitulo, gridBagConstraints);

        jLabelErro.setFont(new java.awt.Font("Lucida Grande", 3, 16)); // NOI18N
        jLabelErro.setForeground(new java.awt.Color(255, 0, 0));
        jLabelErro.setText("ERRO: data inicial não pode ser depois da data final.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        add(jLabelErro, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonIntervaloOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIntervaloOKActionPerformed
        inicioIntervalo = ((Date) jSpinnerDataInicio.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        fimIntervalo = ((Date) jSpinnerDataFim.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        
        if(inicioIntervalo.compareTo(fimIntervalo) < 0) {
            if(now.compareTo(fimIntervalo) < 0) {
                
                facade.registaIntervaloAusencia(inicioIntervalo, fimIntervalo);
                jLabelErro.setVisible(false);
                CardLayout cl = (CardLayout) panelCC.getLayout();
                cl.show(panelCC, "card_cc_inicial");
            }
            else {
                jLabelErro.setText("ERRO: data final tem que ser no futuro.");
                jLabelErro.setVisible(true);
            }
        }
        else {
            jLabelErro.setText("ERRO: data inicial não pode ser depois da data final.");
            jLabelErro.setVisible(true);
        }
    }//GEN-LAST:event_jButtonIntervaloOKActionPerformed

    private void jButtonIntervaloCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIntervaloCancelarActionPerformed
        CardLayout cl = (CardLayout) panelCC.getLayout();
        cl.show(panelCC, "card_cc_inicial");
    }//GEN-LAST:event_jButtonIntervaloCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonIntervaloCancelar;
    private javax.swing.JButton jButtonIntervaloOK;
    private javax.swing.JLabel jLabelErro;
    private javax.swing.JLabel jLabelIntervaloFim;
    private javax.swing.JLabel jLabelIntervaloInicio;
    private javax.swing.JLabel jLabelIntervaloTitulo;
    private javax.swing.JSpinner jSpinnerDataFim;
    private javax.swing.JSpinner jSpinnerDataInicio;
    // End of variables declaration//GEN-END:variables
}
