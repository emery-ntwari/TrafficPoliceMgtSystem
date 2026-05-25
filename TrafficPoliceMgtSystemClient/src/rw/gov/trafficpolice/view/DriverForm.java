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
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import rw.gov.trafficpolice.model.Driver;
import rw.gov.trafficpolice.util.UITheme;

public class DriverForm extends JFrame {
        private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtLicense, txtName,
            txtNationalId, txtPhone,
            txtEmail, txtPlate, txtModel, txtSearch;
    private JButton           btnSave, btnUpdate,
            btnDelete, btnClear, btnSearch;
    private Driver            selected;

    public DriverForm() {
        initUI();
        loadTable();
    }

    private void initUI() {
        setTitle("Driver Management");
        setSize(980, 630);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory
                .createEmptyBorder(15,15,15,15));
        root.setBackground(UITheme.LIGHT_GRAY);

        JLabel lblTitle = new JLabel("Driver Management");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Driver Details", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,8,5,8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        txtLicense    = new JTextField(15);
        txtName       = new JTextField(15);
        txtNationalId = new JTextField(15);
        txtPhone      = new JTextField(15);
        txtEmail      = new JTextField(15);
        txtPlate      = new JTextField(15);
        txtModel      = new JTextField(15);

        UITheme.styleField(txtLicense);
        UITheme.styleField(txtName);
        UITheme.styleField(txtNationalId);
        UITheme.styleField(txtPhone);
        UITheme.styleField(txtEmail);
        UITheme.styleField(txtPlate);
        UITheme.styleField(txtModel);

        addRow(form,g,0,"License Number:", txtLicense);
        addRow(form,g,1,"Full Name:",      txtName);
        addRow(form,g,2,"National ID:",    txtNationalId);
        addRow(form,g,3,"Phone:",          txtPhone);
        addRow(form,g,4,"Email:",          txtEmail);
        addRow(form,g,5,"Vehicle Plate:",  txtPlate);
        addRow(form,g,6,"Vehicle Model:",  txtModel);

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
        left.setPreferredSize(new Dimension(310,0));

        txtSearch = new JTextField(18);
        UITheme.styleField(txtSearch);
        btnSearch = new JButton("Search by Plate");
        UITheme.styleButton(btnSearch, UITheme.PRIMARY);

        JPanel searchBar = new JPanel(
                new FlowLayout(FlowLayout.LEFT,8,6));
        searchBar.setBackground(UITheme.WHITE);
        JLabel lblS = new JLabel("Search:");
        lblS.setFont(UITheme.FONT_LABEL);
        searchBar.add(lblS);
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);

        String[] cols = {"ID","License No.","Full Name",
            "National ID","Phone","Email",
            "Vehicle Plate","Vehicle Model"};
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
            "Driver Records", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));
        right.add(searchBar,
                BorderLayout.NORTH);
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
        btnSearch.addActionListener(e -> search());
    }

    private void save() {
        if (!validateForm()) return;
        try {
            Driver d = buildFromForm();
            Driver saved = ClientConnector
                    .driverService.saveDriverRecord(d);
            if (saved != null) {
                JOptionPane.showMessageDialog(this,
                    "Driver saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed! Check for duplicate "
                    + "license or plate.",
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
                "Please select a driver to update.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;
        try {
            Driver d = buildFromForm();
            d.setId(selected.getId());
            Driver updated = ClientConnector
                    .driverService.updateDriverRecord(d);
            if (updated != null) {
                JOptionPane.showMessageDialog(this,
                    "Driver updated successfully!",
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
                "Please select a driver to delete.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
            "Delete driver: "
            + selected.getFullName() + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            ClientConnector.driverService
                    .deleteDriverRecord(selected);
            JOptionPane.showMessageDialog(this,
                "Driver deleted.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm(); loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void search() {
        String plate = txtSearch.getText().trim();
        if (plate.isEmpty()) { loadTable(); return; }
        try {
            Driver d = ClientConnector.driverService
                    .findDriverByPlate(plate);
            tableModel.setRowCount(0);
            if (d != null) {
                tableModel.addRow(new Object[]{
                    d.getId(), d.getLicenseNumber(),
                    d.getFullName(), d.getNationalId(),
                    d.getPhoneNumber(), d.getEmail(),
                    d.getVehiclePlate(),
                    d.getVehicleModel()});
            } else {
                JOptionPane.showMessageDialog(this,
                    "No driver found with plate: " + plate,
                    "Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Search error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (txtLicense.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "License number is required.",
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
        if (!txtNationalId.getText().trim()
                .matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this,
                "National ID must be exactly 16 digits.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtPlate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vehicle plate is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Driver buildFromForm() {
        Driver d = new Driver();
        d.setLicenseNumber(txtLicense.getText().trim());
        d.setFullName(txtName.getText().trim());
        d.setNationalId(txtNationalId.getText().trim());
        d.setPhoneNumber(txtPhone.getText().trim());
        d.setEmail(txtEmail.getText().trim());
        d.setVehiclePlate(
            txtPlate.getText().trim().toUpperCase());
        d.setVehicleModel(txtModel.getText().trim());
        return d;
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Driver d = new Driver(); d.setId(id);
            selected = ClientConnector.driverService
                    .findDriverRecordById(d);
            if (selected == null) return;
            txtLicense.setText(
                    selected.getLicenseNumber());
            txtName.setText(selected.getFullName());
            txtNationalId.setText(
                    selected.getNationalId());
            txtPhone.setText(selected.getPhoneNumber());
            txtEmail.setText(selected.getEmail());
            txtPlate.setText(selected.getVehiclePlate());
            txtModel.setText(selected.getVehicleModel());
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            List<Driver> list = ClientConnector
                    .driverService.findAllDriverRecords();
            for (Driver d : list) {
                tableModel.addRow(new Object[]{
                    d.getId(), d.getLicenseNumber(),
                    d.getFullName(), d.getNationalId(),
                    d.getPhoneNumber(), d.getEmail(),
                    d.getVehiclePlate(),
                    d.getVehicleModel()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load drivers.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtLicense.setText(""); txtName.setText("");
        txtNationalId.setText(""); txtPhone.setText("");
        txtEmail.setText(""); txtPlate.setText("");
        txtModel.setText(""); txtSearch.setText("");
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
