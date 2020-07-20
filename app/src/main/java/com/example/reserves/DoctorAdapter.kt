package com.example.reserves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.doctor_item.view.*

class DoctorAdapter(private val doctorList: ArrayList<DoctorItem>,
                    private val listener: OnItemClickListener): RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_item, parent, false)
        return DoctorViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentDoctor = doctorList[position]
        holder.nameTextView.text = currentDoctor.name
        holder.surnameTextView.text = currentDoctor.surname

        holder.initialize(currentDoctor, position)
    }

    inner class DoctorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.scheduleDoctor
        val surnameTextView: TextView = itemView.doctorInfo
        //   val specialtyTextView: TextView = itemView.specialty
        private val scheduleButton: Button = itemView.scheduleButton

        fun initialize(item: DoctorItem, position: Int) {
            scheduleButton.setOnClickListener {
                listener.onItemClick(item, position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: DoctorItem, position: Int)
    }
}