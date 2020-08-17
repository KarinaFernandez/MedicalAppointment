package com.example.reserves.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reserves.R
import kotlinx.android.synthetic.main.doctor_item.view.*
import kotlin.collections.ArrayList

class DoctorAdapter(private var doctorList: ArrayList<DoctorItem>, private val listener: OnItemClickListener): RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>(), Filterable {

    var doctorFilterList = ArrayList<DoctorItem>()
    private lateinit var mcontext: Context

    init {
        doctorFilterList = doctorList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_item, parent, false)
        mcontext = parent.context
        return DoctorViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return doctorFilterList.size
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentDoctor = doctorFilterList[position]
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    doctorFilterList = doctorList
                } else {
                    val resultList = ArrayList<DoctorItem>()
                    for (row in doctorList) {
                        if (row.name?.contains(charSearch.toLowerCase())!!) {
                            resultList.add(row)
                        }
                    }
                    doctorFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = doctorFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                doctorFilterList = results?.values as ArrayList<DoctorItem>
                notifyDataSetChanged()
            }
        }
    }
}