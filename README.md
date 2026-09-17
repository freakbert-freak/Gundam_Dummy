# Gundam Info

Gundam Info adalah aplikasi Android sederhana untuk menampilkan informasi berbagai Mobile Suit Gundam menggunakan data dari Gundam API.

Aplikasi dibuat menggunakan **Kotlin** dan **XML Layout** di Android Studio. Data Gundam diambil dari API melalui `HttpURLConnection`, kemudian ditampilkan menggunakan `RecyclerView`.

## Preview

> Tambahkan screenshot aplikasi di bagian ini.

Contoh:

```text
Home / Gundam List           Detail Gundam
┌─────────────────┐          ┌─────────────────┐
│ GUNDAM DATABASE │          │ BACK       #004 │
│ Search...       │          │                 │
│                 │          │     IMAGE       │
│ [IMG] RX-78-2  >│          │                 │
│       Amuro Ray │          │ RX-78-2 GUNDAM │
│                 │          │ PILOT           │
│ [IMG] Gundam EX>│          │ Amuro Ray       │
│                 │          │                 │
│ PREV  1/55 NEXT │          │ OVERVIEW        │
└─────────────────┘          │                 │
                             │ OPEN GUNDAM WIKI│
                             └─────────────────┘
````

## Fitur

* Menampilkan daftar Mobile Suit Gundam.
* Menampilkan gambar Gundam dari API.
* Menampilkan nama Gundam.
* Menampilkan informasi pilot.
* Search Gundam.
* Search berdasarkan:

  * nama Gundam
  * nama pilot
  * informasi pada header/deskripsi
* Search dapat mencari data dari seluruh halaman API setelah semua data selesai dimuat.
* Pagination menggunakan tombol `PREV` dan `NEXT`.
* Menampilkan nomor halaman saat ini.
* Halaman detail Gundam.
* Menampilkan:

  * ID Gundam
  * gambar
  * nama
  * pilot
  * overview
* Tombol **OPEN GUNDAM WIKI** untuk membuka halaman Gundam Wiki.
* Loading state.
* Error state.
* Empty search state.

## API

Project ini menggunakan Gundam API:

```text
https://gundam-api.pages.dev/api/gundams
```

Contoh request:

```text
https://gundam-api.pages.dev/api/gundams?page=1
```

Contoh struktur response:

```json
{
  "info": {
    "count": 867,
    "pages": 55,
    "next": "https://gundam-api.pages.dev/api/gundams?page=2",
    "prev": null
  },
  "results": [
    {
      "id": 4,
      "wikiName": "RX-78-2_Gundam",
      "wikiUrl": "https://gundam.fandom.com/wiki/RX-78-2_Gundam",
      "name": "RX-78-2 Gundam",
      "header": "The RX-78-2 Gundam is the titular mobile suit...",
      "details": null,
      "imgUrl": "https://static.wikia.nocookie.net/gundam/images/...",
      "pilots": "Amuro Ray"
    }
  ]
}
```

> Jumlah Gundam dan halaman mengikuti data terbaru yang diberikan oleh API.

## Teknologi

Project dibuat menggunakan:

* Android Studio
* Kotlin
* XML Layout
* RecyclerView
* Material Components
* ConstraintLayout
* Glide
* HttpURLConnection
* JSONObject / JSONArray
* Thread
* Intent

## Struktur Project

```text
app/
└── src/main/
    ├── java/com/example/projek_gundam/
    │   ├── MainActivity.kt
    │   ├── DetailActivity.kt
    │   ├── Gundam.kt
    │   ├── GundamAdapter.kt
    │   └── ApiClient.kt
    │
    ├── res/
    │   ├── drawable/
    │   │   ├── ic_arrow_left.xml
    │   │   ├── ic_arrow_right.xml
    │   │   └── ic_external_link.xml
    │   │
    │   ├── layout/
    │   │   ├── activity_main.xml
    │   │   ├── activity_detail.xml
    │   │   └── item_gundam.xml
    │   │
    │   ├── mipmap/
    │   │   └── Launcher Icon
    │   │
    │   └── values/
    │       └── strings.xml
    │
    └── AndroidManifest.xml
