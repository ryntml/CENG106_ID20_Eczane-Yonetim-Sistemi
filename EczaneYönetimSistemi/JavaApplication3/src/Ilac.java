public class Ilac {
    private String barkod;
    private String ad;
    private String uretici;
    private String kategori;
    private double fiyat;
    private int stok_miktari;
    private String son_kullanma_tarihi;
    private String recete_tipi;

    public Ilac() {}

    public Ilac(String barkod, String ad, String uretici, String kategori, double fiyat, int stok_miktari, String son_kullanma_tarihi, String recete_tipi) {
        this.barkod = barkod;
        this.ad = ad;
        this.uretici = uretici;
        this.kategori = kategori;
        this.fiyat = fiyat;
        this.stok_miktari = stok_miktari;
        this.son_kullanma_tarihi = son_kullanma_tarihi;
        this.recete_tipi = recete_tipi;
    }

    public String getBarkod() { return barkod; }
    public void setBarkod(String barkod) { this.barkod = barkod; }
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    public String getUretici() { return uretici; }
    public void setUretici(String uretici) { this.uretici = uretici; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public double getFiyat() { return fiyat; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }
    public int getStok_miktari() { return stok_miktari; }
    public void setStok_miktari(int stok_miktari) { this.stok_miktari = stok_miktari; }
    public String getSon_kullanma_tarihi() { return son_kullanma_tarihi; }
    public void setSon_kullanma_tarihi(String son_kullanma_tarihi) { this.son_kullanma_tarihi = son_kullanma_tarihi; }
    public String getRecete_tipi() { return recete_tipi; }
    public void setRecete_tipi(String recete_tipi) { this.recete_tipi = recete_tipi; }
} 