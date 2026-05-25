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
import rw.gov.trafficpolice.model.Officer;
import rw.gov.trafficpolice.util.UITheme;

public class OfficerForm extends JFrame{
        private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtBadge, txtName,
            txtEmail, txtPhone, txtUsername, txtPassword;
    private JComboBox<String> cmbRole;
    private JCheckBox         chkActive;
    private JButton           btnSave, btnUpdate,
            btnDelete, btnClear;
    private Officer           selected;

    public OfficerForm() {
        if (!SessionManager.isAdmin()) {
            JOptionPane.showMessageDialog(null,
                "Access Denied!\n"
                + "Only Admins can manage officers.",
                "Unauthorized",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        initUI();
        loadTable();
    }

    private void initUI() {
        setTitle("Officer Management");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory
                .createEmptyBorder(15,15,15,15));
        root.setBackground(UITheme.LIGHT_GRAY);

        JLabel lblTitle = new JLabel(
                "Officer Management");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Officer Details", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,8,5,8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        txtBadge    = new JTextField(15);
        txtName     = new JTextField(15);
        txtEmail    = new JTextField(15);
        txtPhone    = new JTextField(15);
        txtUsername = new JTextField(15);
        txtPassword = new JTextField(15);
        cmbRole     = new JComboBox<>(
                new String[]{"OFFICER","ADMIN"});
        chkActive   = new JCheckBox("Active", true);
        chkActive.setBackground(UITheme.WHITE);

        UITheme.styleField(txtBadge);
        UITheme.styleField(txtName);
        UITheme.styleField(txtEmail);
        UITheme.styleField(txtPhone);
        UITheme.styleField(txtUsername);
        UITheme.styleField(txtPassword);

        addRow(form,g,0,"Badge Number:", txtBadge);
        addRow(form,g,1,"Full Name:",    txtName);
        addRow(form,g,2,"Email:",        txtEmail);
        addRow(form,g,3,"Phone:",        txtPhone);
        addRow(form,g,4,"Username:",     txtUsername);
        addRow(form,g,5,"Password:",     txtPassword);
        addRow(form,g,6,"Role:",         cmbRole);
        addRow(form,g,7,"Status:",       chkActive);

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
        left.setPreferredSize(new Dimension(320,0));

        String[] cols = {"ID","Badge","Full Name",
            "Email","Phone","Username","Role","Active"};
        tableModel = new DefaultTableModel(cols,0) {
            public boolean isCellEditable(int r,int c) {
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
            "Officer Records", 0, 0,
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
        if (!validate2()) return;
        try {
            Officer o = buildFromForm();
            Officer saved = ClientConnector
                    .officerService
                    .saveOfficerRecord(o);
            if (saved != null) {
                JOptionPane.showMessageDialog(this,
                    "Officer saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed! Check for duplicate "
                    + "badge or username.",
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
                "Please select an officer to update.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validate2()) return;
        try {
            Officer o = buildFromForm();
            o.setId(selected.getId());
            Officer updated = ClientConnector
                    .officerService
                    .updateOfficerRecord(o);
            if (updated != null) {
                JOptionPane.showMessageDialog(this,
                    "Officer updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Update failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
                "Please select an officer to delete.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
            "Delete officer: "
            + selected.getFullName() + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            ClientConnector.officerService
                    .deleteOfficerRecord(selected);
            JOptionPane.showMessageDialog(this,
                "Officer deleted.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm(); loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validate2() {
        if (txtBadge.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Badge number is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Full name is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!txtEmail.getText().trim()
                .matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")){
            JOptionPane.showMessageDialog(this,
                "Enter a valid email address.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtUsername.getText().trim().length() < 4) {
            JOptionPane.showMessageDialog(this,
                "Username must be at least 4 characters.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtPassword.getText().trim().length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Officer buildFromForm() {
        Officer o = new Officer();
        o.setBadgeNumber(txtBadge.getText().trim());
        o.setFullName(txtName.getText().trim());
        o.setEmail(txtEmail.getText().trim());
        o.setPhoneNumber(txtPhone.getText().trim());
        o.setUsername(txtUsername.getText().trim());
        o.setPassword(txtPassword.getText().trim());
        o.setRole((String)cmbRole.getSelectedItem());
        o.setActive(chkActive.isSelected());
        return o;
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Officer o = new Officer();
            o.setId(id);
            selected = ClientConnector.officerService
                    .findOfficerRecordById(o);
            if (selected == null) return;
            txtBadge.setText(selected.getBadgeNumber());
            txtName.setText(selected.getFullName());
            txtEmail.setText(selected.getEmail());
            txtPhone.setText(selected.getPhoneNumber());
            txtUsername.setText(selected.getUsername());
            txtPassword.setText(selected.getPassword());
            cmbRole.setSelectedItem(selected.getRole());
            chkActive.setSelected(selected.isActive());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            List<Officer> list = ClientConnector
                    .officerService.findAllOfficerRecords();
            for (Officer o : list) {
                tableModel.addRow(new Object[]{
                    o.getId(), o.getBadgeNumber(),
                    o.getFullName(), o.getEmail(),
                    o.getPhoneNumber(), o.getUsername(),
                    o.getRole(),
                    o.isActive() ? "Yes" : "No"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load officers.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtBadge.setText(""); txtName.setText("");
        txtEmail.setText(""); txtPhone.setText("");
        txtUsername.setText(""); txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        chkActive.setSelected(true);
        selected = null;
        table.clearSelection();
    }

    private void addRow(JPanel p, GridBagConstraints g,
            int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_LABEL);
        p.add(l, g);
        g.gridx = 1; g.weightx = 1;
        p.add(field, g);
    }
}
