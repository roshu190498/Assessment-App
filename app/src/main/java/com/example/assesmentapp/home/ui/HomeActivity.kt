package com.example.assesmentapp.home.ui

import android.app.Dialog
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assesmentapp.R
import com.example.assesmentapp.base.Status
import com.example.assesmentapp.base.genericadapter.GenericAdapter
import com.example.assesmentapp.databinding.ActivityHomeBinding
import com.example.assesmentapp.databinding.AdapterItemTestBinding
import com.example.assesmentapp.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var mDatabinding : ActivityHomeBinding

    @Inject
    lateinit var progressDialog : Dialog

    private val homeViewModel: HomeViewModel by viewModels()

    private val dummyGenericAdapter by lazy {
        object : GenericAdapter<Int>() {
            override fun getLayoutId(position: Int, obj: Int): Int {
                return R.layout.adapter_item_test
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val itemAccountListBinding = AdapterItemTestBinding.inflate(inflater)
                val lp = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                itemAccountListBinding.root.layoutParams = lp
                return TestViewHolder(itemAccountListBinding)
            }
        }
    }

    inner class TestViewHolder(private val viewBinding: AdapterItemTestBinding) :
        RecyclerView.ViewHolder(viewBinding.root), GenericAdapter.Binder<Int> {
        override fun bind(data: Int) {
            viewBinding.txtTittle.text = data.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        homeViewModel.getEmployeeData()

        mDatabinding.dummyRV.apply {
            val layoutManager = LinearLayoutManager(context)
            this.layoutManager = layoutManager
            this.setHasFixedSize(true)
            this.adapter = dummyGenericAdapter
            ViewCompat.setNestedScrollingEnabled(this, false)
            this.addItemDecoration(object :
                DividerItemDecoration(context, layoutManager.orientation) {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    if (position == state.itemCount - 1) {
                        outRect.setEmpty()
                    } else {
                        super.getItemOffsets(outRect, view, parent, state)
                    }
                }
            }.apply {

            })
        }

        initObserver()
    }

    private fun initObserver() {
        homeViewModel.employeeData.observe(this@HomeActivity, Observer {
            when(it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                }
                Status.SUCCESS -> {
                    var dummyList = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
                    dummyGenericAdapter.setItems(dummyList)
                    progressDialog.dismiss()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    mDatabinding.sampleTest.text = "Something went wrong"
                }
            }
        })
    }
}