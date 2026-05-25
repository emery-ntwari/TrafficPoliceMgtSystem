/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.util;

/**
 *
 * @author PC
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class UITheme {
    
    public static final Color PRIMARY      = new Color(31, 73, 125);
    public static final Color PRIMARY_DARK = new Color(18, 50, 90);
    public static final Color ACCENT       = new Color(220, 53, 69);
    public static final Color SUCCESS      = new Color(40, 167, 69);
    public static final Color WARNING      = new Color(255, 165, 0);
    public static final Color WHITE        = Color.WHITE;
    public static final Color LIGHT_GRAY   = new Color(245, 245, 250);
    public static final Color BORDER_COLOR = new Color(200, 210, 225);
    public static final Color TEXT_DARK    = new Color(33, 37, 41);
    public static final Color TEXT_MUTED   = new Color(108, 117, 125);
    public static final Color TABLE_HEADER = new Color(31, 73, 125);
    public static final Color TABLE_ROW_ALT= new Color(232, 240, 254);

    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_TABLE    = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE_HDR= new Font("Segoe UI", Font.BOLD,  12);

    public static void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(130, 35));
    }

    public static void styleField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    public static void styleTable(
        javax.swing.JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(26);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(WHITE);
        table.setShowGrid(true);
        javax.swing.table.JTableHeader h =
                table.getTableHeader();
        h.setBackground(TABLE_HEADER);
        h.setForeground(WHITE);
        h.setFont(FONT_TABLE_HDR);
        h.setReorderingAllowed(false);
    }
}
