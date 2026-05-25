/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.view;

/**
 *
 * @author PC
 */
import rw.gov.trafficpolice.controller.ClientConnector;
import rw.gov.trafficpolice.controller.SessionManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import rw.gov.trafficpolice.model.ViolationType;
import rw.gov.trafficpolice.util.UITheme;

public class ViolationTypeForm extends JFrame {
        private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtCode, txtName,
            txtFine, txtMultiplier;
    private JTextArea         txtDescription;
    private JComboBox<String> cmbSeverity;
    private JButton           btnSave, btnUpdate,
            btnDelete, btnClear;
    private ViolationType     selected;

    public ViolationTypeForm() {
        if (!SessionManager.isAdmin()) {
            JOptionPane.showMessageDialog(null,
                "Access Denied!\n"
                + "Only Admins can manage "
                + "violation types.",
                "Unauthorized",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        initUI();
        loadTable();
    }

    private void initUI() {
        setTitle("Violation Type Management");
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory
                .createEmptyBorder(15,15,15,15));
        root.setBackground(UITheme.LIGHT_GRAY);

        JLabel lblTitle = new JLabel(
                "Violation Type Management");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY);

  
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Violation Details", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,8,5,8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        txtCode       = new JTextField(15);
        txtName       = new JTextField(15);
        txtFine       = new JTextField(15);
        txtMultiplier = new JTextField("1.0", 15);
        txtDescription = new JTextArea(3,15);
        txtDescription.setLineWrap(true);
        txtDescription.setFont(UITheme.FONT_BODY);
        txtDescription.setBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR));
        cmbSeverity = new JComboBox<>(new String[]{
            "LOW","MEDIUM","HIGH","CRITICAL"});

        UITheme.styleField(txtCode);
        UITheme.styleField(txtName);
        UITheme.styleField(txtFine);
        UITheme.styleField(txtMultiplier);

        addRow(form,g,0,"Code:",              txtCode);
        addRow(form,g,1,"Name:",              txtName);
        addRow(form,g,2,"Fine Amount (RWF):", txtFine);
        addRow(form,g,3,"Penalty Multiplier:",txtMultiplier);
        addRow(form,g,4,"Severity Level:",    cmbSeverity);

        g.gridx=0; g.gridy=5; g.weightx=0;
        JLabel ld = new JLabel("Description:");
        ld.setFont(UITheme.FONT_LABEL);
        form.add(ld, g);
        g.gridx=1; g.weightx=1;
        form.add(new JScrollPane(txtDescription), g);

     
        btnSave   = new JButton("SAVE");
        btnUpdate = new JButton("UPDATE");
        btnDelete = new JButton("DELETE");
        btnClear  = new JButton("CLEAR");
        UITheme.styleButton(btnSave,   UITheme.SUCCESS);
        UITheme.styleButton(btnUpdate, UITheme.WARNING);
        UITheme.styleButton(btnDelete, UITheme.ACCENT);
        UITheme.styleButton(btnClear,  UITheme.TEXT_MUTED);

        JPanel btnPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER,10,10));
        btnPanel.setBackground(UITheme.WHITE);
        btnPanel.add(btnSave);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(UITheme.WHITE);
        left.add(form,     BorderLayout.CENTER);
        left.add(btnPanel, BorderLayout.SOUTH);
        left.setPreferredSize(new Dimension(330,0));

     
        String[] cols = {"ID","Code","Name",
            "Fine (RWF)","Multiplier","Severity"};
        tableModel = new DefaultTableModel(cols,0) {
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.getSelectionModel()
             .addListSelectionListener(
                e -> populateForm());

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(UITheme.WHITE);
        right.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Violation Type Records", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));
        right.add(new JScrollPane(table),
                BorderLayout.CENTER);

        root.add(lblTitle, BorderLayout.NORTH);
        root.add(left,     BorderLayout.WEST);
        root.add(right,    BorderLayout.CENTER);
        setContentPane(root);

        btnSave.addActionListener(e   -> save());
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
        btnClear.addActionListener(e  -> clearForm());
    }

    private void save() {
        if (!validateForm()) return;
        try {
            ViolationType vt = buildFromForm();
            ViolationType saved = ClientConnector
                .violationTypeService
                .saveViolationTypeRecord(vt);
            if (saved != null) {
                JOptionPane.showMessageDialog(this,
                    "Violation type saved!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed! Duplicate code?",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void update() {
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Select a record to update.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;
        try {
            ViolationType vt = buildFromForm();
            vt.setId(selected.getId());
            ViolationType updated = ClientConnector
                .violationTypeService
                .updateViolationTypeRecord(vt);
            if (updated != null) {
                JOptionPane.showMessageDialog(this,
                    "Violation type updated!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void delete() {
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Select a record to delete.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
            "Delete: " + selected.getName() + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            ClientConnector.violationTypeService
                .deleteViolationTypeRecord(selected);
            JOptionPane.showMessageDialog(this,
                "Deleted successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm(); loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (txtCode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Violation code is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Violation name is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            double fine = Double.parseDouble(
                txtFine.getText().trim());
            if (fine <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Fine amount must be a "
                + "positive number.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            double m = Double.parseDouble(
                txtMultiplier.getText().trim());
            if (m < 1.0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Penalty multiplier must be >= 1.0",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private ViolationType buildFromForm() {
        ViolationType vt = new ViolationType();
        vt.setCode(txtCode.getText()
                .trim().toUpperCase());
        vt.setName(txtName.getText().trim());
        vt.setFineAmount(Double.parseDouble(
                txtFine.getText().trim()));
        vt.setPenaltyMultiplier(Double.parseDouble(
                txtMultiplier.getText().trim()));
        vt.setSeverityLevel(
                (String)cmbSeverity.getSelectedItem());
        vt.setDescription(
                txtDescription.getText().trim());
        return vt;
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            ViolationType vt = new ViolationType();
            vt.setId(id);
            selected = ClientConnector
                .violationTypeService
                .findViolationTypeRecordById(vt);
            if (selected == null) return;
            txtCode.setText(selected.getCode());
            txtName.setText(selected.getName());
            txtFine.setText(String.valueOf(
                    selected.getFineAmount()));
            txtMultiplier.setText(String.valueOf(
                    selected.getPenaltyMultiplier()));
            cmbSeverity.setSelectedItem(
                    selected.getSeverityLevel());
            txtDescription.setText(
                    selected.getDescription());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            List<ViolationType> list = ClientConnector
                .violationTypeService
                .findAllViolationTypeRecords();
            for (ViolationType vt : list) {
                tableModel.addRow(new Object[]{
                    vt.getId(), vt.getCode(),
                    vt.getName(),
                    String.format("%.0f",
                        vt.getFineAmount()),
                    vt.getPenaltyMultiplier(),
                    vt.getSeverityLevel()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load records.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtCode.setText(""); txtName.setText("");
        txtFine.setText(""); txtMultiplier.setText("1.0");
        txtDescription.setText("");
        cmbSeverity.setSelectedIndex(0);
        selected = null; table.clearSelection();
    }

    private void addRow(JPanel p, GridBagConstraints g,
            int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_LABEL); p.add(l, g);
        g.gridx = 1; g.weightx = 1; p.add(field, g);
    }
}
