package il.co.or.studentapp.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import il.co.or.studentapp.R
import il.co.or.studentapp.data.Student
import il.co.or.studentapp.data.StudentRepository

class NewStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_student)

        val etName = findViewById<EditText>(R.id.etName)
        val etId = findViewById<EditText>(R.id.etId)
        val cbChecked = findViewById<CheckBox>(R.id.cbChecked)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val id = etId.text.toString().trim()

            val err = validate(name, id)
            if (err != null) {
                Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            StudentRepository.add(Student(id = id, name = name, isChecked = cbChecked.isChecked))
            finish()
        }
    }

    private fun validate(name: String, id: String): String? {
        if (name.isBlank()) return "Name is required"
        if (id.isBlank()) return "ID is required"
        if (StudentRepository.exists(id)) return "ID already exists"
        return null
    }
}