```

## Penjelasan File

### `Gundam.kt`

Digunakan sebagai model data untuk menyimpan data Gundam yang didapat dari API.

Data yang digunakan antara lain:

```text
id
wikiName
wikiUrl
name
header
details
imgUrl
pilots
```

Selain itu terdapat `ApiInfo` dan `GundamResponse` untuk membaca informasi pagination dan daftar Gundam.

### `ApiClient.kt`

Digunakan untuk berkomunikasi dengan Gundam API.

Request dilakukan menggunakan:

```kotlin
HttpURLConnection
```

Contoh:

```kotlin
ApiClient.getGundams(1)
```

akan mengambil:

```text
https://gundam-api.pages.dev/api/gundams?page=1
```

Data JSON kemudian diubah menjadi objek `Gundam`.

### `MainActivity.kt`

Merupakan halaman utama aplikasi.

Bertanggung jawab untuk:

* mengambil data API
* menampilkan data ke RecyclerView
* menjalankan search
* mengatur pagination
* menangani loading dan error
* membuka halaman detail

### `GundamAdapter.kt`

Menghubungkan data `Gundam` dengan `RecyclerView`.

Setiap card menampilkan:

```text
Gambar Gundam
Nama Gundam
Pilot
Tombol menuju detail
```

Glide digunakan untuk mengambil gambar dari URL.

### `DetailActivity.kt`

Menampilkan informasi Gundam yang dipilih.

Data dikirim dari `MainActivity` melalui `Intent`.

Halaman ini menampilkan:

```text
ID
Gambar
Nama Gundam
Pilot
Overview
Gundam Wiki
```

### `activity_main.xml`

Layout halaman utama yang berisi:

* nama aplikasi
* subtitle
* Search Bar
* jumlah Mobile Suit
* RecyclerView
* tombol PREV
* nomor halaman
* tombol NEXT

### `item_gundam.xml`

Layout untuk setiap item di RecyclerView.

### `activity_detail.xml`

Layout untuk halaman detail Gundam.

## Cara Kerja Aplikasi

Alur utama aplikasi:

```text
Gundam API
     │
     ▼
ApiClient.kt
     │
     ▼
GundamResponse
     │
     ▼
MainActivity
     │
     ▼
GundamAdapter
     │
     ▼
RecyclerView
```

Ketika card Gundam ditekan:

```text
RecyclerView
     │
     ▼
GundamAdapter
     │
     ▼
Intent
     │
     ▼
DetailActivity
```

## Search

Search dilakukan secara lokal menggunakan data Gundam yang sudah didownload dari API.

Data dapat dicari berdasarkan:

```kotlin
gundam.name
gundam.pilots
gundam.header
```

Contoh pencarian:

```text
RX-78
Amuro Ray
Thunderbolt
Prototype
```

Untuk memungkinkan pencarian dari semua halaman, aplikasi mengambil seluruh halaman API dan menyimpannya ke dalam:

```kotlin
allGundams
```

Kemudian search menggunakan:

```kotlin
allGundams.filter { gundam ->
    gundam.name.contains(keyword, ignoreCase = true) ||
    gundam.pilots.contains(keyword, ignoreCase = true) ||
    gundam.header.contains(keyword, ignoreCase = true)
}
```

## Pagination

Pagination menggunakan informasi:

```json
{
  "next": "...",
  "prev": "...",
  "pages": 55
}
```

Aplikasi juga menyimpan:

```kotlin
currentPage
totalPages
```

Saat tombol `NEXT` ditekan:

```text
currentPage + 1
        ↓
getGundams(currentPage)
```

Saat tombol `PREV` ditekan:

```text
currentPage - 1
        ↓
getGundams(currentPage)
```

## Mengambil Gambar

Gambar Gundam ditampilkan menggunakan Glide:

```kotlin
Glide.with(holder.itemView.context)
    .load(gundam.imgUrl)
    .into(holder.image)
```

Dependency:

```kotlin
implementation("com.github.bumptech.glide:glide:4.16.0")
```

## Permission Internet

Karena aplikasi mengambil API dan gambar dari internet, tambahkan permission berikut pada:

```text
AndroidManifest.xml
```

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Instalasi

Clone repository:

```bash
git clone <URL-REPOSITORY>
```

Buka project menggunakan Android Studio.

Pastikan Gradle Sync berhasil.

Kemudian jalankan aplikasi menggunakan:

* Android Emulator
* atau perangkat Android fisik

Minimum Android SDK:

```text
API 24
```

## Design

Aplikasi menggunakan konsep:

```text
White-gray industrial mobile interface
```

Warna utama:

```text
Background        #F5F5F3
Card              #FFFFFF
Secondary         #E9EAEB
Primary Text      #202326
Secondary Text    #73787D
Border            #D2D5D7
Accent Red        #D1242F
```

Tampilan dibuat sederhana agar informasi Gundam menjadi fokus utama aplikasi.

## Catatan

Gundam Info merupakan project pembelajaran Android dan menggunakan API pihak ketiga sebagai sumber data.

Ketersediaan data, gambar, jumlah Gundam, dan jumlah halaman dapat berubah mengikuti API yang digunakan.

Beberapa data seperti `details` atau `pilots` dapat kosong. Aplikasi menangani kondisi tersebut dengan teks pengganti seperti:

```text
Pilot data unavailable
```

## Author

**Albert Dewa Ananta**

Project Android Studio - Gundam Info

```

Satu perubahan yang saya sarankan sebelum kamu upload ke GitHub: pada bagian contoh JSON, jangan terlalu mengandalkan angka `867` dan `55`, karena API dapat berubah. Di README di atas sudah saya beri catatan bahwa jumlah data mengikuti API.
```
