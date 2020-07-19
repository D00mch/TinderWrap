package com.livermor.tinderwrap.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import com.livermor.tinderwrap.databinding.ActivityMainBinding
import com.livermor.tinderwrap.ui.adapter.BioAdapter
import com.livermor.tinderwrap.ui.adapter.PhotoAdapter
import com.livermor.tinderwrap.ui.adapter.SwipeCallback

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val compositeAdapter = CompositeDelegateAdapter(PhotoAdapter(), BioAdapter())

    private val viewModel by lazy {
        val factory = MainViewModel.Factory(applicationContext)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            rvPhotos.run {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = compositeAdapter
                val swipeCallback = SwipeCallback(viewModel::update)
                val itemTouchHelper = ItemTouchHelper(swipeCallback)
                itemTouchHelper.attachToRecyclerView(this)
            }
            bNo.setOnClickListener { viewModel.update(Message.Choose(like = false)) }
            bYes.setOnClickListener { viewModel.update(Message.Choose(like = true)) }
        }


        viewModel.feed.observe(this, Observer { compositeAdapter.swapData(it) })
        viewModel.errors.observe(this, Observer { Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show() })
    }
}

