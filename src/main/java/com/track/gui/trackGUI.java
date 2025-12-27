package com.track.gui;

import com.track.dao.TrackDAO;
import com.track.model.Track;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TrackGUI extends JFrame {

    private JTextField incomeField, expenseField, descField, dateField;
    private JLabel totalIncomeLabel, totalExpenseLabel, balanceLabel;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> filterBox;

    private TrackDAO dao = new TrackDAO();

    public TrackGUI() {
        setTitle("Expense Tracker");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---------- TOP PANEL ---------- Income / Expense / Desc / Date ----------
        JPanel topPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        incomeField = new JTextField();
        expenseField = new JTextField();
        descField = new JTextField();
        dateField = new JTextField(); // yyyy-MM-dd

        JButton addBtn = new JButton("ADD");
        JButton refreshBtn = new JButton("REFRESH");

        topPanel.add(new JLabel("Income:"));
        topPanel.add(new JLabel("Expense:"));
        topPanel.add(new JLabel("Description:"));
        topPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        topPanel.add(new JLabel(""));

        topPanel.add(incomeField);
        topPanel.add(expenseField);
        topPanel.add(descField);
        topPanel.add(dateField);
        topPanel.add(addBtn);

        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[] { "ID", "Date", "Income", "Expense", "Description", "Balance" }, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---------- BOTTOM PANEL ----------
        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        totalIncomeLabel = new JLabel("Total Income: 0");
        totalExpenseLabel = new JLabel("Total Expense: 0");
        balanceLabel = new JLabel("Balance: 0");

        filterBox = new JComboBox<>(new String[] { "All", "Monthly", "Yearly", "Date" });
        JTextField filterField = new JTextField(); // month number / year / date
        JButton filterBtn = new JButton("FILTER");

        bottomPanel.add(totalIncomeLabel);
        bottomPanel.add(totalExpenseLabel);
        bottomPanel.add(balanceLabel);
        bottomPanel.add(new JLabel("")); // empty
        bottomPanel.add(new JLabel("Filter:"));
        bottomPanel.add(filterBox);
        bottomPanel.add(filterField);
        bottomPanel.add(filterBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------- ACTIONS ----------
        addBtn.addActionListener(e -> addTrack());
        refreshBtn.addActionListener(e -> loadAll());

        filterBtn.addActionListener(e -> {
            String filterType = (String) filterBox.getSelectedItem();
            String value = filterField.getText();
            try {
                switch (filterType) {
                    case "All":
                        loadAll();
                        break;
                    case "Monthly":
                        String[] parts = value.split("-"); // "yyyy-MM"
                        int year = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);
                        loadMonthly(month, year);
                        break;
                    case "Yearly":
                        int y = Integer.parseInt(value);
                        loadYearly(y);
                        break;
                    case "Date":
                        LocalDate date = LocalDate.parse(value);
                        loadDateWise(date);
                        break;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input: " + ex.getMessage());
            }
        });

        loadAll();
    }

    private void addTrack() {
        try {
            double income = incomeField.getText().isEmpty() ? 0 : Double.parseDouble(incomeField.getText());
            double expense = expenseField.getText().isEmpty() ? 0 : Double.parseDouble(expenseField.getText());
            String desc = descField.getText();
            LocalDate date = dateField.getText().isEmpty() ? LocalDate.now() : LocalDate.parse(dateField.getText());

            Track t = new Track(date, income, expense, desc, 0);
            dao.add(t);

            incomeField.setText("");
            expenseField.setText("");
            descField.setText("");
            dateField.setText("");

            loadAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadAll() {
        try {
            model.setRowCount(0);
            List<Track> list = dao.getAll();
            for (Track t : list) {
                model.addRow(new Object[] {
                        t.getId(),
                        t.getDate(),
                        t.getIncome(),
                        t.getExpense(),
                        t.getDescription(),
                        t.getBalance()
                });
            }
            refreshTotals();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadMonthly(int month, int year) {
        try {
            model.setRowCount(0);
            List<Track> list = dao.monthly(month, year);
            for (Track t : list) {
                model.addRow(new Object[] {
                        t.getId(),
                        t.getDate(),
                        t.getIncome(),
                        t.getExpense(),
                        t.getDescription(),
                        t.getBalance()
                });
            }
            refreshTotals(list);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadYearly(int year) {
        try {
            model.setRowCount(0);
            List<Track> list = dao.yearly(year);
            for (Track t : list) {
                model.addRow(new Object[] {
                        t.getId(),
                        t.getDate(),
                        t.getIncome(),
                        t.getExpense(),
                        t.getDescription(),
                        t.getBalance()
                });
            }
            refreshTotals(list);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadDateWise(LocalDate date) {
        try {
            model.setRowCount(0);
            List<Track> list = dao.getAll();
            for (Track t : list) {
                if (t.getDate().equals(date)) {
                    model.addRow(new Object[] {
                            t.getId(),
                            t.getDate(),
                            t.getIncome(),
                            t.getExpense(),
                            t.getDescription(),
                            t.getBalance()
                    });
                }
            }
            refreshTotals(list);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void refreshTotals() throws SQLException {
        totalIncomeLabel.setText("Total Income: " + dao.totalIncome());
        totalExpenseLabel.setText("Total Expense: " + dao.totalExpense());
        balanceLabel.setText("Balance: " + dao.balance());
    }

    private void refreshTotals(List<Track> list) {
        double income = list.stream().mapToDouble(Track::getIncome).sum();
        double expense = list.stream().mapToDouble(Track::getExpense).sum();
        totalIncomeLabel.setText("Total Income: " + income);
        totalExpenseLabel.setText("Total Expense: " + expense);
        balanceLabel.setText("Balance: " + (income - expense));
    }

    public static void main(String[] args) {
        new TrackGUI().setVisible(true);
    }
}
