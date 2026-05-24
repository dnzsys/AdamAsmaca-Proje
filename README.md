# 🎮 Adam Asmaca - Java Swing

Bu proje, Java ve Swing GUI kullanılarak geliştirilmiş masaüstü tabanlı bir Adam Asmaca oyunudur. Üniversite proje ödevi kapsamında geliştirilmiş olup, dosya okuma/yazma işlemleri, dinamik arayüz yönetimi ve temel şifreleme mantıklarını içermektedir.

## 🚀 Özellikler

- **Şifreli Giriş Sistemi:** Oyuna girişler ve log/skor temizleme işlemleri belirlenen bir şifre ile korunmaktadır.
- **Dinamik Arayüz (JTabbedPane):** Oyun, Skorlar ve Loglar olmak üzere 3 farklı sekmeden oluşur.
- **Dosya İşlemleri:** - `kelimeler.txt`: Rastgele seçilen kelimelerin tutulduğu kaynak.
  - `oyunlar.txt`: Oynanan oyunların süre ve kazanma/kaybetme durumlarının kaydedildiği dosya.
  - `log.txt`: Uygulamaya yapılan tüm başarılı/başarısız girişlerin saatleriyle tutulduğu kayıt defteri.
- **Zamanlayıcı:** Oyun esnasında geçen süreyi saniye bazında takip eden sayaç.

## 📁 Dosya ve Klasör Yapısı
Uygulamanın çalışması için bilgisayarın `C:\` sürücüsünde aşağıdaki yapının bulunması gerekmektedir:
```text
C:\
└── P2Oyun\
    ├── Resimler\ (1.jpg - 11.jpg arası adam asmaca görselleri)
    └── TXTDosyalar\
        ├── kelimeler.txt
        ├── sifre.txt
        ├── log.txt
        └── oyunlar.txt
