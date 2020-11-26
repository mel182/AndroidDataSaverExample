package com.example.datasaverexampleapp.room_db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID

class DatabaseListAdapter : RecyclerView.Adapter<DatabaseListAdapter.DBViewHolder>() {

    private val userEntityList:ArrayList<UserEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(Layout.item_database_list_custom_cell,parent,false)
        return DBViewHolder(view)
    }

    override fun onBindViewHolder(holder: DBViewHolder, position: Int) {
        val userFound = userEntityList[position]
        holder.bind(userFound)
    }

    override fun getItemCount(): Int {
        return userEntityList.size
    }

    fun add(user:UserEntity)
    {
        userEntityList.add(user)
        notifyDataSetChanged()
    }

    fun remove(user:UserEntity)
    {
        userEntityList.remove(user)
        notifyDataSetChanged()
    }

    fun clearData()
    {
        userEntityList.clear()
        notifyDataSetChanged()
    }

    class DBViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        private var nameTextView = view.findViewById<TextView>(ViewByID.name_textView)
        private var idTextView = view.findViewById<TextView>(ViewByID.id_textView)
        private var ageTextView = view.findViewById<TextView>(ViewByID.age_textView)

        fun bind(user: UserEntity)
        {
            val fullname = "${user.name} ${user.lastname}"
            nameTextView?.text = fullname
            idTextView?.text = user.id.toString()
            ageTextView?.text = user.age.toString()
        }
    }
}