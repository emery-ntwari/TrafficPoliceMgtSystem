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
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import rw.gov.trafficpolice.model.*;
import rw.gov.trafficpolice.util.NotificationUtil;
import rw.gov.trafficpolice.util.TicketReceiptPrinter;
import rw.gov.trafficpolice.util.UITheme;
import rw.gov.trafficpolice.util.SMSUtil;
import rw.gov.trafficpolice.util.DriverNotificationUtil;
import java.text.SimpleDateFormat;

public class TicketForm extends JFrame {
        private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtTicketNum,
            txtLocation, txtTotal;
    private JSpinner          spnDueDate;
    private JTextArea         txtNotes;
    private JComboBox<Driver>        cmbDriver;
    private JComboBox<ETicketStatus> cmbStatus;
    private JList<ViolationType>     lstViolations;
    private JButton btnIssue, btnUpdate,
        btnDelete, btnEscalate,
        btnClear, btnPrint;
    private Ticket            selected;

    public TicketForm() {
        initUI();
        loadDrivers();
        loadViolationTypes();
        loadTable();
    }

    private void initUI() {
        setTitle("Ticket Management");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory
                .createEmptyBorder(15,15,15,15));
        root.setBackground(UITheme.LIGHT_GRAY);

        JLabel lblTitle = new JLabel(
                "Ticket Management");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY);

     
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Ticket Details", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,8,5,8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        txtTicketNum = new JTextField("AUTO-GENERATED");
        txtTicketNum.setEditable(false);
        txtTicketNum.setBackground(UITheme.LIGHT_GRAY);
        UITheme.styleField(txtTicketNum);

        txtLocation = new JTextField(15);
        UITheme.styleField(txtLocation);

        spnDueDate = new JSpinner(
            new SpinnerDateModel());
        JSpinner.DateEditor de =
            new JSpinner.DateEditor(spnDueDate,
                "yyyy-MM-dd");
        spnDueDate.setEditor(de);
        spnDueDate.setValue(new Date(
            System.currentTimeMillis()
            + 30L*24*60*60*1000));

        txtTotal = new JTextField("0.00");
        txtTotal.setEditable(false);
        txtTotal.setBackground(UITheme.LIGHT_GRAY);
        UITheme.styleField(txtTotal);

        txtNotes = new JTextArea(3,15);
        txtNotes.setLineWrap(true);
        txtNotes.setFont(UITheme.FONT_BODY);
        txtNotes.setBorder(BorderFactory
            .createLineBorder(UITheme.BORDER_COLOR));

        cmbDriver = new JComboBox<>();
        cmbStatus = new JComboBox<>(
                ETicketStatus.values());

       lstViolations = new JList<>();
lstViolations.setSelectionMode(
    ListSelectionModel
    .MULTIPLE_INTERVAL_SELECTION);
lstViolations.setFont(UITheme.FONT_BODY);
lstViolations.setVisibleRowCount(6);
lstViolations.setFixedCellHeight(25);
lstViolations.setBackground(UITheme.WHITE);
lstViolations.setBorder(
    BorderFactory.createEmptyBorder(3,5,3,5));
lstViolations.addListSelectionListener(
    e -> calculateTotal());

        addRow(form,g,0,"Ticket #:",     txtTicketNum);
        addRow(form,g,1,"Driver:",       cmbDriver);
        addRow(form,g,2,"Location:",     txtLocation);
        addRow(form,g,3,"Due Date:",     spnDueDate);
addRow(form,g,4,"Status:", cmbStatus);


g.gridx = 0; g.gridy = 5;
g.weightx = 0; g.weighty = 0;
JLabel lv = new JLabel("Violations:");
lv.setFont(UITheme.FONT_LABEL);
form.add(lv, g);

g.gridx = 1; g.weightx = 1;
g.weighty = 1;
g.fill = GridBagConstraints.BOTH;
g.ipady = 80;
JScrollPane violationScroll =
    new JScrollPane(lstViolations);
violationScroll.setPreferredSize(
    new Dimension(200, 100));
violationScroll.setBorder(
    BorderFactory.createLineBorder(
        UITheme.BORDER_COLOR));
form.add(violationScroll, g);
g.ipady = 0;
g.weighty = 0;
g.fill = GridBagConstraints.HORIZONTAL;


addRow(form,g,6,"Total Fine (RWF):", txtTotal);


g.gridx = 0; g.gridy = 7;
g.weightx = 0;
JLabel ln = new JLabel("Notes:");
ln.setFont(UITheme.FONT_LABEL);
form.add(ln, g);
g.gridx = 1; g.weightx = 1;
g.ipady = 40;
form.add(new JScrollPane(txtNotes), g);
g.ipady = 0;

        //BUTTONS 
 btnIssue    = new JButton("ISSUE TICKET");
