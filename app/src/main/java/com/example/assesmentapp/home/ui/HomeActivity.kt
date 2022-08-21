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
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assesmentapp.R
import com.example.assesmentapp.base.Status
import com.example.assesmentapp.base.TARGETREQUESTCODE
import com.example.assesmentapp.base.genericadapter.GenericAdapter
import com.example.assesmentapp.base.loadImage
import com.example.assesmentapp.base.showBottomSheet
import com.example.assesmentapp.databinding.ActivityHomeBinding
import com.example.assesmentapp.databinding.AdapterItemTestBinding
import com.example.assesmentapp.databinding.ItemEmpDetailsBinding
import com.example.assesmentapp.errorhandling.ErrorDialog
import com.example.assesmentapp.home.model.Details
import com.example.assesmentapp.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var mDatabinding : ActivityHomeBinding

    @Inject
    lateinit var progressDialog : Dialog

    private val homeViewModel: HomeViewModel by viewModels()

    private val empAdapter by lazy {
        object : GenericAdapter<Details>() {
            override fun getLayoutId(position: Int, obj: Details): Int {
                return R.layout.item_emp_details
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val itemAccountListBinding = ItemEmpDetailsBinding.inflate(inflater)
                val lp = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                itemAccountListBinding.root.layoutParams = lp
                lp.bottomMargin = 10
                return EmpViewHolder(itemAccountListBinding)
            }
        }
    }

    inner class EmpViewHolder(private val viewBinding: ItemEmpDetailsBinding) :
        RecyclerView.ViewHolder(viewBinding.root), GenericAdapter.Binder<Details> {
        override fun bind(data: Details) {
            viewBinding.ivEmpIcon.loadImage(
                data.profileImage?:"",
                R.drawable.ic_employeee
            )
            viewBinding.tvEmpName.text = getString(R.string.txtName,data.employeeName)
            viewBinding.tvAge.text = getString(R.string.txtage,data.employeeAge)
            viewBinding.tvEmpId.text = getString(R.string.txtId,data.id)
            viewBinding.tvSalary.text = getString(R.string.txtSalary,data.employeeSalary)

            viewBinding.root.setOnClickListener {
                viewBinding.tvSalary.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        homeViewModel.getEmployeeData()

        mDatabinding.rvEmpList.apply {
            val layoutManager = LinearLayoutManager(context)
            this.layoutManager = layoutManager
            this.setHasFixedSize(true)
            this.adapter = empAdapter
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

                    it.data?.data?.let{
                        mDatabinding.sampleTest.visibility = View.VISIBLE
                        mDatabinding.rvEmpList.visibility = View.VISIBLE
                        empAdapter.setItems(it)
                    }
                    progressDialog.dismiss()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    mDatabinding.sampleTest.visibility = View.GONE
                    mDatabinding.rvEmpList.visibility = View.GONE
                    ErrorDialog.newInstance().apply {
                        arguments = Bundle().apply {
                            putString(TARGETREQUESTCODE,"Error_code_1001")
                        }
                        showBottomSheet(this)
                        setFragmentResultListener("Error_code_1001"){requestKey, bundle ->
                            //Handle Click Here
                        }
                    }

                }
            }
        })
    }
}