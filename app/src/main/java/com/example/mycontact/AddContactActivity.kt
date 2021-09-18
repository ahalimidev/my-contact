package com.example.mycontact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycontact.databinding.ActivityAddContactBinding

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddContactBinding
    private lateinit var sqlite : SQLiteHelperDB
    var status_hubungan = "teman"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqlite = SQLiteHelperDB(this@AddContactActivity)

        binding.imgBack.setOnClickListener {
            this@AddContactActivity.finish()
        }

        binding.llSimpanKontak.setOnClickListener {
            if(binding.etNamaKontak.text.toString().isEmpty()){
                Toast.makeText(this@AddContactActivity,"Nama Kontak Kosong",Toast.LENGTH_LONG).show()
            }else if(status_hubungan == null){
                Toast.makeText(this@AddContactActivity,"Pilih Status Hubungan",Toast.LENGTH_LONG).show()
            }else{
                val simpan = sqlite.tambah_kontak(
                    Kontak(
                        binding.etNamaKontak.text.toString(),
                        binding.etNomorTelepon.text.toString(),
                        binding.etEmail.text.toString(),
                        status_hubungan
                    )
                )
                if (simpan){
                    Toast.makeText(this@AddContactActivity,"Berhasil Tambah Kontak",Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this@AddContactActivity,"Gagal Tambah Kontak",Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.rgStatusHubungan.setOnCheckedChangeListener { radioGroup, i ->
            if(i == binding.rbBisnis.id){
                status_hubungan = "bisnis"
            }else if(i == binding.rbKeluarga.id){
                status_hubungan = "keluarga"
            }else if(i == binding.rbTemen.id){
                status_hubungan = "teman"
            }
        }
    }
}