btnUpdate   = new JButton("UPDATE");
btnEscalate = new JButton("ESCALATE");
btnDelete   = new JButton("DELETE");
btnPrint    = new JButton("🖨 PRINT");
btnClear    = new JButton("CLEAR");
UITheme.styleButton(btnIssue,
        UITheme.SUCCESS);
UITheme.styleButton(btnUpdate,
        UITheme.WARNING);
UITheme.styleButton(btnEscalate,
        UITheme.PRIMARY_DARK);
UITheme.styleButton(btnDelete,
        UITheme.ACCENT);
UITheme.styleButton(btnPrint,
        new Color(0, 128, 128));
UITheme.styleButton(btnClear,
        UITheme.TEXT_MUTED);

        JPanel btnPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER,8,10));
        btnPanel.setBackground(UITheme.WHITE);
btnPanel.add(btnIssue);
btnPanel.add(btnUpdate);
btnPanel.add(btnEscalate);
btnPanel.add(btnPrint);
btnPanel.add(btnDelete);
btnPanel.add(btnClear);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(UITheme.WHITE);
        left.add(form,     BorderLayout.CENTER);
        left.add(btnPanel, BorderLayout.SOUTH);
        left.setPreferredSize(new Dimension(370,0));

        
        String[] cols = {"ID","Ticket #","Driver",
            "Location","Issue Date","Due Date",
            "Total (RWF)","Status","Officer"};
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
            "All Tickets", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));
        right.add(new JScrollPane(table),
                BorderLayout.CENTER);

        root.add(lblTitle, BorderLayout.NORTH);
        root.add(left,     BorderLayout.WEST);
        root.add(right,    BorderLayout.CENTER);
        setContentPane(root);

     btnIssue.addActionListener(e    -> issue());
