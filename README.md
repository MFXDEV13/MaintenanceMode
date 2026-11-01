# MaintenanceMode

[![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
![Build Success](https://img.shields.io/badge/Build-Success-brightgreen?logo=githubactions&logoColor=white)

Plugin Paper ringan untuk mengaktifkan mode maintenance server. Saat maintenance aktif, pemain tanpa izin bypass akan ditolak saat bergabung. Pemain yang sudah online juga dapat dikeluarkan secara otomatis.

## Fitur

- Aktifkan, nonaktifkan, periksa status, dan muat ulang konfigurasi melalui command.
- Simpan status maintenance ke `config.yml`, termasuk setelah server dimulai ulang.
- Atur apakah pemain online tanpa izin bypass dikeluarkan saat maintenance diaktifkan.
- Kelola whitelist maintenance tersendiri, yang tersimpan di `whitelist.yml`.
- Lihat jumlah pemain yang di-whitelist melalui command status.
- Ubah pesan plugin dan gunakan kode warna dengan format `&`.

## Persyaratan

- Paper **26.2** atau kompatibel dengan Paper API `26.2.build.129-stable`.
- Java **25**.

## Instalasi

1. Unduh `MaintenanceMode-1.1.0.jar` dari folder `target` atau buat JAR dari source.
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
| `/maintenance add <pemain>` | `/maint add <pemain>` | Tambahkan pemain yang pernah masuk server ke whitelist maintenance. |
| `/maintenance remove <pemain>` | `/maint remove <pemain>` | Hapus pemain dari whitelist maintenance. |
| `/maintenance list` | `/maint list` | Tampilkan pemain di whitelist maintenance. |

Semua sub-command membutuhkan izin `maintenance.admin`.

Pemain yang ditambahkan ke whitelist dapat bergabung selama maintenance dan tidak dikeluarkan saat maintenance diaktifkan. Whitelist disimpan terpisah dari permission `maintenance.bypass`. Pemain harus pernah masuk server agar dapat ditemukan untuk ditambahkan.

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
  unknown-command: "&cSub-command tidak dikenal."
  usage: "&7Gunakan: &f/maintenance <on|off|status|reload|add|remove|list>"
  reloaded: "&aKonfigurasi berhasil dimuat ulang."
  status: "&7Maintenance: %status%"
  whitelist-added: "&a%player% berhasil ditambahkan ke whitelist."
  whitelist-removed: "&a%player% berhasil dihapus dari whitelist."
  whitelist-already-added: "&e%player% sudah ada di whitelist."
  whitelist-not-found: "&c%player% tidak ditemukan di whitelist."
  whitelist-player-unknown: "&cPemain tidak ditemukan. Pemain harus pernah masuk server."
  whitelist-list-empty: "&eBelum ada pemain dalam whitelist."
  whitelist-list-header: "&6Daftar Maintenance Whitelist:"
  whitelist-list-entry: "&7- &f%player%"
```

- `maintenance.enabled`: status maintenance awal saat plugin dimulai.
- `maintenance.kick-existing-players`: keluarkan pemain online tanpa izin bypass saat maintenance diaktifkan.
- `messages.prefix`: awalan untuk pesan konfigurasi.
- Kode warna `&` didukung pada pesan yang dibaca dari konfigurasi.
- Data whitelist disimpan oleh plugin di `plugins/MaintenanceMode/whitelist.yml`.

## Build dari source

Pastikan Java 25 dan Maven tersedia, lalu jalankan dari direktori proyek:

```shell
mvn package
```

JAR akan dibuat di `target/MaintenanceMode-1.1.0.jar`.

## Lisensi

Didistribusikan di bawah lisensi MIT. Lihat [LICENSE](LICENSE).
