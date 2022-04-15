package com.example.attendance_doctor.Desgin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_doctor.Data.Student
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.student_list_item_view.view.*


class StudentAdapter() :
    androidx.recyclerview.widget.ListAdapter<Student, StudentAdapter.StudentViewHolder>(
        StudentDiffCallback()
    ) {

    //         RecyclerView.Adapter<TodoAdapter.TodoViewHolder>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_item_view, parent, false)
        return StudentViewHolder(view)
    }


    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.apply {
            id.text = getItem(position).StudentID
            name.text = getItem(position).StudentName
        }
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id = itemView.mStudentId
        var name = itemView.mStudentName
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(p0: Student, p1: Student): Boolean {
            return p0.StudentID == p1.StudentID
        }

        override fun areContentsTheSame(p0: Student, p1: Student): Boolean {
            return p0.equals(p1)
        }
    }


}