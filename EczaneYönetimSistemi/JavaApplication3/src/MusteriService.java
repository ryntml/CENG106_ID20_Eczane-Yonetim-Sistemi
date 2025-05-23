import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MusteriService {
    private MusteriDAO musteriDAO;
    public MusteriService(Connection conn) {
        this.musteriDAO = new MusteriDAO(conn);
    }
    public void musteriEkle(Musteri musteri) throws SQLException {
        musteriDAO.musteriEkle(musteri);
    }
    public Musteri musteriBul(int id) throws SQLException {
        return musteriDAO.musteriBul(id);
    }
    public void musteriSil(int id) throws SQLException {
        musteriDAO.musteriSil(id);
    }
    public void musteriGuncelle(Musteri musteri) throws SQLException {
        musteriDAO.musteriGuncelle(musteri);
    }
    public List<Musteri> musterileriListele() throws SQLException {
        return musteriDAO.musterileriListele();
    }
} 