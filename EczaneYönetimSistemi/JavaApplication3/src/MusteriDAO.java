import java.sql.*;
import java.util.*;

public class MusteriDAO {
    private Connection conn;
    public MusteriDAO(Connection conn) {
        this.conn = conn;
    }
    public void musteriEkle(Musteri musteri) throws SQLException {
        String sql = "INSERT INTO Musteri (ad, soyad, tcNo, telefon, adres) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, musteri.getAd());
            stmt.setString(2, musteri.getSoyad());
            stmt.setString(3, musteri.getTcNo());
            stmt.setString(4, musteri.getTelefon());
            stmt.setString(5, musteri.getAdres());
            stmt.executeUpdate();
        }
    }
    public Musteri musteriBul(int id) throws SQLException {
        String sql = "SELECT * FROM Musteri WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Musteri(
                    rs.getInt("id"),
                    rs.getString("ad"),
                    rs.getString("soyad"),
                    rs.getString("tcNo"),
                    rs.getString("telefon"),
                    rs.getString("adres")
                );
            }
        }
        return null;
    }
    public void musteriSil(int id) throws SQLException {
        String sql = "DELETE FROM Musteri WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void musteriGuncelle(Musteri musteri) throws SQLException {
        String sql = "UPDATE Musteri SET ad=?, soyad=?, tcNo=?, telefon=?, adres=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, musteri.getAd());
            stmt.setString(2, musteri.getSoyad());
            stmt.setString(3, musteri.getTcNo());
            stmt.setString(4, musteri.getTelefon());
            stmt.setString(5, musteri.getAdres());
            stmt.setInt(6, musteri.getId());
            stmt.executeUpdate();
        }
    }
    public List<Musteri> musterileriListele() throws SQLException {
        List<Musteri> list = new ArrayList<>();
        String sql = "SELECT * FROM Musteri";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Musteri(
                    rs.getInt("id"),
                    rs.getString("ad"),
                    rs.getString("soyad"),
                    rs.getString("tcNo"),
                    rs.getString("telefon"),
                    rs.getString("adres")
                ));
            }
        }
        return list;
    }
} 