package com.dhamaddam.aplikasipenggunagithubs3.view.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dhamaddam.aplikasipenggunagithubs3.view.FragmentFollower
import com.dhamaddam.aplikasipenggunagithubs3.view.FragmentFollowing

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)  {

    var username : String = ""


    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {

        var fragment: Fragment

        if(position == 0){
            fragment = FragmentFollower()
            fragment.arguments = Bundle().apply {
                putInt(FragmentFollower.ARG_SECTION_NUMBER, position + 1)
                putString(FragmentFollower.USERNAME, username)
            }
            return fragment;
        } else {
            fragment= FragmentFollowing()

            fragment.arguments = Bundle().apply {
                putString(FragmentFollower.USERNAME, username)
            }
        }


        return fragment

    }


}