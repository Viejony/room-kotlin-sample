package com.example.roomkotlinsample.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomkotlinsample.R
import com.example.roomkotlinsample.tools.Utils


class AdapterEmails(
    private val context: Context,
    var values: ArrayList<String>) : RecyclerView.Adapter<AdapterEmails.ViewHolder>() {

    var afterDeleteItemtask: () -> (Unit) = {}

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        var emailText: TextView = mView.findViewById(R.id.email_text)
        var deleteButton: View = mView.findViewById(R.id.button_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_email, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.emailText.text = values[position]

        // Add listener for delete button
        holder.deleteButton.setOnClickListener{
            Utils().printLog("Clicked delete button")
            values.removeAt(position)
            notifyDataSetChanged()
            afterDeleteItemtask()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setAfterDeleteItemTask(task: () -> Unit){
        afterDeleteItemtask = task
    }

}