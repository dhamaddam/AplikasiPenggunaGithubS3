package com.dhamaddam.aplikasipenggunagithubs3.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.GithubResponseItem
import com.dhamaddam.aplikasipenggunagithubs3.databinding.ItemRowUserBinding

class RecyclerViewAdapterFragment (private val listUser: ArrayList<GithubResponseItem>): RecyclerView.Adapter<RecyclerViewAdapterFragment.ListViewHolder>() {


    fun setFollowerData(item: ArrayList<GithubResponseItem>) {
        listUser.clear()
        listUser.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, username, type, avatar) = listUser[position]
        Glide.with(holder.itemView.getContext()).load(avatar).into(holder.binding.imgItemPhoto)
        holder.binding.tvItemName.text = username
        holder.binding.tvItemDescription.text = type

    }


    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding:ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}