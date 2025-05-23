 public class Satis {
    private int id;
    private String barkod;
    private String ilacAd;
    private int miktar;
    private double fiyat;
    private double toplamFiyat;
    private String musteriAd;
    private String musteriTc;
    private String tarih;

    public Satis() {}
    public Satis(int id, String barkod, String ilacAd, int miktar, double fiyat, double toplamFiyat, String musteriAd, String musteriTc, String tarih) {
        this.id = id;
        this.barkod = barkod;
        this.ilacAd = ilacAd;
        this.miktar = miktar;
        this.fiyat = fiyat;
        this.toplamFiyat = toplamFiyat;
        this.musteriAd = musteriAd;
        this.musteriTc = musteriTc;
        this.tarih = tarih;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBarkod() { return barkod; }
    public void setBarkod(String barkod) { this.barkod = barkod; }
    public String getIlacAd() { return ilacAd; }
    public void setIlacAd(String ilacAd) { this.ilacAd = ilacAd; }
    public int getMiktar() { return miktar; }
    public void setMiktar(int miktar) { this.miktar = miktar; }
    public double getFiyat() { return fiyat; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }
    public double getToplamFiyat() { return toplamFiyat; }
    public void setToplamFiyat(double toplamFiyat) { this.toplamFiyat = toplamFiyat; }
    public String getMusteriAd() { return musteriAd; }
    public void setMusteriAd(String musteriAd) { this.musteriAd = musteriAd; }
    public String getMusteriTc() { return musteriTc; }
    public void setMusteriTc(String musteriTc) { this.musteriTc = musteriTc; }
    public String getTarih() { return tarih; }
    public void setTarih(String tarih) { this.tarih = tarih; }
}
