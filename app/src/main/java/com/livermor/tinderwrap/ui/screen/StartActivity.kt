package com.livermor.tinderwrap.ui.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.livermor.tinderwrap.data.AppDb
import com.livermor.tinderwrap.databinding.ActivityStartBinding
import com.livermor.tinderwrap.ui.screen.swap.SwapActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityStartBinding.inflate(layoutInflater)) {
            setContentView(root)
            if (AppDb.token.isNotBlank()) {
                etToken.setText(AppDb.token)
            }
            bGo.setOnClickListener {
                AppDb.token = etToken.text.toString()
                startActivity(Intent(this@StartActivity, SwapActivity::class.java))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(StartActivity::class.java.simpleName, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(StartActivity::class.java.simpleName, "onStop: ")
    }
}
