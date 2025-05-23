public class EczaciService {
    private EczaciDAO eczaciDAO = new EczaciDAO();

    public boolean girisYap(String kullaniciAdi, String sifre) {
        Eczaci e = eczaciDAO.findByKullaniciAdi(kullaniciAdi);
        return e != null && e.getSifre().equals(sifre);
    }
    public Eczaci getEczaci(String kullaniciAdi) {
        return eczaciDAO.findByKullaniciAdi(kullaniciAdi);
    }
}
 