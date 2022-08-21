package com.example.assesmentapp.errorhandling

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.example.assesmentapp.R
import com.example.assesmentapp.base.TARGETREQUESTCODE
import com.example.assesmentapp.databinding.FragmentErrorDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ErrorDialog : BottomSheetDialogFragment() {

    protected lateinit var mDataBinding:FragmentErrorDialogBinding

    private var mRequestCode : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mDataBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_error_dialog, container, false)
        mDataBinding.root.isClickable=true
        return mDataBinding.root
    }


    override fun onStart() {
        super.onStart()
        mDataBinding.btnCancel.setOnClickListener {
            dismiss()
            activity?.finish()
        }
        mDataBinding.btnRetry.setOnClickListener {
            dismiss()
            setFragmentResult(arguments?.getString(TARGETREQUESTCODE)?:"",Bundle())
        }
    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ErrorDialog().apply {
            }
    }
}