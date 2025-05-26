package com.example.gestiond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class StudentListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var db: FirebaseFirestore
    private lateinit var studentList: MutableList<Student>
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        fab = view.findViewById(R.id.fab)
        db = FirebaseFirestore.getInstance()
        studentList = mutableListOf()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = StudentAdapter(
            students = studentList,
            onEditClick = { student: Student ->
                val bundle = Bundle().apply {
                    putString("studentId", student.id)
                }
                findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment, bundle)
            },
            onDeleteClick = { student: Student ->
                db.collection("students").document(student.id).delete()
                    .addOnSuccessListener {
                        studentList.remove(student)
                        adapter.notifyDataSetChanged()
                    }
            }
        )

        recyclerView.adapter = adapter

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
        }

        loadStudents()
    }

    private fun loadStudents() {
        db.collection("students")
            .get()
            .addOnSuccessListener { result ->
                studentList.clear()
                for (document in result) {
                    val student = document.toObject(Student::class.java)
                    student.id = document.id // important pour suppression ou modification
                    studentList.add(student)
                }
                adapter.notifyDataSetChanged()
            }
    }
}
