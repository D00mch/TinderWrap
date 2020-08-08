package com.livermor.tinderwrap.ui.screen.fix

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import com.livermor.tinderwrap.data.PhotoRepository
import com.livermor.tinderwrap.data.RepoConst
import com.livermor.tinderwrap.databinding.ActivityFixBinding
import com.livermor.tinderwrap.factory.GlideFactory
import com.livermor.tinderwrap.factory.NamesImpl
import com.livermor.tinderwrap.factory.ViewModelsFactory
import com.livermor.tinderwrap.ui.adapter.PhotoAdapter
import com.livermor.tinderwrap.ui.adapter.SwipeCallback
import com.livermor.tinderwrap.ui.screen.SwapMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FixActivity : AppCompatActivity() {

    private val bindings by lazy { ActivityFixBinding.inflate(layoutInflater) }
    private val compositeAdapter = CompositeDelegateAdapter(PhotoAdapter())
    private val viewModel: FixViewModel by lazy { ViewModelsFactory.fixModel(this) }

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
    }
}