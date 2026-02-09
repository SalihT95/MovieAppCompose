# 🎬 MovieAppCompose

**MovieAppCompose**, Android dünyasının en güncel teknolojisi olan **Jetpack Compose** ile geliştirilmiş, **Clean Architecture** ve **MVVM** prensiplerine tam uyumlu modern bir film keşif uygulamasıdır.

XML kullanılmadan tamamen **Declarative UI** yaklaşımıyla yazılan bu proje, **TMDB (The Movie Database) API** verilerini kullanarak kullanıcılara zengin bir içerik sunar.

## 📱 Ekran Görüntüleri ve Özellikler

Uygulamanın %100 Compose ile geliştirilen arayüzünden kareler:

| Giriş / Splash | Ana Sayfa | Film Detayı |
|:---:|:---:|:---:|
| <img src="Screenshots/Screenshot1.png" width="250" alt="Splash Screen"> | <img src="Screenshots/Screenshot2.png" width="250" alt="Home Screen"> | <img src="Screenshots/Screenshot3.png" width="250" alt="Detail Screen"> |

| Arama / Liste | Profil / Ayarlar | 🎥 Canlı Demo |
|:---:|:---:|:---:|
| <img src="Screenshots/Screenshot4.png" width="250" alt="Search Screen"> | <img src="Screenshots/Screenshot5.png" width="250" alt="Profile Screen"> | **[▶️ Videoyu İzle](Screenshots/Screen_recording_youtube.webm)**<br>*(WebM formatında kayıt)* |

## ✨ Temel Özellikler

* **Modern UI:** Tamamen **Jetpack Compose** (Material 3) ile geliştirilmiş akıcı ve reaktif arayüzler.
* **Oyuncu Detayları:** Aktörlerin biyografileri, doğum tarihleri ve rol aldıkları diğer yapımlar (Filmografi).
* **Gelişmiş Arama:** TMDB veritabanında film ve oyuncu arama.
* **Firebase Entegrasyonu:**
    * **Auth:** Email/Şifre ile güvenli kullanıcı girişi ve kaydı.
    * **Firestore:** Favori filmleri buluta kaydetme ve senkronizasyon.
* **Misafir Girişi (Guest User):** Kayıt zorunluluğu olmadan uygulamayı deneyimleme imkanı.
* **Dinamik Animasyonlar:** Compose Animation API ile zengin geçiş efektleri.

## 🛠️ Teknoloji Yığını (Tech Stack)

Proje, modern Android geliştirme standartlarına uygun olarak inşa edilmiştir:

* **Dil:** Kotlin (%100)
* **UI Toolkit:** **Jetpack Compose** (No XML)
* **Mimari:** Clean Architecture & MVVM (Model-View-ViewModel)
* **Asenkron İşlemler:** Coroutines & Flow
* **Ağ (Network):** Retrofit & OkHttp
* **Görsel Yükleme:** Coil (Compose-first image loading)
* **Dependency Injection:** Hilt
* **Navigasyon:** Jetpack Navigation Compose
* **Veri Tabanı (Local):** Room Database (Önbellekleme için)
* **Diğer:** Accompanist (Pager vb. için), Serialization

## 📂 Proje Mimarisi

Uygulama, sorumlulukların ayrılması (Separation of Concerns) ilkesine dayanan katmanlı bir yapıya sahiptir:

* **Domain Layer:** İş mantığı (Use Cases), Repository arayüzleri ve Model sınıfları. (Platform bağımsız)
* **Data Layer:** API çağrıları, Veritabanı işlemleri ve Repository implementasyonları.
* **Presentation Layer:** UI (Composables) ve ViewModel (State Management).

## 🚀 Kurulum

Projeyi yerel ortamınızda çalıştırmak için:

1.  Repoyu klonlayın:
    ```bash
    git clone [https://github.com/SalihT95/MovieAppCompose.git](https://github.com/SalihT95/MovieAppCompose.git)
    ```
2.  Android Studio (Giraffe veya daha yeni sürüm) ile projeyi açın.
3.  Gradle senkronizasyonunun tamamlanmasını bekleyin.
4.  **API Key:** TMDB API anahtarınızı `local.properties` dosyasına ekleyin:
    ```properties
    tmdb_api_key="SENIN_API_ANAHTARIN"
    ```
5.  Uygulamayı çalıştırın!

## 📞 İletişim

**Geliştirici:** Salih Türkoğlu
* GitHub: [@SalihT95](https://github.com/SalihT95)
* Web: [salihturkoglu.dev](https://salihturkoglu.dev)

---
*Bu proje açık kaynaklıdır ve eğitim amaçlı geliştirilmiştir.*
