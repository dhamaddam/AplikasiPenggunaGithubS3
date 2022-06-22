package com.dhamaddam.aplikasipenggunagithubs3.view.adapter

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.dhamaddam.aplikasipenggunagithubs3.databinding.ItemRowUserBinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dhamaddam.aplikasipenggunagithubs3.data.local.UserDiffCallback
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity
import com.dhamaddam.aplikasipenggunagithubs3.view.GithubUserDetailsActivity

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private val listUser = ArrayList<UserEntity>()
    fun setListUser(listUser: List<UserEntity>) {
        val diffCallback = UserDiffCallback(this.listUser, listUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(listUser)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class UserViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            with(binding) {
                Glide.with(itemView.getContext()).load(user.avatar).into(binding.imgItemPhoto)
                tvItemName.text = user.login
                tvItemDescription.text = user.type
                itemView.setOnClickListener {
                    val intent = Intent(it.context,GithubUserDetailsActivity::class.java)
                    intent.putExtra(GithubUserDetailsActivity.EXTRA_USER, user.login)
                    intent.putExtra(GithubUserDetailsActivity.EXTRA_USER_FAVORITE, user.isfavorite.toString())
                    Log.d(TAG, "bind: user.isfavorite"+user.isfavorite)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}