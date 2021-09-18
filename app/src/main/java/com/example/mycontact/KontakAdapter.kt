package com.siupindo.pringgo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontact.DetailActivity
import com.example.mycontact.KontakTampil
import com.example.mycontact.R
import com.example.mycontact.databinding.ListItemBinding

class KontakAdapter(private val context: Context, results: ArrayList<KontakTampil>) : RecyclerView.Adapter<KontakAdapter.MyViewHolder>() {

    private var Items = ArrayList<KontakTampil>()

    init {
        this.Items = results

    }

    inner class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = Items[position]
        with(holder){
            binding.txNamaKontak.text = result.nama_kontak
            binding.txStatusHubungan.text = result.status_kontak

            if (result.sync == 0){
                binding.imgStatus.setBackgroundResource(R.drawable.ic_error)
            }else{
                binding.imgStatus.setBackgroundResource(R.drawable.ic_checklist)
            }

            if (result.status_kontak.equals("teman")){
                binding.txStatusHubungan.setBackgroundResource(R.drawable.gradient_blue)
            } else if (result.status_kontak.equals("keluarga")){
                binding.txStatusHubungan.setBackgroundResource(R.drawable.gradient_orange)
            } else if (result.status_kontak.equals("bisnis")){
                binding.txStatusHubungan.setBackgroundResource(R.drawable.gradient_green)
            }

            if(result.email != null){
                binding.txkontak.text = result.nomor_telepon+" "+result.email
            }else{
                binding.txkontak.text = result.nomor_telepon
            }
            holder.itemView.setOnClickListener {
                context.startActivity(Intent(context,DetailActivity::class.java).putExtra("id",result.id))
            }
        }
    }

    override fun getItemCount(): Int {
        return Items.size
    }
}