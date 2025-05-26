package com.example.gestiond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AddStudentFragment : Fragment() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editAge: EditText
    private lateinit var btnSave: Button

    private lateinit var db: FirebaseFirestore
    private var studentId: String? = null  // null si ajout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editName = view.findViewById(R.id.editName)
        editEmail = view.findViewById(R.id.editEmail)
        editAge = view.findViewById(R.id.editAge)
        btnSave = view.findViewById(R.id.btnSave)

        db = FirebaseFirestore.getInstance()

        studentId = arguments?.getString("studentId")

        if (studentId != null) {
            db.collection("students").document(studentId!!)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        val student = doc.toObject(Student::class.java)
                        editName.setText(student?.name ?: "")
                        editEmail.setText(student?.email ?: "")
                        editAge.setText(student?.age?.toString() ?: "")
                    }
                }
        }

        btnSave.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val ageText = editAge.text.toString().trim()
            val age = ageText.toIntOrNull() ?: 0

            if (name.isNotEmpty() && email.isNotEmpty() && age > 0) {
                val student = Student(name = name, email = email, age = age)

                if (studentId == null) {
                    db.collection("students").add(student)
                        .addOnSuccessListener {
                            requireActivity().onBackPressed()
                        }
                } else {
                    db.collection("students").document(studentId!!)
                        .set(student)
                        .addOnSuccessListener {
                            requireActivity().onBackPressed()
                        }
                }
            } else {
                // Gérer erreurs de validation ici si besoin
            }
        }
    }
}
