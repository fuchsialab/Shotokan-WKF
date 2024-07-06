package com.fuchsia.shotokanwkf.activity



import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.fuchsia.shotokanwkf.R

@Suppress("DEPRECATION")
class NavBar : AppCompatActivity() {



    var home:LinearLayout? = null
    var share:LinearLayout? = null
    var policy:LinearLayout? = null
    var moreapp:LinearLayout? = null
    var rate:LinearLayout? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark


        home = findViewById(R.id.home)
        home?.setOnClickListener {

            finish()
            Animatoo.animateSwipeLeft(this);


        }

        share = findViewById(R.id.share)
        share?.setOnClickListener {

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody =
                "Download Shotokan Karate App and Learn.  https://play.google.com/store/apps/details?id=com.fuchsia.shotokanwkf"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shotokan WKF")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))

            finish()



        }
        policy = findViewById(R.id.policy)
        policy?.setOnClickListener {

            val browse = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.lkmkm)))
            startActivity(browse)

            finish()


        }
        moreapp = findViewById(R.id.moreapp)
        moreapp?.setOnClickListener {

            val browses = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/collection/cluster?clp=igM4ChkKEzUzNjIwODY3OTExNjgyNTA2MTkQCBgDEhkKEzUzNjIwODY3OTExNjgyNTA2MTkQCBgDGAA%3D:S:ANO1ljJMw2s&gsr=CjuKAzgKGQoTNTM2MjA4Njc5MTE2ODI1MDYxORAIGAMSGQoTNTM2MjA4Njc5MTE2ODI1MDYxORAIGAMYAA%3D%3D:S:ANO1ljI3U6g")
            )
            startActivity(browses)
            finish()



        }
        rate = findViewById(R.id.rate)
        rate?.setOnClickListener {

            val appPackageName = packageName
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }

            finish()


        }

        val arow = findViewById<ImageView>(R.id.arrow)
        arow.setOnClickListener {

            finish()
            Animatoo.animateSwipeLeft(this);


        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
        Animatoo.animateSwipeLeft(this);
    }
}