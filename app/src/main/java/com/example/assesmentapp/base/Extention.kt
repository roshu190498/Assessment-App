package com.example.assesmentapp.base

import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallBackKt<T>: Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}


/**
 * Retrofit
 */
fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}


fun AppCompatActivity.showBottomSheet(fragment: BottomSheetDialogFragment) {
    fragment.showNow(supportFragmentManager, fragment.tag)
}


fun AppCompatImageView.loadImage(url: String, @DrawableRes placeholder: Int?=null) {
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade()).apply(){
            placeholder?.let{
                error(it)
            }
        }
        .into(this)

}