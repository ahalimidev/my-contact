package com.example.mycontact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycontact.databinding.ActivityDetailBinding
import android.content.Intent

import android.net.Uri
import androidx.appcompat.app.AlertDialog


class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private lateinit var sqlite : SQLiteHelperDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqlite = SQLiteHelperDB(this@DetailActivity)
        binding.imgBack.setOnClickListener { this@DetailActivity.finish() }

    }

    override fun onResume() {
        super.onResume()
        tampil_detail()
    }
    fun tampil_detail(){
        val cursor = sqlite.tampil_satu_kontak(intent.getIntExtra("id",0))
        if(cursor.count > 0) {
            cursor.moveToFirst()
            binding.txNamaKontak.text = cursor.getString(1)
            binding.txStatusHubungan.text = cursor.getString(4)
            binding.llPesan.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("smsto:" + Uri.encode(cursor.getString(2)))
                startActivity(Intent.createChooser(intent, "Send SMS Via"))
            }
            binding.llTelepon.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:")
                startActivity(Intent.createChooser(intent, "Call Telepon Via "))
            }
            binding.llWa.setOnClickListener {
                val uri = Uri.parse("smsto:" + Uri.encode(cursor.getString(2)))
                val i = Intent(Intent.ACTION_SENDTO, uri)
                i.setPackage("com.whatsapp")
                startActivity(Intent.createChooser(i, ""))
            }

            if(cursor.getString(3) != null){
                binding.llGmail.setOnClickListener {
                    val emailApp = Intent(Intent.ACTION_SEND)
                    emailApp.putExtra(Intent.EXTRA_EMAIL, cursor.getString(3))
                    startActivity(Intent.createChooser(emailApp, "Send Email Via"))
                }
            }
            binding.imgdelete.setOnClickListener {
                val alert = AlertDialog.Builder(this@DetailActivity)
                alert.setTitle("Hapus Kontak")
                alert.setMessage("Apakah kamu yakin hapus kontak ini?")
                alert.setPositiveButton("Ya") { dialogInterface, i ->
                    if( sqlite.hapus_kontak(cursor.getInt(0))){
                        finish()
                    }

                }
                alert.setNegativeButton("Tidak") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                alert.create().show()
            }
            binding.imgedit.setOnClickListener {
                startActivity(Intent(this@DetailActivity,EditContactAcitivity::class.java).putExtra("id",cursor.getInt(0)))
            }

        }else{
            finish()
        }
    }
}