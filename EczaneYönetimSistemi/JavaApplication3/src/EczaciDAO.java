import java.sql.*;

public class EczaciDAO {
    public Eczaci findByKullaniciAdi(String kullaniciAdi) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM eczacilar WHERE kullanici_adi=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kullaniciAdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Eczaci e = new Eczaci();
                e.setId(rs.getInt("id"));
                e.setKullaniciAdi(rs.getString("kullanici_adi"));
                e.setSifre(rs.getString("sifre"));
                e.setAd(rs.getString("ad"));
                e.setSoyad(rs.getString("soyad"));
                e.setEmail(rs.getString("email"));
                e.setTelefon(rs.getString("telefon"));
                e.setFotoPath(rs.getString("foto_path"));
                return e;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
 