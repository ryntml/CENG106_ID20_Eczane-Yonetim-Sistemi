import java.sql.*;
import java.util.*;

public class SatisDAO {
    private Connection conn;
    public SatisDAO(Connection conn) {
        this.conn = conn;
    }
    public void satisEkle(Satis satis) throws SQLException {
        String sql = "INSERT INTO Satis (barkod, ilacAd, miktar, fiyat, toplamFiyat, musteriAd, musteriTc, tarih) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, satis.getBarkod());
            stmt.setString(2, satis.getIlacAd());
            stmt.setInt(3, satis.getMiktar());
            stmt.setDouble(4, satis.getFiyat());
            stmt.setDouble(5, satis.getToplamFiyat());
            stmt.setString(6, satis.getMusteriAd());
            stmt.setString(7, satis.getMusteriTc());
            stmt.setString(8, satis.getTarih());
            stmt.executeUpdate();
        }
    }
    public Satis satisBul(int id) throws SQLException {
        String sql = "SELECT * FROM Satis WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Satis(
                    rs.getInt("id"),
                    rs.getString("barkod"),
                    rs.getString("ilacAd"),
                    rs.getInt("miktar"),
                    rs.getDouble("fiyat"),
                    rs.getDouble("toplamFiyat"),
                    rs.getString("musteriAd"),
                    rs.getString("musteriTc"),
                    rs.getString("tarih")
                );
            }
        }
        return null;
    }
    public void satisSil(int id) throws SQLException {
        String sql = "DELETE FROM Satis WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public List<Satis> satislariListele() throws SQLException {
        List<Satis> list = new ArrayList<>();
        String sql = "SELECT * FROM Satis";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Satis(
                    rs.getInt("id"),
                    rs.getString("barkod"),
                    rs.getString("ilacAd"),
                    rs.getInt("miktar"),
                    rs.getDouble("fiyat"),
                    rs.getDouble("toplamFiyat"),
                    rs.getString("musteriAd"),
                    rs.getString("musteriTc"),
                    rs.getString("tarih")
                ));
            }
        }
        return list;
    }
} 