package com.dhamaddam.aplikasipenggunagithubs3.data.local

import androidx.recyclerview.widget.DiffUtil
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity

class UserDiffCallback(private val mOldUserList: List<UserEntity>, private val mNewUserList: List<UserEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldUserList[oldItemPosition]
        val newEmployee = mNewUserList[newItemPosition]
        return oldEmployee.id == newEmployee.id && oldEmployee.login == newEmployee.login
    }
}