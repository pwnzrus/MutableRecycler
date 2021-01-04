package asd.android.mutablerecycler

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnRecyclerItemClickListener {

    private lateinit var viewModel: NumbersListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var numbersListAdapter: NumbersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecycler()
        initViewModel()
    }


    private fun initRecycler() {
        recyclerView = findViewById(R.id.recycler_view)
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else if ((orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }

        numbersListAdapter = NumbersListAdapter(this)
        recyclerView.adapter = numbersListAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this
        ).get(NumbersListViewModel::class.java)
        viewModel.numberOfItems.observe(this, {
            numbersListAdapter.submitList(it.toMutableList())
        })
    }

    override fun onClick(pos: Int) {
        viewModel.removeItem(pos)
    }
}