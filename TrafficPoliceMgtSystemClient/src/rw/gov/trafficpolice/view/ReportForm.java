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
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import rw.gov.trafficpolice.model.*;
import rw.gov.trafficpolice.util.UITheme;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;

public class ReportForm extends JFrame {
        private JTable            table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbReport;
    private JLabel            lblSummary;
    private JButton           btnGenerate,
            btnPDF, btnExcel, btnCSV;

    public ReportForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Reports and Export");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(BorderFactory
                .createEmptyBorder(15,15,15,15));
        root.setBackground(UITheme.LIGHT_GRAY);

        JLabel lblTitle = new JLabel(
                "Reports and Data Export");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY);

        JPanel bar = new JPanel(
                new FlowLayout(FlowLayout.LEFT,12,8));
        bar.setBackground(UITheme.WHITE);
        bar.setBorder(BorderFactory.createLineBorder(
                UITheme.BORDER_COLOR));

        cmbReport = new JComboBox<>(new String[]{
            "All Tickets Report",
            "Paid Tickets Report",
            "Overdue Tickets Report",
            "All Payments Report",
            "All Drivers Report",
            "All Violation Types Report"
        });
        cmbReport.setFont(UITheme.FONT_BODY);
        cmbReport.setPreferredSize(
                new Dimension(240,32));

        btnGenerate = new JButton("Generate Report");
        btnPDF      = new JButton("Export to PDF");
        btnExcel    = new JButton("Export to Excel");
        UITheme.styleButton(btnGenerate,
                UITheme.PRIMARY);
        UITheme.styleButton(btnPDF,
                UITheme.ACCENT);
        UITheme.styleButton(btnExcel,
                UITheme.SUCCESS);
        btnPDF.setEnabled(false);
        btnExcel.setEnabled(false);

        JLabel lblR = new JLabel("Report:");
        lblR.setFont(UITheme.FONT_LABEL);
        bar.add(lblR);
        bar.add(cmbReport);
        bar.add(btnGenerate);
        bar.add(btnPDF);
        bar.add(btnExcel);
        
        btnCSV = new JButton("Export to CSV");
