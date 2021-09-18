package com.example.mycontact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycontact.databinding.ActivityEditContactBinding

class EditContactAcitivity : AppCompatActivity() {
    private lateinit var sqlite : SQLiteHelperDB
    private lateinit var binding : ActivityEditContactBinding
    var status_hubungan = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqlite = SQLiteHelperDB(this@EditContactAcitivity)
        binding.rgStatusHubungan.setOnCheckedChangeListener { radioGroup, i ->
            if(i == binding.rbBisnis.id){
                status_hubungan = "bisnis"
            }else if(i == binding.rbKeluarga.id){
                status_hubungan = "keluarga"
            }else if(i == binding.rbTemen.id){
                status_hubungan = "teman"
            }
        }
        tampil_detail()
    }
    fun tampil_detail() {
        val cursor = sqlite.tampil_satu_kontak(intent.getIntExtra("id", 0))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            binding.etNamaKontak.setText(cursor.getString(1))
            binding.etNomorTelepon.setText(cursor.getString(2))
            binding.etEmail.setText(cursor.getString(3))
            if(cursor.getString(4) == "bisnis"){
                binding.rbBisnis.isChecked = true
                status_hubungan = "bisnis"
            }else if(cursor.getString(4) == "keluarga"){
                binding.rbKeluarga.isChecked = true
                status_hubungan = "keluarga"

            }else if(cursor.getString(4) == "teman"){
                binding.rbTemen.isChecked = true
                status_hubungan = "teman"

            }
            binding.llPembaruanKontak.setOnClickListener {
                val simpan = sqlite.edit_kontak(
                    Kontak(
                        binding.etNamaKontak.text.toString(),
                        binding.etNomorTelepon.text.toString(),
                        binding.etEmail.text.toString(),
                        status_hubungan
                    ),
                    intent.getIntExtra("id", 0)
                )
                if (simpan){
                    Toast.makeText(this@EditContactAcitivity,"Berhasil Pembaruan Kontak", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this@EditContactAcitivity,"Gagal Pembaruan Kontak", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            finish()
        }
    }
}