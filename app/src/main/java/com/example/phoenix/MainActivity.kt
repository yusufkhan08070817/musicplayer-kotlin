package com.example.phoenix

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRunTimePermission()
        setContentView(R.layout.activity_main)

        //ani
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        val unttb = AnimationUtils.loadAnimation(this, R.anim.unttb)
        val stb = AnimationUtils.loadAnimation(this, R.anim.stb)
        val unstb = AnimationUtils.loadAnimation(this, R.anim.unstb)
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        val unbtt = AnimationUtils.loadAnimation(this, R.anim.unbtt)
        val fad = AnimationUtils.loadAnimation(this, R.anim.fadin)
        var fadout = AnimationUtils.loadAnimation(this, R.anim.fadout)
        //set anim
        layuser.startAnimation(ttb)
        laypass.startAnimation(ttb)
        idont.startAnimation(ttb)
        logo.startAnimation(fad)
        blogin.startAnimation(btt)


        idont.setOnClickListener {
            layuser.startAnimation(unttb)
            laypass.startAnimation(unttb)
            idont.startAnimation(unttb)
            logo.startAnimation(unstb)
            blogin.startAnimation(unbtt)
            layoutsignin.visibility = View.VISIBLE
            layoutlogin.visibility = View.GONE
            slayuser.startAnimation(ttb)
            slaypass.startAnimation(ttb)
            bsignup.startAnimation(btt)
            logo.startAnimation(fad)

        }
        layoutlogin.setOnClickListener {
            startActivity(Intent(this@MainActivity, player::class.java))
        }

    }

    private fun requestRunTimePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.INTERNET
                ),
                13
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show()

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.INTERNET
                    ),
                    13
                )
            }
        }
    }
}