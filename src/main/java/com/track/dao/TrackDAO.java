package com.track.dao;

import com.track.model.Track;
import com.track.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackDAO {

    private static final String INSERT = "INSERT INTO tracks(date, income, expense, description) VALUES(?,?,?,?)";
    private static final String SELECT_ALL = "SELECT * FROM tracks";
    private static final String TOTAL_INCOME = "SELECT SUM(income) FROM tracks";
    private static final String TOTAL_EXPENSE = "SELECT SUM(expense) FROM tracks";
    private static final String MONTHLY = "SELECT * FROM tracks WHERE MONTH(date)=? AND YEAR(date)=?";
    private static final String YEARLY = "SELECT * FROM tracks WHERE YEAR(date)=?";

    // Add Track
    public void add(Track t) throws SQLException {
        try (Connection c = DatabaseConnection.getDBConnection();
                PreparedStatement ps = c.prepareStatement(INSERT)) {

            ps.setDate(1, Date.valueOf(t.getDate()));
            ps.setDouble(2, t.getIncome());
            ps.setDouble(3, t.getExpense());
            ps.setString(4, t.getDescription());
            ps.executeUpdate();
        }
    }

    // Get All Tracks
    public List<Track> getAll() throws SQLException {
        List<Track> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getDBConnection();
                PreparedStatement ps = c.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Track(
                        rs.getDate("date").toLocalDate(),
                        rs.getDouble("income"),
                        rs.getDouble("expense"),
                        rs.getString("description"),
                        rs.getInt("id")));
            }
        }
        return list;
    }

    // Total Income
    public double totalIncome() throws SQLException {
        try (Connection c = DatabaseConnection.getDBConnection();
                PreparedStatement ps = c.prepareStatement(TOTAL_INCOME);
                ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // Total Expense
    public double totalExpense() throws SQLException {
        try (Connection c = DatabaseConnection.getDBConnection();
                PreparedStatement ps = c.prepareStatement(TOTAL_EXPENSE);
                ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // Monthly Tracks
    public List<Track> monthly(int month, int year) throws SQLException {
        List<Track> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getDBConnection();
                PreparedStatement ps = c.prepareStatement(MONTHLY)) {

            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Track(
                        rs.getDate("date").toLocalDate(),
                        rs.getDouble("income"),
                        rs.getDouble("expense"),
                        rs.getString("description"),
                        rs.getInt("id")));
            }
        }
        return list;
    }

    // Yearly Tracks
    public List<Track> yearly(int year) throws SQLException {
        List<Track> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getDBConnection();
                PreparedStatement ps = c.prepareStatement(YEARLY)) {

            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Track(
                        rs.getDate("date").toLocalDate(),
                        rs.getDouble("income"),
                        rs.getDouble("expense"),
                        rs.getString("description"),
                        rs.getInt("id")));
            }
        }
        return list;
    }

    // Calculate Balance
    public double balance() throws SQLException {
        return totalIncome() - totalExpense();
    }
}
