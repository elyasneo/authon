package ir.apptick.authsdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.apptick.authon.Authon
import ir.apptick.authon.AuthonCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Authon.start(this, R.id.container, R.drawable.firelogo, object : AuthonCallback {
            override fun success() {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }

            override fun failed() {

            }
        })
    }
}