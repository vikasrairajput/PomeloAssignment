package com.example.pomeloassignment.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pomeloassignment.R
import com.example.pomeloassignment.data.PickUpDataModel


class ListAdapter(var arrayList: ArrayList<PickUpDataModel>) :
    RecyclerView.Adapter<ListAdapter.PickUpViewHolder>() {

    companion object {
        private val TAG = ListAdapter::class.java.simpleName
    }

    fun updateList(arrayList: ArrayList<PickUpDataModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = PickUpViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_adapter, parent, false)
    )

    override fun getItemCount() = arrayList.size
    override fun onBindViewHolder(holder: PickUpViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    inner class PickUpViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        private val addressTextView = view.findViewById<TextView>(R.id.addressTextView)
        private val cityTextView = view.findViewById<TextView>(R.id.cityTextView)

        fun bind(pickUpDataModel: PickUpDataModel) {
            titleTextView.text = pickUpDataModel.alias
            addressTextView.text = pickUpDataModel.address1
            cityTextView.text = pickUpDataModel.city
        }
    }


}