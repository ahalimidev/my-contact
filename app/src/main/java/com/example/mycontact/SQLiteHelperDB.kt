package com.example.mycontact

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelperDB(context: Context) : SQLiteOpenHelper(context,"contact.db",null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val kontak = "CREATE TABLE kontak (id_kontak INTEGER PRIMARY KEY AUTOINCREMENT, nama_kontak TEXT, nomor_telepon TEXT, email TEXT, status_kontak TEXT, snyc INTEGER NOT NULL DEFAULT '0')"
        db?.execSQL(kontak)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS kontak")
    }

    fun tambah_kontak(kontak: Kontak): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("nama_kontak", kontak.nama_kontak)
        values.put("nomor_telepon", kontak.nomor_telepon)
        values.put("email", kontak.email)
        values.put("status_kontak", kontak.status_kontak)
        val _success = db.insert("kontak", null, values)
        return (("$_success").toInt() != -1)
    }

    fun edit_kontak(kontak: Kontak, id : Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("nama_kontak", kontak.nama_kontak)
        values.put("nomor_telepon", kontak.nomor_telepon)
        values.put("email", kontak.email)
        values.put("status_kontak", kontak.status_kontak)
        val _success = db.update("kontak", values, "id_kontak = $id",null )
        return (("$_success").toInt() != -1)
    }

    fun tampil_semua_kontak() : Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM kontak ", null)
    }

    fun tampil_satu_kontak(id : Int) : Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM kontak WHERE id_kontak = $id ", null)
    }

    fun hapus_kontak(id : Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete("kontak","id_kontak = $id",null)
        return (("$_success").toInt() != -1)
    }

    fun update_sync(id : Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("snyc", 1)
        val _success = db.update("kontak", values, "id_kontak = $id",null )
        return (("$_success").toInt() != -1)
    }

    fun download_kontak(kontak: Kontak): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("nama_kontak", kontak.nama_kontak)
        values.put("nomor_telepon", kontak.nomor_telepon)
        values.put("email", kontak.email)
        values.put("status_kontak", kontak.status_kontak)
        values.put("snyc", 1)
        val _success = db.insert("kontak", null, values)
        return (("$_success").toInt() != -1)
    }
}