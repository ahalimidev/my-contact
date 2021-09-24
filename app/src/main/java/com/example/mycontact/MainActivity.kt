package com.example.mycontact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontact.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.siupindo.pringgo.adapter.KontakAdapter
import com.androidnetworking.error.ANError

import org.json.JSONObject

import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority


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
            upload()
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
            AndroidNetworking.get("http://192.168.100.8/kontak/download.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        for (x in 0 until response.getJSONArray("data").length()) {
                            sqlite.tambah_kontak(
                                Kontak(
                                    response.getString("nama_kontak")[x].toString(),
                                    response.getString("nomor_telepon")[x].toString(),
                                    response.getString("email")[x].toString(),
                                    response.getString("status_kontak")[x].toString(),
                                )
                            )

                        }
                        tampil_kontak()
                    }
                    override fun onError(error: ANError) {
                        Log.e("ERROR APLIKASI", error.toString())
                    }
                })
        }
        cursor.close()
    }

    private fun upload(){
        val cursor = sqlite.tampil_semua_kontak()
        if(cursor.count > 0){
            cursor.moveToFirst()
            for (x in 0 until cursor.count) {
                if(cursor.getInt(5) == 0){
                    sqlite.update_sync(cursor.getInt(0))
                    AndroidNetworking.post("http://192.168.100.8/kontak/upload.php")
                        .addBodyParameter("nama_kontak", cursor.getString(1))
                        .addBodyParameter("nomor_telepon", cursor.getString(2))
                        .addBodyParameter("email", cursor.getString(3))
                            .addBodyParameter("status_kontak", cursor.getString(4))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                            }
                            override fun onError(error: ANError) {
                               Log.e("ERROR APLIKASI", error.toString())
                            }
                        })


                }
                cursor.moveToNext()
            }
            tampil_kontak()
        }
    }
}