UITheme.styleButton(btnCSV, new java.awt.Color(40, 167, 69)); // green
btnCSV.setEnabled(false);
bar.add(btnCSV);

        lblSummary = new JLabel(
            "Select a report and click Generate.");
        lblSummary.setFont(UITheme.FONT_SMALL);
        lblSummary.setForeground(UITheme.TEXT_MUTED);
        lblSummary.setBorder(BorderFactory
                .createEmptyBorder(4,8,4,8));

        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JPanel tablePanel = new JPanel(
                new BorderLayout());
        tablePanel.setBackground(UITheme.WHITE);
        tablePanel.setBorder(BorderFactory
                .createLineBorder(UITheme.BORDER_COLOR));
        tablePanel.add(lblSummary,
                BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table),
                BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout(0,8));
        top.setOpaque(false);
        top.add(lblTitle, BorderLayout.NORTH);
        top.add(bar,      BorderLayout.SOUTH);

        root.add(top,       BorderLayout.NORTH);
        root.add(tablePanel,BorderLayout.CENTER);
        setContentPane(root);

        btnGenerate.addActionListener(
                e -> generateReport());
        btnPDF.addActionListener(
                e -> exportPDF());
        btnExcel.addActionListener(
                e -> exportExcel());
        btnCSV.addActionListener(
                e -> exportCSV());
    }

    private void generateReport() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        String sel = (String)cmbReport
                .getSelectedItem();
        try {
            switch (sel) {
                case "All Tickets Report":
                    loadTickets(null); break;
                case "Paid Tickets Report":
                    loadTickets(ETicketStatus.PAID);
                    break;
                case "Overdue Tickets Report":
                    loadTickets(ETicketStatus.OVERDUE);
                    break;
                case "All Payments Report":
                    loadPayments(); break;
                case "All Drivers Report":
                    loadDrivers(); break;
                case "All Violation Types Report":
                    loadViolations(); break;
            }
            boolean hasData =
                tableModel.getRowCount() > 0;
            btnPDF.setEnabled(hasData);
            btnExcel.setEnabled(hasData);
            btnCSV.setEnabled(hasData);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTickets(ETicketStatus status)
            throws Exception {
        List<Ticket> list = status == null
            ? ClientConnector.ticketService
                .findAllTicketRecords()
            : ClientConnector.ticketService
                .findTicketsByStatus(status);
        tableModel.setColumnIdentifiers(new String[]{
            "Ticket #","Driver","Plate","Location",
            "Issue Date","Due Date",
            "Total Fine (RWF)","Status","Officer"});
        double total = 0;
        for (Ticket t : list) {
            total += t.getTotalFineAmount();
            tableModel.addRow(new Object[]{
                t.getTicketNumber(),
                t.getDriver().getFullName(),
                t.getDriver().getVehiclePlate(),
                t.getLocation(),
                t.getIssueDate(), t.getDueDate(),
                String.format("%.0f",
                    t.getTotalFineAmount()),
                t.getStatus().name(),
                t.getOfficer().getFullName()
            });
        }
        lblSummary.setText("Total: " + list.size()
            + " ticket(s)  |  Total Fines: RWF "
            + String.format("%,.0f", total));
    }

    private void loadPayments() throws Exception {
        List<Payment> list = ClientConnector
            .paymentService.findAllPaymentRecords();
        tableModel.setColumnIdentifiers(new String[]{
            "Pay Ref #","Ticket #","Driver",
            "Amount (RWF)","Date","Method",
            "Received By"});
        double total = 0;
        for (Payment p : list) {
            total += p.getAmountPaid();
            tableModel.addRow(new Object[]{
                p.getPaymentReference(),
                p.getTicket().getTicketNumber(),
                p.getTicket().getDriver().getFullName(),
                String.format("%.0f",p.getAmountPaid()),
                p.getPaymentDate(),
                p.getPaymentMethod().name(),
                p.getReceivedBy()
            });
        }
        lblSummary.setText("Total Payments: "
            + list.size() + "  |  Total Collected: RWF "
            + String.format("%,.0f", total));
    }

    private void loadDrivers() throws Exception {
        List<Driver> list = ClientConnector
            .driverService.findAllDriverRecords();
        tableModel.setColumnIdentifiers(new String[]{
            "License No.","Full Name","National ID",
            "Phone","Email","Vehicle Plate",
            "Vehicle Model"});
        for (Driver d : list) {
            tableModel.addRow(new Object[]{
                d.getLicenseNumber(),d.getFullName(),
                d.getNationalId(),d.getPhoneNumber(),
                d.getEmail(),d.getVehiclePlate(),
                d.getVehicleModel()
            });
        }
        lblSummary.setText("Total Drivers: "
                + list.size());
    }

    private void loadViolations() throws Exception {
        List<ViolationType> list = ClientConnector
            .violationTypeService
            .findAllViolationTypeRecords();
        tableModel.setColumnIdentifiers(new String[]{
            "Code","Name","Fine Amount (RWF)",
            "Multiplier","Severity","Description"});
        for (ViolationType vt : list) {
            tableModel.addRow(new Object[]{
                vt.getCode(), vt.getName(),
                String.format("%.0f",
                    vt.getFineAmount()),
                vt.getPenaltyMultiplier(),
                vt.getSeverityLevel(),
                vt.getDescription()
            });
        }
        lblSummary.setText("Total Violation Types: "
                + list.size());
    }

    private void exportPDF() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(
            cmbReport.getSelectedItem()
                .toString().replace(" ","_")+".pdf"));
        if (fc.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) return;
        try {
            Document doc = new Document(
                    PageSize.A4.rotate());
            PdfWriter.getInstance(doc,
                new FileOutputStream(fc.getSelectedFile()));
            doc.open();

            com.itextpdf.text.Font tf =
                new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily
                    .HELVETICA, 14,
                com.itextpdf.text.Font.BOLD,
                BaseColor.WHITE);

            PdfPTable hdr = new PdfPTable(1);
            hdr.setWidthPercentage(100);
            PdfPCell hc = new PdfPCell(
                new Phrase("Traffic Policy "
                + "Management System\n"
                + cmbReport.getSelectedItem(), tf));
            hc.setBackgroundColor(
                new BaseColor(31,73,125));
            hc.setPadding(12);
            hc.setHorizontalAlignment(
                Element.ALIGN_CENTER);
            hdr.addCell(hc);
            doc.add(hdr);
            doc.add(new Paragraph(" "));

            com.itextpdf.text.Font sf =
                new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily
                    .HELVETICA, 9,
                com.itextpdf.text.Font.ITALIC);
            doc.add(new Paragraph(
                lblSummary.getText(), sf));
            doc.add(new Paragraph(" "));

            int cols = tableModel.getColumnCount();
            PdfPTable pt = new PdfPTable(cols);
            pt.setWidthPercentage(100);

            com.itextpdf.text.Font hf =
                new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily
                    .HELVETICA, 8,
                com.itextpdf.text.Font.BOLD,
                BaseColor.WHITE);
            com.itextpdf.text.Font cf =
                new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily
                    .HELVETICA, 8);

            for (int c=0; c<cols; c++) {
                PdfPCell cell = new PdfPCell(
                    new Phrase(
                        tableModel.getColumnName(c),hf));
                cell.setBackgroundColor(
                    new BaseColor(31,73,125));
                cell.setPadding(5);
                pt.addCell(cell);
            }
            for (int r=0; r<tableModel.getRowCount();r++){
                for (int c=0; c<cols; c++) {
                    Object v =
                        tableModel.getValueAt(r,c);
                    PdfPCell cell = new PdfPCell(
                        new Phrase(
                            v!=null?v.toString():"",cf));
                    cell.setPadding(4);
                    if (r%2==0) cell.setBackgroundColor(
                        new BaseColor(232,240,254));
                    pt.addCell(cell);
                }
            }
            doc.add(pt);
            doc.close();

            JOptionPane.showMessageDialog(this,
                "PDF exported successfully!",
                "Export Done",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "PDF export failed: "+ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(
            cmbReport.getSelectedItem()
                .toString().replace(" ","_")+".xlsx"));
        if (fc.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) return;
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet(
                (String)cmbReport.getSelectedItem());

            CellStyle hs = wb.createCellStyle();
            hs.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex());
            hs.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);
            Font hf = wb.createFont();
            hf.setColor(IndexedColors.WHITE.getIndex());
            hf.setBold(true);
            hs.setFont(hf);

            CellStyle as = wb.createCellStyle();
            as.setFillForegroundColor(
                IndexedColors.LIGHT_CORNFLOWER_BLUE
                    .getIndex());
            as.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);

            Row hr = sheet.createRow(0);
            for (int c=0;c<tableModel.getColumnCount();c++){
                Cell cell = hr.createCell(c);
                cell.setCellValue(
                    tableModel.getColumnName(c));
                cell.setCellStyle(hs);
            }
            for (int r=0;r<tableModel.getRowCount();r++){
                Row row = sheet.createRow(r+1);
                for (int c=0;
                        c<tableModel.getColumnCount();
                        c++) {
                    Cell cell = row.createCell(c);
                    Object v = tableModel.getValueAt(r,c);
                    cell.setCellValue(
                        v!=null?v.toString():"");
                    if (r%2!=0)
                        cell.setCellStyle(as);
                }
            }
            for (int c=0;
                    c<tableModel.getColumnCount();c++)
                sheet.autoSizeColumn(c);

            try (FileOutputStream fo =
                    new FileOutputStream(
                        fc.getSelectedFile())) {
                wb.write(fo);
            }
            JOptionPane.showMessageDialog(this,
                "Excel exported successfully!",
                "Export Done",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Excel failed: "+ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void exportCSV() {
    JFileChooser fc = new JFileChooser();
    fc.setSelectedFile(new File(
        cmbReport.getSelectedItem()
            .toString().replace(" ", "_") + ".csv"));
    if (fc.showSaveDialog(this)
            != JFileChooser.APPROVE_OPTION) return;
    try (java.io.PrintWriter pw =
            new java.io.PrintWriter(
                new java.io.FileWriter(
                    fc.getSelectedFile()))) {
        StringBuilder header = new StringBuilder();
        for (int c = 0; c < tableModel.getColumnCount(); c++) {
            if (c > 0) header.append(",");
            header.append("\"")
                  .append(tableModel.getColumnName(c))
                  .append("\"");
        }
        pw.println(header.toString());
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            StringBuilder row = new StringBuilder();
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                if (c > 0) row.append(",");
                Object val = tableModel.getValueAt(r, c);
                row.append("\"")
                   .append(val != null ? val.toString()
                           .replace("\"", "\"\"") : "")
                   .append("\"");
            }
            pw.println(row.toString());
        }
        JOptionPane.showMessageDialog(this,
            "CSV exported successfully!",
            "Export Done",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
            "CSV export failed: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}
