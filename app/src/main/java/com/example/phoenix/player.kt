package com.example.phoenix

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_player.*
import java.io.File
import java.io.IOException

class player : AppCompatActivity() {
    //global

    var songPosition :Int=0
    companion object{
        lateinit var MusicListMA:ArrayList<Music>
        lateinit var musicListpa:ArrayList<Music>

        var mediaPlayer: MediaPlayer? = null
        var isplay :Int=0
    }





    private lateinit var runnable: Runnable
    private var handler= Handler()

    private lateinit var musicAdapter: musicAdapter
    var i=0
    var ispo=0
    var min:Int =0
    var cdura:Int=0
    var cmin:Int=0
    var csec:Int=0
    var dura:Int=0
    var dur:String =""
    var cdur:String=""
    var net:Int=1
    var iss:Int=2

    var sec:Int=0




    var song=mutableListOf<String>(
        "https://alpha080819.000webhostapp.com/lp/Faint.mp3",
        "https://alpha080819.000webhostapp.com/lp/BleedIt.mp3",
        "https://alpha080819.000webhostapp.com/lp/Habit.mp3",
        "https://alpha080819.000webhostapp.com/lp/Ite.mp3",
        "https://alpha080819.000webhostapp.com/lp/Numb.mp3",
        "https://alpha080819.000webhostapp.com/lp/Whatihave.mp3"
    )
    private lateinit var layout: LinearLayout

