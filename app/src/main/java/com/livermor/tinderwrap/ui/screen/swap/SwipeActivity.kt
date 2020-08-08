package com.livermor.tinderwrap.ui.screen.swap

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import com.livermor.tinderwrap.databinding.ActivitySwipeBinding
import com.livermor.tinderwrap.factory.ViewModelsFactory
import com.livermor.tinderwrap.ui.adapter.BioAdapter
import com.livermor.tinderwrap.ui.adapter.PhotoAdapter
import com.livermor.tinderwrap.ui.adapter.SwipeCallback
import com.livermor.tinderwrap.ui.screen.SwapMessage
import kotlinx.android.synthetic.main.activity_swipe.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

private const val TAG = "SwapActivity"

class SwapActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySwipeBinding.inflate(layoutInflater) }
    private val compositeAdapter = CompositeDelegateAdapter(PhotoAdapter(), BioAdapter())

    private val viewModel: SwipeViewModel by lazy { ViewModelsFactory.swapModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)

            rvPhotos.run {
                layoutManager = LinearLayoutManager(this@SwapActivity)
                adapter = compositeAdapter
                val swipeCallback = SwipeCallback(viewModel::update)
                val itemTouchHelper = ItemTouchHelper(swipeCallback)
                itemTouchHelper.attachToRecyclerView(this)
                OverScrollDecoratorHelper.setUpOverScroll(this, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
            }
            bNo.setOnClickListener { viewModel.update(SwapMessage.Choose(like = false)) }
            bYes.setOnClickListener { viewModel.update(SwapMessage.Choose(like = true)) }
        }
        observe(viewModel)
    }

    private fun observe(model: SwipeViewModel) {
        model.feed.observe(this, Observer { compositeAdapter.swapData(it) })
        model.errors.observe(this, Observer {
            Log.e(TAG, "observe: error $it")
            toast(it.toString())
        })
        model.noMoreAccounts.observe(this, Observer { toast(it.toString()) })
        model.age.observe(this, Observer { tvBirth.text = it.toString() })
        model.progress.observe(this, Observer { pbLoading.isVisible = it })
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}

