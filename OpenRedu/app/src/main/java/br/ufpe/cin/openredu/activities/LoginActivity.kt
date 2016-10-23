package br.ufpe.cin.openredu.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import br.ufpe.cin.openredu.ReduApplication

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginButton = Button(this)
        loginButton.text = "Login"
        loginButton.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        loginButton.setOnClickListener {
            val application = ReduApplication.getReduClient(this)

            val authorizeURL = application.authorizeUrl

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authorizeURL))
            startActivity(intent)
        }

        val contentLayout = LinearLayout(this)
        contentLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        contentLayout.addView(loginButton)

        setContentView(contentLayout)
    }

    override fun onResume() {
        super.onResume()

        val URI = intent.data

        if (URI != null) {
            val accessToken = URI.getQueryParameter("access_token")

        }
    }
}
