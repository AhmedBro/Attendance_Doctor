package com.example.attendance_doctor.Desgin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.lectcure_shap.view.*

import kotlinx.android.synthetic.main.list_item_course_view.view.*

class LecturesAdapter(var allLecturesList : List<String>?) : RecyclerView.Adapter<LecturesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lectcure_shap, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.itemView
        holder.itemView.apply {

            mLecDate.text=allLecturesList!![position]



        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.let { it(allLecturesList!![position]) }
            return@setOnLongClickListener true
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(allLecturesList!![position]) }
        }
    }
    override fun getItemCount(): Int {
        return allLecturesList!!.size
    }
    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
    private var onItemLongClickListener: ((String) -> Unit)? = null
    fun setOnItemLongClickListener(listener: (String) -> Unit) {
        onItemLongClickListener = listener
    }
}