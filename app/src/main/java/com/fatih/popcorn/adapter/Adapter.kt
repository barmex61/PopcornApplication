package com.fatih.popcorn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R

class Adapter:RecyclerView.Adapter<Adapter.AdapterViewHolder>() {

    var list= mutableListOf<String>()

    class AdapterViewHolder(val itemView: View) :RecyclerView.ViewHolder(itemView){
        val textView=itemView.findViewById<TextView>(R.id.imText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.textView.text=list[position]
    }
}