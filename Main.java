import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * =============================================================
 * PROJE: Trie Tabanlı Otomatik Tamamlama Sistemi (Autocomplete)
 * =============================================================
 * Açıklama:
 * Bu sınıf, uygulamanın giriş noktasıdır (Main Class).
 * Kullanıcıdan dil seçimi alır, ilgili veri setini (CSV) yükler
 * ve Trie veri yapısı üzerinde milisaniyeler içinde arama yapar.
 *
 * Veri Yapısı: Trie (Prefix Tree)
 * Zaman Karmaşıklığı: O(L) - L: Kelime uzunluğu
 *
 * @author [Sümeyye Öztürk]
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Trie trie = new Trie();
        String secilenDosya = "";

        // --- 1. AŞAMA: KULLANICI ARAYÜZÜ VE DİL SEÇİMİ ---
        System.out.println("\n");
        System.out.println("==================================================");
        System.out.println("|    OTOMATİK DOLDURMA MOTORU (AUTOCOMPLETE)     |");
        System.out.println("|           Veri Yapıları Final Projesi          |");
        System.out.println("==================================================");
        System.out.println("\n\n");
        System.out.println("Lütfen veri seti dilini seçiniz / Select Language:");
        System.out.println("[1] Türkçe (Turkish Data)");
        System.out.println("[2] English (İngilizce Data)");
        System.out.print("Seçiminiz / Your choice (1-2): ");

        String secim = scanner.nextLine();

        if (secim.equals("1")) {
            secilenDosya = "sozluk_tr.csv";
            System.out.println("\n>> Türkçe modu aktif edildi.");
        } else if (secim.equals("2")) {
            secilenDosya = "sozluk_en.csv";
            System.out.println("\n>> English mode activated.");
        } else {
            System.out.println(">> Hatalı seçim! Varsayılan (Türkçe) yükleniyor...");
            secilenDosya = "sozluk_tr.csv";
        }

        // --- 2. AŞAMA: DOSYADAN KELİMELERİ YÜKLEME ---
        // Not: Veriler CSV formatında 'Ham Veri' (Raw Data) olarak tutulmaktadır.
        // Kelime tekrar sıklığına göre (Frequency) dinamik puanlama yapılır.
        System.out.println("Veri seti belleğe yükleniyor... (" + secilenDosya + ")");
        long baslangic = System.currentTimeMillis(); // Performans ölçümü başlat

        try {
            // Türkçe karakterlerin bozulmaması için UTF-8 kodlaması kullandım.
            Scanner dosyaOkuyucu = new Scanner(new File(secilenDosya), "UTF-8");

            int kelimeSayisi = 0;
            while (dosyaOkuyucu.hasNextLine()) {

                String kelime = dosyaOkuyucu.nextLine().trim().toLowerCase();

                if (!kelime.isEmpty()) {
                    trie.insert(kelime);
                    kelimeSayisi++;
                }
            }
            dosyaOkuyucu.close();

            long bitis = System.currentTimeMillis();
            // Yükleme süresini milisaniye cinsinden kullanıcıya göster
            System.out.println(">> BAŞARILI: " + kelimeSayisi + " kelime " + (bitis - baslangic) + " ms içinde indexlendi.\n");

        } catch (FileNotFoundException e) {
            System.out.println("HATA: Veri dosyası (" + secilenDosya + ") bulunamadı!");
            System.out.println("ÇÖZÜM: Lütfen 'sozluk_tr.csv' ve 'sozluk_en.csv' dosyalarını proje ana dizinine ekleyin.");
            return;
        } catch (Exception e) {
            System.out.println("Beklenmedik bir hata oluştu: " + e.getMessage());
            return;
        }

        // --- 3. AŞAMA: ETKİLEŞİMLİ ARAMA DÖNGÜSÜ ---
        System.out.println("Arama motoru hazır! (Çıkış için 'q' veya 'exit' yazın)");
        System.out.println("--------------------------------------------------");

        while (true) {
            System.out.print("Kelime Ara: ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Çıkış koşulu
            if (input.equals("q") || input.equals("exit")) {
                System.out.println("Program kapatılıyor... İyi günler!");
                break;
            }

            if (input.isEmpty()) { continue; }

            // Trie üzerinde arama yap (Maliyet: Kelime uzunluğu kadar işlem O(L) )
            List<String> sonuclar = trie.autoComplete(input);

            if (sonuclar.isEmpty()) {
                System.out.println("-> Sonuç bulunamadı (No results).");
            } else {
                // Sonuçları listele (Ekranı doldurmamak için limit koydum istenirse ekranda gösterilecek kelime sayısı buradan değiştirilebilir)
                int limit = Math.min(sonuclar.size(), 15);

                System.out.println("-> '" + input + "' için en popüler " + limit + " sonuç:");
                for (int i = 0; i < limit; i++) {
                    System.out.println("   * " + sonuclar.get(i));
                }
            }
            System.out.println("");
        }

        scanner.close();
    }
}