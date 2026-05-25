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
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import rw.gov.trafficpolice.model.*;
import rw.gov.trafficpolice.util.NotificationUtil;
import rw.gov.trafficpolice.util.UITheme;

public class PaymentForm extends JFrame{
        private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtPayRef, txtAmount,
            txtReceivedBy;
    private JComboBox<Ticket>         cmbTicket;
    private JComboBox<EPaymentMethod> cmbMethod;
    private JLabel            lblTicketInfo;
    private JButton           btnPay, btnDelete,
            btnClear;
    private Payment           selected;

    public PaymentForm() {
        initUI();
        loadTickets();
        loadTable();
    }

    private void initUI() {
        setTitle("Payment Processing");
        setSize(980, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory
                .createEmptyBorder(15,15,15,15));
        root.setBackground(UITheme.LIGHT_GRAY);

        JLabel lblTitle = new JLabel(
                "Fine Payment Processing");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Payment Details", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,8,6,8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        cmbTicket    = new JComboBox<>();
        cmbMethod    = new JComboBox<>(
                EPaymentMethod.values());

        txtPayRef    = new JTextField(
                "AUTO-GENERATED");
        txtPayRef.setEditable(false);
        txtPayRef.setBackground(UITheme.LIGHT_GRAY);
        UITheme.styleField(txtPayRef);

        txtAmount    = new JTextField();
        txtAmount.setEditable(false);
        txtAmount.setBackground(UITheme.LIGHT_GRAY);
        UITheme.styleField(txtAmount);

        txtReceivedBy = new JTextField();
        UITheme.styleField(txtReceivedBy);

        lblTicketInfo = new JLabel(" ");
        lblTicketInfo.setFont(UITheme.FONT_SMALL);
        lblTicketInfo.setForeground(UITheme.TEXT_MUTED);

        cmbTicket.addActionListener(
                e -> populateTicketInfo());

        addRow(form,g,0,"Select Ticket:", cmbTicket);
        g.gridx=0; g.gridy=1;
        form.add(new JLabel(""), g);
        g.gridx=1;
        form.add(lblTicketInfo, g);
        addRow(form,g,2,"Payment Ref #:",  txtPayRef);
        addRow(form,g,3,"Amount (RWF):",   txtAmount);
        addRow(form,g,4,"Payment Method:", cmbMethod);
        addRow(form,g,5,"Received By:",    txtReceivedBy);

        btnPay    = new JButton("PROCESS PAYMENT");
        btnDelete = new JButton("DELETE");
        btnClear  = new JButton("CLEAR");
        UITheme.styleButton(btnPay,    UITheme.SUCCESS);
        UITheme.styleButton(btnDelete, UITheme.ACCENT);
        UITheme.styleButton(btnClear,  UITheme.TEXT_MUTED);

        JPanel btnPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER,10,10));
        btnPanel.setBackground(UITheme.WHITE);
        btnPanel.add(btnPay);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(UITheme.WHITE);
        left.add(form,     BorderLayout.CENTER);
        left.add(btnPanel, BorderLayout.SOUTH);
        left.setPreferredSize(new Dimension(340,0));

        String[] cols = {"ID","Pay Ref #","Ticket #",
            "Driver","Amount (RWF)","Date",
            "Method","Received By"};
        tableModel = new DefaultTableModel(cols,0) {
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.getSelectionModel()
             .addListSelectionListener(
                e -> selectPayment());

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(UITheme.WHITE);
        right.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR),
            "Payment Records", 0, 0,
            UITheme.FONT_LABEL, UITheme.PRIMARY));
        right.add(new JScrollPane(table),
                BorderLayout.CENTER);

        root.add(lblTitle, BorderLayout.NORTH);
        root.add(left,     BorderLayout.WEST);
        root.add(right,    BorderLayout.CENTER);
        setContentPane(root);

        btnPay.addActionListener(e    -> pay());
        btnDelete.addActionListener(e -> deletePayment());
        btnClear.addActionListener(e  -> clearForm());
    }

    private void pay() {
        Ticket ticket =
            (Ticket) cmbTicket.getSelectedItem();
        if (ticket == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ticket.getStatus() == ETicketStatus.PAID) {
            JOptionPane.showMessageDialog(this,
                "This ticket is already PAID.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ticket.getStatus()
                == ETicketStatus.ESCALATED) {
            JOptionPane.showMessageDialog(this,
                "Escalated tickets must be settled "
                + "through the court.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtReceivedBy.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter who received payment.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Payment p = new Payment();
            p.setPaymentReference(genPayRef());
            p.setAmountPaid(
                ticket.getTotalFineAmount());
            p.setPaymentDate(new Date());
            p.setPaymentMethod(
                (EPaymentMethod)
                cmbMethod.getSelectedItem());
            p.setReceivedBy(
                txtReceivedBy.getText().trim());
            p.setTicket(ticket);

            Payment saved = ClientConnector
                .paymentService.savePaymentRecord(p);

            if (saved != null) {
                ticket.setStatus(ETicketStatus.PAID);
                ClientConnector.ticketService
                    .updateTicketRecord(ticket);
                NotificationUtil.notifyPaymentReceived(
                    ticket.getTicketNumber(),
                    ticket.getTotalFineAmount());
                JOptionPane.showMessageDialog(this,
                    "Payment processed!\n"
                    + "Ref: " + p.getPaymentReference()
                    + "\nAmount: RWF "
                    + String.format("%.0f",
                        p.getAmountPaid()),
                    "Payment Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTickets();
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Payment failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePayment() {
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Select a payment to delete.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
            "Delete payment "
            + selected.getPaymentReference() + "?\n"
            + "Ticket will revert to ISSUED.",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            Ticket t = selected.getTicket();
            t.setStatus(ETicketStatus.ISSUED);
            ClientConnector.ticketService
                .updateTicketRecord(t);
            ClientConnector.paymentService
                .deletePaymentRecord(selected);
            JOptionPane.showMessageDialog(this,
                "Payment deleted. "
                + "Ticket reverted to ISSUED.",
                "Done",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm(); loadTickets(); loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTicketInfo() {
        Ticket t = (Ticket) cmbTicket.getSelectedItem();
        if (t != null) {
            txtAmount.setText(String.format("%.0f",
                t.getTotalFineAmount()));
            lblTicketInfo.setText(
                "Driver: " + t.getDriver().getFullName()
                + "  |  Status: " + t.getStatus());
            txtPayRef.setText(genPayRef());
        }
    }

    private void selectPayment() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Payment p = new Payment(); p.setId(id);
            selected = ClientConnector.paymentService
                .findPaymentRecordById(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTickets() {
        cmbTicket.removeAllItems();
        try {
            List<Ticket> issued = ClientConnector
                .ticketService.findTicketsByStatus(
                    ETicketStatus.ISSUED);
            for (Ticket t : issued)
                cmbTicket.addItem(t);
            List<Ticket> overdue = ClientConnector
                .ticketService.findTicketsByStatus(
                    ETicketStatus.OVERDUE);
            for (Ticket t : overdue)
                cmbTicket.addItem(t);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            List<Payment> list = ClientConnector
                .paymentService.findAllPaymentRecords();
            for (Payment p : list) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getPaymentReference(),
                    p.getTicket().getTicketNumber(),
                    p.getTicket().getDriver()
                        .getFullName(),
                    String.format("%.0f",
                        p.getAmountPaid()),
                    p.getPaymentDate(),
                    p.getPaymentMethod().name(),
                    p.getReceivedBy()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load payments.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        cmbTicket.setSelectedIndex(0);
        cmbMethod.setSelectedIndex(0);
        txtReceivedBy.setText("");
        txtAmount.setText("");
        lblTicketInfo.setText(" ");
        txtPayRef.setText("AUTO-GENERATED");
        selected = null;
        table.clearSelection();
    }

    private String genPayRef() {
        Calendar cal = Calendar.getInstance();
        return "PAY-" + cal.get(Calendar.YEAR)
            + "-" + String.format("%05d",
            new Random().nextInt(99999));
    }

    private void addRow(JPanel p, GridBagConstraints g,
            int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_LABEL); p.add(l, g);
        g.gridx = 1; g.weightx = 1; p.add(field, g);
    }
}
