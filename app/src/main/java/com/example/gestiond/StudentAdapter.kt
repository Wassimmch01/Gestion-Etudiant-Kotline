package com.example.gestiond

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val students: List<Student>,
    private val onEditClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val emailText: TextView = itemView.findViewById(R.id.emailText)
        val ageText: TextView = itemView.findViewById(R.id.ageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.nameText.text = student.name
        holder.emailText.text = student.email
        holder.ageText.text = "Âge : ${student.age}"

        holder.itemView.setOnClickListener { onEditClick(student) }
        holder.itemView.setOnLongClickListener {
            onDeleteClick(student)
            true
        }
    }

    override fun getItemCount(): Int = students.size
}
