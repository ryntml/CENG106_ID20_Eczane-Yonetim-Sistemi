import java.sql.SQLException;
import java.util.List;

public class IlacService {
    private IlacDAO ilacDAO = new IlacDAO();

    public void ilacEkle(Ilac ilac) throws SQLException {
        ilacDAO.ekle(ilac);
    }
    public Ilac ilacBul(String barkod) throws SQLException {
        return ilacDAO.bul(barkod);
    }
    public void ilacSil(String barkod) throws SQLException {
        ilacDAO.sil(barkod);
    }
    public void ilacGuncelle(Ilac ilac) throws SQLException {
        ilacDAO.guncelle(ilac);
    }
    public List<Ilac> ilaclariListele() throws SQLException {
        return ilacDAO.listele();
    }
} 