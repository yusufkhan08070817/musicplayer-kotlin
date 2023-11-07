package com.example.phoenix

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.phoenix.databinding.ActivityMainBinding
import com.example.phoenix.databinding.MusicViewBinding

class musicAdapter (private val context: Context,private val musiclist:ArrayList<Music>):RecyclerView.Adapter<musicAdapter.myholder>() {
    class myholder (binding: MusicViewBinding):RecyclerView.ViewHolder(binding.root){
val title=binding.songnamemv

        var album=binding.album
        val image=binding.imagemv
        var duration=binding.songdur
        val rot=binding.root

    }

    var min:Int=0
    var sec:Int=0
    var dur:Int=0
     var sdur:String=""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myholder {
        return myholder(MusicViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }


    override fun onBindViewHolder(holder: myholder, position: Int) {
        holder.title.text=musiclist[position].title
        holder.album.text=musiclist[position].album
        dur=musiclist[position].duration.toInt()/1000
        min=dur/60
        sec=dur%60
        sdur="$min:$sec"
        holder.duration.text=sdur
var data:String

        Glide
            .with(context)
            .load(musiclist[position]
                .artUri).apply(RequestOptions()
                .placeholder(R.drawable.bg))
            .centerCrop()
            .into(holder.image)

        holder.rot.setOnClickListener {

val intent=Intent(context,offplay::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","MusicAdapter")

            data=musiclist[position].path
            intent.putExtra("ok",data)
            ContextCompat.startActivity(context,intent,null)

        }


    }

    override fun getItemCount(): Int {
       return musiclist.size
    }
}