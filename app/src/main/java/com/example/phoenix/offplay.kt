package com.example.phoenix

import android.content.Intent
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_offplay.*

class offplay : AppCompatActivity() {
    private lateinit var layout: LinearLayout

    private lateinit var runnable: Runnable
    private var handler = Handler()
    var min:Int=0
    var sec:Int=0
    var dur:Int=0
    var sdur:String=""
    //v
    var cmin:Int=0
    var csec:Int=0
    var cdur:Int=0
    var csdur:String=""
    var tgem :Int=0
    var tap:Int =0
    companion object{
        var ffisplay:Int=0
        var songPosition :Int=0
        var mediaPlayer: MediaPlayer? =null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offplay)
        songPosition = intent.getIntExtra("index",0)


        when(intent.getStringExtra("class"))
        {
            "MusicAdapter"->{

                play()
            }
        }
        offplay.setOnClickListener {
            if(ffisplay==0)
                play()

        }
        offpaus.setOnClickListener {
            if(ffisplay==1) {
                mediaPlayer!!.pause()
                ffisplay=0
            }
        }
        layout = findViewById(R.id.thispla)
        layout.setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                Toast.makeText(this@offplay, "Swipe Left gesture detected",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                Toast.makeText(
                    this@offplay,
                    "Swipe Right gesture detected",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onSwipeUp() {
                super.onSwipeUp()
next()


            }
            override fun onSwipeDown() {
                super.onSwipeDown()

pre()
            }
        })

        unlike.setOnClickListener {
            tap++

            val hal=Handler()
            hal.postDelayed({
                if(tap==2)
                {
                    ilike.visibility= View.VISIBLE
                    unlike.visibility=View.GONE
                    Toast.makeText(this@offplay, "add to favorites",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                tap=0
            },500)
        }
        offbteq.setOnClickListener {
            if(ffisplay==1)
                eqlis()
            else
                Toast.makeText(this,"first play the audio" , Toast.LENGTH_LONG).show()
        }

        offbtlog.setOnClickListener {
            startActivity(Intent(this@offplay, MainActivity::class.java))
        }
    }
    private fun play(){
        ffisplay=1
        if(player.isplay==1)
            player.mediaPlayer!!.pause()
        offseek.progress=0
        offseek.max=player.MusicListMA[songPosition].duration.toInt()

        if(mediaPlayer ==null)
            mediaPlayer =MediaPlayer()
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(player.MusicListMA[songPosition].path)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        dur=player.MusicListMA[songPosition].duration.toInt()/1000
        min=dur/60
        sec=dur%60
        sdur="$min:$sec"
        offshowdur.text=sdur
        Glide
            .with(this@offplay)
            .load(player.MusicListMA[songPosition]
                .artUri).apply(
                RequestOptions()
                    .placeholder(R.drawable.bg))
            .centerCrop()
            .into(offimage)
        runnable= Runnable {
            offseek.progress= mediaPlayer!!.currentPosition
            handler.postDelayed(runnable,1000)
            cdur= mediaPlayer!!.currentPosition/1000
            cmin=cdur/60
            csec=cdur%60

            csdur="$cmin:$csec"
            offcurdur.text=csdur

        }
        handler.postDelayed(runnable,1000)
        mediaPlayer!!.setOnCompletionListener {
            next()
        }

        offalbum.text=player.MusicListMA[songPosition].title
        offart.text=player.MusicListMA[songPosition].album
        ilike.visibility= View.GONE
        unlike.visibility= View.VISIBLE

    }
    private fun next(){
        if(mediaPlayer!!.isPlaying)
            mediaPlayer!!.pause()
        songPosition= songPosition+1
        play()
    }
    private fun pre(){
        if(mediaPlayer!!.isPlaying)
            mediaPlayer!!.pause()
        songPosition= songPosition-1
        play()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==13||resultCode== RESULT_OK)
            return
    }
    private fun eqlis() {
        val eqintent= Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        eqintent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,mediaPlayer!!.audioSessionId)
        eqintent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME,baseContext.packageName)
        eqintent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
        startActivityForResult( eqintent,13)

    }
}