import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IlacDAO {
    public void ekle(Ilac ilac) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO ilaclar (barkod, ad, uretici, kategori, fiyat, stok_miktari, son_kullanma_tarihi, reçete_tipi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ilac.getBarkod());
            stmt.setString(2, ilac.getAd());
            stmt.setString(3, ilac.getUretici());
            stmt.setString(4, ilac.getKategori());
            stmt.setDouble(5, ilac.getFiyat());
            stmt.setInt(6, ilac.getStok_miktari());
            stmt.setString(7, ilac.getSon_kullanma_tarihi());
            stmt.setString(8, ilac.getRecete_tipi());
            stmt.executeUpdate();
        }
    }
    public Ilac bul(String barkod) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM ilaclar WHERE barkod=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, barkod);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ilac(
                    rs.getString("barkod"),
                    rs.getString("ad"),
                    rs.getString("uretici"),
                    rs.getString("kategori"),
                    rs.getDouble("fiyat"),
                    rs.getInt("stok_miktari"),
                    rs.getString("son_kullanma_tarihi"),
                    rs.getString("reçete_tipi")
                );
            }
        }
        return null;
    }
    public void sil(String barkod) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM ilaclar WHERE barkod=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, barkod);
            stmt.executeUpdate();
        }
    }
    public void guncelle(Ilac ilac) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE ilaclar SET ad=?, uretici=?, kategori=?, fiyat=?, stok_miktari=?, son_kullanma_tarihi=?, reçete_tipi=? WHERE barkod=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ilac.getAd());
            stmt.setString(2, ilac.getUretici());
            stmt.setString(3, ilac.getKategori());
            stmt.setDouble(4, ilac.getFiyat());
            stmt.setInt(5, ilac.getStok_miktari());
            stmt.setString(6, ilac.getSon_kullanma_tarihi());
            stmt.setString(7, ilac.getRecete_tipi());
            stmt.setString(8, ilac.getBarkod());
            stmt.executeUpdate();
        }
    }
    public List<Ilac> listele() throws SQLException {
        List<Ilac> ilaclar = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ilaclar");
            while (rs.next()) {
                ilaclar.add(new Ilac(
                    rs.getString("barkod"),
                    rs.getString("ad"),
                    rs.getString("uretici"),
                    rs.getString("kategori"),
                    rs.getDouble("fiyat"),
                    rs.getInt("stok_miktari"),
                    rs.getString("son_kullanma_tarihi"),
                    rs.getString("reçete_tipi")
                ));
            }
        }
        return ilaclar;
    }
} 