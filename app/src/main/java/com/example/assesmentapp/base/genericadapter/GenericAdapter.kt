package com.example.assesmentapp.base.genericadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class GenericAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var listItems: ArrayList<T>

    constructor(listItems: List<T>) {
        this.listItems = ArrayList()
        this.listItems.clear()
        this.listItems.addAll(listItems)
    }

    constructor() {
        this.listItems = ArrayList()
    }

    fun clear(){
        this.listItems.clear()
        notifyDataSetChanged()
    }

    fun setItems(listItems: List<T>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int) {
        listItems.removeAt(position)
        notifyItemChanged(position)
        notifyItemRangeRemoved(position, 1)
    }
    fun insertItem(item:T){
        val lastCount=itemCount
        this.listItems.add(item)
        notifyItemRangeInserted(lastCount,listItems.size)
    }

    fun setLazyLoadItem(listItems: List<T>){
        if(itemCount==0){
            this.listItems.addAll(listItems)
            notifyDataSetChanged()
        }else{
            val lastCount=itemCount
            this.listItems.addAll(listItems)
            notifyItemRangeInserted(lastCount,listItems.size)
        }
    }

    fun removeItem(item:T){
        this.listItems.remove(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
            , viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }
}