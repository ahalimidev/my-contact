package com.example.mycontact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontact.databinding.ActivityMainBinding
import com.siupindo.pringgo.adapter.KontakAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var sqlite : SQLiteHelperDB
    var mAdapter: KontakAdapter? = null
    var result = ArrayList<KontakTampil>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqlite = SQLiteHelperDB(this@MainActivity)
        binding.imgSync.setOnClickListener {
            val i = Intent(this@MainActivity, DetailActivity::class.java)
            startActivity(i)
        }
        binding.imgAddData.setOnClickListener {
            val i = Intent(this@MainActivity, AddContactActivity::class.java)
            startActivity(i)
        }

    }

    override fun onResume() {
        super.onResume()
        tampil_kontak()
    }
    private fun tampil_kontak (){
        val cursor = sqlite.tampil_semua_kontak()
        result.clear()
        if(cursor.count > 0) {
            cursor.moveToFirst()
            do{
                val kontak = KontakTampil()
                kontak.id = cursor.getInt(0)
                kontak.nama_kontak = cursor.getString(1)
                kontak.nomor_telepon = cursor.getString(2)
                kontak.email = cursor.getString(3)
                kontak.status_kontak = cursor.getString(4)
                kontak.sync = cursor.getInt(5)
                result.add(kontak)
            }while(cursor.moveToNext())

            val linearLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            linearLayoutManager.scrollToPositionWithOffset(0, 0)
            mAdapter = KontakAdapter(this@MainActivity,result)
            binding.rvListContact.setLayoutManager(linearLayoutManager)
            binding.rvListContact.setAdapter(mAdapter)
            mAdapter!!.notifyDataSetChanged()
        }else{
            Toast.makeText(this@MainActivity,"Tidak ada kontak",Toast.LENGTH_LONG).show()
        }
        cursor.close()
    }
}