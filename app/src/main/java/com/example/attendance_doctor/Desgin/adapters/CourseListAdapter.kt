package com.example.attendance_doctor.Desgin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.R

import kotlinx.android.synthetic.main.list_item_course_view.view.*

class CourseListAdapter(var allCoursesList : List<Course>?) : RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_course_view, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.itemView
        holder.itemView.apply {
            mCourseName.text=allCoursesList!![position].courseName

            mCourseDay.text=allCoursesList!![position].courseDate
            mCourseTimeTo.text= allCoursesList!![position].courseTimeFrom
            mCourseTimeFrom.text = allCoursesList!![position].courseTimeTo
            mCourseLocation.text=allCoursesList!![position].coursePlace
            if (allCoursesList!![position].courseGroup=="Group 1"){
                mCourseCode.text=allCoursesList!![position].courseCode+"\nG1"
            }else{
                mCourseCode.text=allCoursesList!![position].courseCode+"\nG2"
            }

        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.let { it(allCoursesList!![position]) }
            return@setOnLongClickListener true
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(allCoursesList!![position]) }
        }
    }
    override fun getItemCount(): Int {
        return allCoursesList!!.size
    }
    private var onItemClickListener: ((Course) -> Unit)? = null
    fun setOnItemClickListener(listener: (Course) -> Unit) {
        onItemClickListener = listener
    }
    private var onItemLongClickListener: ((Course) -> Unit)? = null
    fun setOnItemLongClickListener(listener: (Course) -> Unit) {
        onItemLongClickListener = listener
    }
}