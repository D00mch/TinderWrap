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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val compositeAdapter = CompositeDelegateAdapter(PhotoAdapter(), BioAdapter())

    private val viewModel: MainViewModel by lazy {
        val factory = MainViewModel.Factory(applicationContext)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)

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
        observe(viewModel)
    }

    private fun observe(model: MainViewModel) {
        model.feed.observe(this, Observer { compositeAdapter.swapData(it) })
        model.errors.observe(this, Observer { toast(it.toString()) })
        model.noMoreAccounts.observe(this, Observer { toast(it.toString()) })
        model.age.observe(this, Observer { tvBirth.text = it.toString() })
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}

