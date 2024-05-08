package com.vk.usersapp.feature.feed.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vk.usersapp.R
import com.vk.usersapp.feature.feed.model.User

class UserListItemVh(view: View) : RecyclerView.ViewHolder(view) {

    private val avatar: ImageView = view.findViewById(R.id.userPhoto)
    private val userName: TextView = view.findViewById(R.id.userName)
    private val universityName: TextView = view.findViewById(R.id.universityName)
    private val userAge: TextView = view.findViewById(R.id.userAge)

    fun bind(user: User) {
        Glide.with(avatar).load(user.image).into(avatar)
        userName.text = "${user.firstName} ${user.lastName}"
        universityName.text = user.university
        userAge.text = user.age.toString()
    }
}