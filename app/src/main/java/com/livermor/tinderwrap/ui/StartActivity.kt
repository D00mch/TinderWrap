package com.livermor.tinderwrap.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.livermor.tinderwrap.data.AppDb
import com.livermor.tinderwrap.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityStartBinding.inflate(layoutInflater)) {
            setContentView(root)
            if (AppDb.token.isNotBlank()) {
                editTextTextPassword.setText(AppDb.token)
            }
            bGo.setOnClickListener {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
        }
    }
}