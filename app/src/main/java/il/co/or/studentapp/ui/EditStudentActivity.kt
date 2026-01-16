package il.co.or.studentapp.ui
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import il.co.or.studentapp.R
import il.co.or.studentapp.data.Student
import il.co.or.studentapp.data.StudentRepository
import il.co.or.studentapp.util.Nav

class EditStudentActivity : AppCompatActivity() {

    private var originalId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        val etName = findViewById<EditText>(R.id.etName)
        val etId = findViewById<EditText>(R.id.etId)
        val cbChecked = findViewById<CheckBox>(R.id.cbChecked)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        originalId = intent.getStringExtra(Nav.EXTRA_STUDENT_ID)
        val student = originalId?.let { StudentRepository.getById(it) }

        if (student == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // preload
        etName.setText(student.name)
        etId.setText(student.id)
        cbChecked.isChecked = student.isChecked

        btnUpdate.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newId = etId.text.toString().trim()

            val err = validate(newName, newId, originalId!!)
            if (err != null) {
                Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updated = Student(
                id = newId,
                name = newName,
                isChecked = cbChecked.isChecked
            )

            StudentRepository.update(originalId!!, updated)
            originalId = updated.id

            intent = intent.apply {
                putExtra(Nav.RESULT_UPDATED_ID, updated.id)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        btnDelete.setOnClickListener {
            StudentRepository.delete(originalId!!)

            intent = intent.apply {
                putExtra(Nav.RESULT_DELETED, true)
            }
            setResult(RESULT_OK, intent)
            finish()

        }
    }

    private fun validate(name: String, id: String, originalId: String): String? {
        if (name.isBlank()) return "Name is required"
        if (id.isBlank()) return "ID is required"
        if (id != originalId && StudentRepository.exists(id)) return "ID already exists"
        return null
    }
}