    //local
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        musicrv.setHasFixedSize(true)
        musicrv.setItemViewCacheSize(13)
        var musiclistmv=getAllAudio()
        if(checkForInternet(this))
        {

            net=0
        }
        else
        {
            Toast.makeText(this,"please check your internet connection",Toast.LENGTH_SHORT).show()
            net=1
        }
        MusicListMA=getAllAudio()
        musicListpa=getAllAudio()
        musicrv.layoutManager= LinearLayoutManager(this)
        musicAdapter=musicAdapter(this@player, musiclistmv)
        musicrv.adapter=musicAdapter
        totalno.text="total Songs :"+ musicAdapter.itemCount
        val ttb= AnimationUtils.loadAnimation(this,R.anim.ttb)
        val unttb= AnimationUtils.loadAnimation(this,R.anim.unttb)
        val stb= AnimationUtils.loadAnimation(this,R.anim.stb)
        val unstb= AnimationUtils.loadAnimation(this,R.anim.unstb)
        val btt= AnimationUtils.loadAnimation(this,R.anim.btt)
        val unbtt= AnimationUtils.loadAnimation(this,R.anim.unbtt)
        val fad= AnimationUtils.loadAnimation(this,R.anim.fadin)
        var fadout= AnimationUtils.loadAnimation(this,R.anim.fadout)
        onlayout.startAnimation(btt)
        play.setOnClickListener {

            if(isplay==0) {
                playAudio()
            }
        }
        paus.setOnClickListener {
            if(isplay==1)
            {
                mediaPlayer!!.stop()
                isplay=0
                paus.visibility= View.GONE
                play.visibility=View.VISIBLE
            }
        }
        layout = findViewById(R.id.lplayer)
        layout.setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeLeft() {
                super.onSwipeLeft()

                if(iss<=4&&iss>=1)
                    iss=iss+1
                when(iss)
                {
                    1->{ishome()
                        homelayout.startAnimation(ttb)}
                    2->{isonline()
                        onlayout.startAnimation(ttb)}
                    3->{islib()
                        offlayout.startAnimation(ttb)}
                    4->{isser()
                        serlayout.startAnimation(ttb)}
                    else->{Toast.makeText(this@player,"there is no slides",Toast.LENGTH_SHORT).show()
                        iss=4
                    }
                }
            }
            override fun onSwipeRight() {
                super.onSwipeRight()

                if(iss>=1&&iss<=4)
                    iss=iss-1
                when(iss)
                {
                    1->{ishome()
                        homelayout.startAnimation(ttb)}
                    2->{isonline()
                        onlayout.startAnimation(ttb)}
                    3->{islib()
                        offlayout.startAnimation(ttb)}
                    4->{isser()
                        serlayout.startAnimation(ttb)}
                    else->{Toast.makeText(this@player,"there is no slides",Toast.LENGTH_SHORT).show()
                        iss=1
                    }
                }
            }
            override fun onSwipeUp() {
                super.onSwipeUp()
                if(iss==2)
                    next()
            }
            override fun onSwipeDown() {
                super.onSwipeDown()
                if(iss==2)
                    previ()
            }

        })

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {

                    Companion.mediaPlayer!!.seekTo(p1)

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        btlog.setOnClickListener {
            startActivity(Intent(this@player, MainActivity::class.java))
        }
        bteq.setOnClickListener {
            if(isplay==1)
                eqlis()
            else
                Toast.makeText(this,"first play the audio" , Toast.LENGTH_LONG).show()
        }
        change.setOnClickListener {
            ispo++
            when(ispo)
            {
                1-> avator.setImageResource(R.drawable.av1)
                2-> avator.setImageResource(R.drawable.av2)
                3-> avator.setImageResource(R.drawable.av3)
                4-> avator.setImageResource(R.drawable.av4)
                else->ispo=0
            }
        }
    }
    // for functions
    private fun playAudio()
    {
        if(checkForInternet(this))
        {

            net=0
        }
        else
        {
            Toast.makeText(this,"please check your internet connection",Toast.LENGTH_SHORT).show()
            net=1
        }

        if(net==0)
        {
           if(offplay.ffisplay==1)
               offplay.mediaPlayer!!.pause()
            var audiourl =  song[i]
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer!!.setDataSource(audiourl)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
                isplay=1
                seek.progress=0
                seek.max=mediaPlayer!!.duration
                dura=mediaPlayer!!.duration/1000
                min=dura/60
                sec=dura%60
                dur="$min:$sec"
                showdur.text=dur
                play.visibility= View.GONE
                paus.visibility= View.VISIBLE

            } catch (e: IOException) {
                e.printStackTrace()
            }
            runnable= Runnable {
                seek.progress=mediaPlayer!!.currentPosition
                cdura=mediaPlayer!!.currentPosition/1000
                cmin=cdura/60
                csec=cdura%60
                cdur="$cmin:$csec"
                curdur.text=cdur
                if(checkForInternet(this))
                {

                    net=0
                }
                else
                {if(iss==2)
                    Toast.makeText(this,"please check your internet connection",Toast.LENGTH_SHORT).show()
                    net=1
                    mediaPlayer!!.pause()
                }
                handler.postDelayed(runnable,1000)

            }
            handler.postDelayed(runnable,1000)
            mediaPlayer!!.setOnCompletionListener {
                if(isplay==1)
                {
                    next()
                }
            }

        }


    }
    private fun pauseAudio()
    {
        if(isplay==1)
        {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            isplay=0
            paus.visibility= View.GONE
            play.visibility=View.VISIBLE
        }

    }
    private fun previ()
    {
        if(isplay==1) {
            pauseAudio()
        }
        i--
        if(i<0) {
            i = 5
        }
        playAudio()
    }
    private fun next()
    {
        if(isplay==1) {
            pauseAudio()
        }
        i++
        if(i>5) {
            i = 0
        }
        playAudio()
    }
    private fun isser()
    {

        isser.setBackgroundColor(ContextCompat.getColor(this,R.color.lblue))
        islib.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        isonline.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        ishome.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        serlayout.visibility=View.VISIBLE
        offlayout.visibility= View.GONE
        onlayout.visibility=View.GONE
        homelayout.visibility=View.GONE
    }
    private fun islib()
    {
        islib.setBackgroundColor(ContextCompat.getColor(this,R.color.lblue))
        isonline.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        ishome.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        isser.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        offlayout.visibility= View.VISIBLE
        serlayout.visibility=View.GONE
        onlayout.visibility=View.GONE
        homelayout.visibility=View.GONE
    }
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    private fun isonline()
    {
        isonline.setBackgroundColor(ContextCompat.getColor(this,R.color.lblue))
        ishome.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        islib.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        isser.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        onlayout.visibility=View.VISIBLE
        offlayout.visibility= View.GONE
        serlayout.visibility=View.GONE
        homelayout.visibility=View.GONE
    }
    private fun ishome()
    {
        ishome.setBackgroundColor(ContextCompat.getColor(this,R.color.lblue))
        isonline.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        islib.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        isser.setBackgroundColor(ContextCompat.getColor(this,R.color.dblue))
        homelayout.visibility=View.VISIBLE
        onlayout.visibility=View.GONE
        offlayout.visibility= View.GONE
        serlayout.visibility=View.GONE
    }
    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getAllAudio(): ArrayList<Music>{
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC +  " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)

        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,selection,null,
            MediaStore.Audio.Media.ALBUM + " ASC", null)

        if(cursor != null){
            if(cursor.moveToFirst())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))?:"Unknown"
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))?:"Unknown"
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))?:"Unknown"
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))?:"Unknown"
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)).toString()
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumidc=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri= Uri.parse("content://media/external/audio/albumart")
                    val artUri= Uri.withAppendedPath(uri,albumidc).toString()

                    val music = Music(id = idC, title = titleC, album = albumC, artist = artistC, path = pathC, duration = durationC,
                        artUri = artUri)
                    val file = File(music.path)
                    if(file.exists())
                        tempList.add(music)
                }while (cursor.moveToNext())
            cursor.close()
        }
        return tempList
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