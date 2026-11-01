# MaintenanceMode

[![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
![Build Success](https://img.shields.io/badge/Build-Success-brightgreen?logo=githubactions&logoColor=white)

Plugin Paper ringan untuk mengaktifkan mode maintenance server. Saat maintenance aktif, pemain tanpa izin bypass akan ditolak saat bergabung. Pemain yang sudah online juga dapat dikeluarkan secara otomatis.

## Fitur

- Aktifkan, nonaktifkan, periksa status, dan muat ulang konfigurasi melalui command.
- Simpan status maintenance ke `config.yml`, termasuk setelah server dimulai ulang.
- Atur apakah pemain online tanpa izin bypass dikeluarkan saat maintenance diaktifkan.
- Ubah pesan plugin dan gunakan kode warna dengan format `&`.

## Persyaratan

- Paper **26.2** atau kompatibel dengan Paper API `26.2.build.129-stable`.
- Java **25**.

## Instalasi

1. Unduh `MaintenanceMode-1.0.0.jar` dari folder `target` atau buat JAR dari source.
2. Salin JAR ke folder `plugins` server Paper.
3. Mulai atau mulai ulang server.
4. Ubah `plugins/MaintenanceMode/config.yml` sesuai kebutuhan.

## Command

| Command | Alias | Keterangan |
|---|---|---|
| `/maintenance on` | `/maint on` | Aktifkan maintenance. |
| `/maintenance off` | `/maint off` | Nonaktifkan maintenance. |
| `/maintenance status` | `/maint status` | Tampilkan status maintenance. |
| `/maintenance reload` | `/maint reload` | Muat ulang konfigurasi dari file. |

Semua sub-command membutuhkan izin `maintenance.admin`.

## Permission

| Permission | Default | Keterangan |
|---|---|---|
| `maintenance.admin` | Operator (`op`) | Mengelola mode maintenance. |
| `maintenance.bypass` | Operator (`op`) | Bergabung ke server saat maintenance aktif dan tidak dikeluarkan ketika maintenance diaktifkan. |

## Konfigurasi

```yaml
maintenance:
  enabled: false
  kick-existing-players: true

messages:
  prefix: "&8[&cMaintenance&8] "
  kick: "&cServer sedang dalam maintenance!"
  join: "&cServer sedang maintenance. Silakan coba lagi nanti."
  enabled: "&aMaintenance berhasil diaktifkan."
  disabled: "&aMaintenance berhasil dinonaktifkan."
  already-enabled: "&eMaintenance sudah aktif."
  already-disabled: "&eMaintenance sudah nonaktif."
  no-permission: "&cKamu tidak memiliki permission."
  player-only: "&cCommand ini hanya bisa digunakan oleh pemain."
  reloaded: "&aKonfigurasi berhasil dimuat ulang."
```

- `maintenance.enabled`: status maintenance awal saat plugin dimulai.
- `maintenance.kick-existing-players`: keluarkan pemain online tanpa izin bypass saat maintenance diaktifkan.
- `messages.prefix`: awalan untuk pesan konfigurasi.
- Kode warna `&` didukung pada pesan yang dibaca dari konfigurasi.

## Build dari source

Pastikan Java 25 dan Maven tersedia, lalu jalankan dari direktori proyek:

```shell
mvn package
```

JAR akan dibuat di `target/MaintenanceMode-1.0.0.jar`.

## Lisensi

Didistribusikan di bawah lisensi MIT. Lihat [LICENSE](LICENSE).
