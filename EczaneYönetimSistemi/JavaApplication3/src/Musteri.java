public class Musteri {
    private int id;
    private String ad;
    private String soyad;
    private String tcNo;
    private String telefon;
    private String adres;

    public Musteri() {}
    public Musteri(int id, String ad, String soyad, String tcNo, String telefon, String adres) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.tcNo = tcNo;
        this.telefon = telefon;
        this.adres = adres;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public String getTcNo() { return tcNo; }
    public void setTcNo(String tcNo) { this.tcNo = tcNo; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getAdres() { return adres; }
    public void setAdres(String adres) { this.adres = adres; }
} 