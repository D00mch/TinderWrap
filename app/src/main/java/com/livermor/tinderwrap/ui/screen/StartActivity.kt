package com.livermor.tinderwrap.ui.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.livermor.tinderwrap.data.AppDb
import com.livermor.tinderwrap.databinding.ActivityStartBinding
import com.livermor.tinderwrap.ui.screen.fix.FixActivity
import com.livermor.tinderwrap.ui.screen.swap.SwapActivity

class StartActivity : AppCompatActivity() {

    private val bindings by lazy { ActivityStartBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        setUpSwapScreenButton()
        setUpFixScreenButton()
    }

    private fun setUpFixScreenButton() = with(bindings) {
        bGoFix.setOnClickListener {
            startActivity(Intent(this@StartActivity, FixActivity::class.java))
        }
    }

    private fun setUpSwapScreenButton() = with(bindings) {
        if (AppDb.token.isNotBlank()) etToken.setText(AppDb.token)
        bGoSwipe.setOnClickListener {
            AppDb.token = etToken.text.toString()
            startActivity(Intent(this@StartActivity, SwapActivity::class.java))
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
