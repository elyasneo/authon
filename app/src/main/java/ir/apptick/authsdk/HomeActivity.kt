package ir.apptick.authsdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.apptick.authon.Authon
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        logout.setOnClickListener {
            Authon.logout {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        get_access_token.setOnClickListener {
            Authon.getAccessToken({
                tv_res.text = it
            })
        }
    }
}