btnUpdate.addActionListener(e   -> update());
btnEscalate.addActionListener(e -> escalate());
btnDelete.addActionListener(e   -> delete());
btnClear.addActionListener(e    -> clearForm());
btnPrint.addActionListener(e    -> printTicket());
    }

    private void issue() {
        if (!validateForm()) return;
        try {
            Driver driver =
                (Driver) cmbDriver.getSelectedItem();
            long prev = ClientConnector.ticketService
                .countTicketsByDriver(driver.getId());

            Set<ViolationType> violations =
                new HashSet<>(lstViolations
                    .getSelectedValuesList());

            double total = 0;
            for (ViolationType vt : violations) {
                double fine = vt.getFineAmount();
                if (prev >= 3)
                    fine *= vt.getPenaltyMultiplier();
                total += fine;
            }

            Ticket t = new Ticket();
            t.setTicketNumber(genTicketNum());
            t.setDriver(driver);
            t.setOfficer(SessionManager
                    .getCurrentOfficer());
            t.setLocation(
                    txtLocation.getText().trim());
            t.setIssueDate(new Date());
            t.setDueDate(
                (Date) spnDueDate.getValue());
            t.setViolationTypes(violations);
            t.setTotalFineAmount(total);
            t.setStatus(ETicketStatus.ISSUED);
            t.setNotes(txtNotes.getText().trim());

            Ticket saved = ClientConnector
                .ticketService.saveTicketRecord(t);

if (saved != null) {
    // Send ActiveMQ notification
    NotificationUtil.notifyTicketIssued(
        t.getTicketNumber(),
        driver.getFullName(), total);

   
    final Ticket finalTicket = saved;
    new Thread(() -> {
        SMSUtil.sendTicketSMS(finalTicket);
    }).start();

   
    DriverNotificationUtil
        .notifyTicketIssued(finalTicket);

 
    SimpleDateFormat sdf =
        new SimpleDateFormat("dd/MM/yyyy");
    JOptionPane.showMessageDialog(this,
        "✅ Ticket Issued Successfully!\n\n"
        + "Ticket No : "
        + t.getTicketNumber() + "\n"
        + "Driver    : "
        + driver.getFullName() + "\n"
        + "Phone     : "
        + driver.getPhoneNumber() + "\n"
        + "Location  : "
        + t.getLocation() + "\n"
        + "Fine (RWF): RWF "
        + String.format("%,.0f", total) + "\n"
        + "Due Date  : "
        + sdf.format(t.getDueDate()) + "\n\n"
        + "📱 SMS notification sent to\n"
        + "   " + driver.getPhoneNumber(),
        "Ticket Issued",
        JOptionPane.INFORMATION_MESSAGE);
    clearForm();
    loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to issue ticket.",
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
                "Select a ticket to update.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            selected.setLocation(
                txtLocation.getText().trim());
            selected.setStatus(
                (ETicketStatus)
                cmbStatus.getSelectedItem());
            selected.setNotes(
                txtNotes.getText().trim());
            ClientConnector.ticketService
                .updateTicketRecord(selected);
            JOptionPane.showMessageDialog(this,
                "Ticket updated!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm(); loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void escalate() {
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Select a ticket to escalate.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selected.getStatus()
                == ETicketStatus.PAID) {
            JOptionPane.showMessageDialog(this,
                "Cannot escalate a PAID ticket.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
            "Escalate ticket "
            + selected.getTicketNumber()
            + " to legal action?",
            "Confirm Escalation",
            JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
           selected.setStatus(ETicketStatus.ESCALATED);
Ticket escalated = ClientConnector.ticketService
    .updateTicketRecord(selected);
NotificationUtil.notifyTicketEscalated(
    selected.getTicketNumber(),
    selected.getDriver().getFullName());

// Send escalation email to driver
if (escalated != null) {
    DriverNotificationUtil
        .notifyTicketEscalated(escalated);
}

JOptionPane.showMessageDialog(this,
    "Ticket escalated to legal action.\n"
    + "Driver has been notified by email.",
    "Escalated",
    JOptionPane.WARNING_MESSAGE);
            clearForm(); loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void delete() {
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Select a ticket to delete.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selected.getStatus()
                == ETicketStatus.PAID) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete a PAID ticket.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
            "Delete ticket "
            + selected.getTicketNumber() + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            ClientConnector.ticketService
                .deleteTicketRecord(selected);
            JOptionPane.showMessageDialog(this,
                "Ticket deleted.",
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
        if (cmbDriver.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a driver.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (lstViolations
                .getSelectedValuesList().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Select at least one violation.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtLocation.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Location is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Date due = (Date) spnDueDate.getValue();
        if (due.before(new Date())) {
            JOptionPane.showMessageDialog(this,
                "Due date must be a future date.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void calculateTotal() {
        List<ViolationType> sel =
            lstViolations.getSelectedValuesList();
        double total = sel.stream()
            .mapToDouble(ViolationType::getFineAmount)
            .sum();
        txtTotal.setText(
                String.format("%.0f", total));
    }

    private String genTicketNum() {
        Calendar cal = Calendar.getInstance();
        return "TKT-" + cal.get(Calendar.YEAR)
            + "-" + String.format("%05d",
            new Random().nextInt(99999));
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Ticket t = new Ticket(); t.setId(id);
            selected = ClientConnector.ticketService
                .findTicketRecordById(t);
            if (selected == null) return;
            txtTicketNum.setText(
                selected.getTicketNumber());
            txtLocation.setText(selected.getLocation());
            spnDueDate.setValue(selected.getDueDate());
            txtTotal.setText(String.format("%.0f",
                selected.getTotalFineAmount()));
            txtNotes.setText(selected.getNotes());
            cmbStatus.setSelectedItem(
                selected.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            List<Ticket> list = ClientConnector
                .ticketService.findAllTicketRecords();
            for (Ticket t : list) {
                tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTicketNumber(),
                    t.getDriver().getFullName(),
                    t.getLocation(),
                    t.getIssueDate(),
                    t.getDueDate(),
                    String.format("%.0f",
                        t.getTotalFineAmount()),
                    t.getStatus().name(),
                    t.getOfficer().getFullName()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load tickets.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDrivers() {
        try {
            List<Driver> drivers = ClientConnector
                .driverService.findAllDriverRecords();
            for (Driver d : drivers)
                cmbDriver.addItem(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadViolationTypes() {
        try {
            List<ViolationType> vts = ClientConnector
                .violationTypeService
                .findAllViolationTypeRecords();
            lstViolations.setListData(
                vts.toArray(new ViolationType[0]));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        txtTicketNum.setText("AUTO-GENERATED");
        txtLocation.setText("");
        spnDueDate.setValue(new Date(
            System.currentTimeMillis()
            + 30L*24*60*60*1000));
        txtTotal.setText("0.00");
        txtNotes.setText("");
        cmbStatus.setSelectedIndex(0);
        lstViolations.clearSelection();
        selected = null;
        table.clearSelection();
    }

    private void addRow(JPanel p, GridBagConstraints g,
            int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_LABEL); p.add(l, g);
        g.gridx = 1; g.weightx = 1; p.add(field, g);
    }
    private void printTicket() {
    if (selected == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a ticket to print.",
            "No Ticket Selected",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Print receipt for ticket:\n"
        + selected.getTicketNumber() + "\n\n"
        + "Driver: "
        + selected.getDriver().getFullName()
        + "\nFine: RWF "
        + String.format("%,.0f",
            selected.getTotalFineAmount()),
        "Confirm Print",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        TicketReceiptPrinter.printReceipt(selected);
    }
}
}
