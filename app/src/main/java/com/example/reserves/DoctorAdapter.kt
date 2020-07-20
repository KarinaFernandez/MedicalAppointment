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

        /*
        val sb = StringBuilder()
        currentDoctor.specialty?.forEach { sb.append(it).append(" - ")}
        val specialty = sb.removeSuffix("-").toString()
        holder.specialtyTextView.text = specialty
         */
    }

    inner class DoctorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val nameTextView: TextView = itemView.doctorName
        val surnameTextView: TextView = itemView.doctorSurname
        //   val specialtyTextView: TextView = itemView.specialty

        val scheduleButton: Button = itemView.scheduleButton

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}