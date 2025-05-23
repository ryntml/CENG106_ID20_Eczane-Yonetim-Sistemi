import java.sql.*;

public class YoneticiDAO {
    public Yonetici findByKullaniciAdi(String kullaniciAdi) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM yoneticiler WHERE kullanici_adi=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kullaniciAdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Yonetici y = new Yonetici();
                y.setId(rs.getInt("id"));
                y.setKullaniciAdi(rs.getString("kullanici_adi"));
                y.setSifre(rs.getString("sifre"));
                y.setAd(rs.getString("ad"));
                y.setSoyad(rs.getString("soyad"));
                y.setEmail(rs.getString("email"));
                y.setTelefon(rs.getString("telefon"));
                return y;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 