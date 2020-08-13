package com.livermor.tinderwrap.ui.screen.fix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import com.livermor.tinderwrap.databinding.ActivityFixBinding
import com.livermor.tinderwrap.factory.ViewModelsFactory
import com.livermor.tinderwrap.ui.adapter.PhotoAdapter
import com.livermor.tinderwrap.ui.adapter.SwipeCallback
import com.livermor.tinderwrap.ui.screen.FixingType
import com.livermor.tinderwrap.ui.screen.SwapMessage

class FixActivity : AppCompatActivity() {

    private val bindings by lazy { ActivityFixBinding.inflate(layoutInflater) }
    private val compositeAdapter = CompositeDelegateAdapter(PhotoAdapter())
    private val type by lazy { intent.getSerializableExtra(FIXING_TYPE_EXTRA) as FixingType }
    private val viewModel: FixViewModel by lazy { ViewModelsFactory.fixModel(this, type) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        Log.i(FixActivity::class.java.simpleName, "onCreate")
        bind()
        observe(viewModel)
    }

    private fun bind() {
        with(bindings) {
            rvPhotos.run {
                layoutManager = LinearLayoutManager(this@FixActivity)
                adapter = compositeAdapter
                val swipeCallback = SwipeCallback(viewModel::update)
                val itemTouchHelper = ItemTouchHelper(swipeCallback)
                itemTouchHelper.attachToRecyclerView(this)
            }
            bNext.setOnClickListener { viewModel.update(SwapMessage.Next) }
        }
    }

    private fun observe(model: FixViewModel) {
        model.feed.observe(this, Observer { compositeAdapter.swapData(it) })
        model.progress.observe(this, Observer { bindings.pbLoading.isVisible = it })
        model.end.observe(this, Observer { Toast.makeText(this, "No more photos", Toast.LENGTH_LONG).show() })
    }

    companion object {
        private const val FIXING_TYPE_EXTRA = "FixingType"
        fun startIntent(type: FixingType, context: Context): Intent =
            Intent(context, FixActivity::class.java).apply {
                putExtra(FIXING_TYPE_EXTRA, type)
            }
    }
}