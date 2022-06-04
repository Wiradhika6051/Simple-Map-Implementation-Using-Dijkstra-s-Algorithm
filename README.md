# Simple Map Implementation Using Dijkstra's Algorithm
Sebuah program simulasi map dengan algoritma Djikstra

# Identitas Author
- Nama: Fawwaz Anugrah Wiradhika Dharmasatya
- NIM:13520086
- Jurusan:Teknik Informatika

# Daftar Isi
1. [Deskripsi Singkat](#deskripsi-singkat)
2. [Requirement dan Instalasi](#requirement-dan-instalasi)
3. [Cara Penggunaan](#cara-penggunaan)
   - [Troubleshooting](#troubleshooting)

# Deskripsi Singkat
Program simulasi peta menggunakan algoritma Djikstra ini merupakan sebuah program yang menyimulasikan fungsionalitas pencarian jarak terdekat pada aplikasi Map. Pengguna bisa memasukkan peta, memilih titik mana yang ingin dicari jarak terdekatnya, serta bobot langkah-per-langkah tiap simpul saat algoritma ini bekerja. Program ini sata beri nama Pathfinder D-2000 karena seperti namanya berguna untuk mencari jalan (*pathfinding*). D di namanya singkatan dari Djikstra, yakni nama algoritma yang digunakan sekaligus nama penemunya. Angka 2000 hanyalah tambahan saja supaya terdengar keren saja 😎😎.

# Requirement dan Instalasi
- Pastikan JRE sudah terinstall agar bisa menjalankan program ini. <br> Link instalasi: https://docs.oracle.com/goldengate/1212/gg-winux/GDRAD/java.htm#BGBFHBEA

# Cara Penggunaan
1. Buka folder `bin`
2. Jalankan `Pathfinder D-2000.jar`
3. Tekan tombol `PILIH FILE` lalu pilih file txt yang ingin dimasukkan. Ketentuan file txt:
   - Komentar ditulis dengan diawali karakter `#`. Karakter ini harus menjadi karakter pertama pada baris yang menjadi komentar
   - Nama Simpul tidak boleh diawali `#` karena alasan di atas serta tidak boleh memiliki spasi. Bobot sisi boleh berupa pecahan
   - Format isi file:
   JUMLAH_SIMPUL
   <br>
   SIMPUL_A
   <br>
   ...<br>SIMPUL_B<br>JUMLAH_SISI<br>SIMPUL_A SIMPUL_B 10<br>SIMPUL_B SIMPUL_C (bobot sisi)<br>...<br>SIMPUL_C SIMPUL_D 40.5<br>
4. Pilih simpul yang ingin dijadikan simpul asal dengan menekannya sampai warnanya menjadi biru. Jika tidak jadi memilihnya, tekan simpul itu sekali lagi hingga warnanya kembali menjadi abu-abu.
5. Pilih simpul yang ingin dijadikan simpul tujuan dengan menekannya sampai warnanya menjadi hijau. Jika tidak jadi memilihnya, tekan simpul itu sekali lagi hingga warnanya kembali menjadi abu-abu.
6. Tekan tombol `RUN` untuk menjalankan algoritma
7. Hasil akhir algoritma berupa path terpendek akan ditambilkan. Jalur yang dilewati akan diwarnai dengan warna kuning. Ditampilkan juga waktu eksekusi dan jumlah iterasi.
8. Jika ingin mengulangi algoritma di peta yang sama namun simpulnya berbeda,tekan tombol `RESTART`. Lalu ulangi mulai langkah ke-4.
9. Jika ingin melihat bobot tiap simpul langkah-per-langkah, pilih `MODE STEP ALGORITMA`. Lalu tekan `NEXT STEP` atau `PREV STEP` untuk bergeser ke iterasi sesudah atau sebelumnya. Simpul yang sedang diperiksa ditandai dengan warna merah.
10. Jika ingin kembali ke mode melihat lintasan terpendek, tekan `MODE HASIL`
 ## Troubleshooting

