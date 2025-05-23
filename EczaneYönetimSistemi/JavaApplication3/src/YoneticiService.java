public class YoneticiService {
    private YoneticiDAO yoneticiDAO = new YoneticiDAO();

    public boolean girisYap(String kullaniciAdi, String sifre) {
        Yonetici y = yoneticiDAO.findByKullaniciAdi(kullaniciAdi);
        return y != null && y.getSifre().equals(sifre);
    }
    public Yonetici getYonetici(String kullaniciAdi) {
        return yoneticiDAO.findByKullaniciAdi(kullaniciAdi);
    }
}
