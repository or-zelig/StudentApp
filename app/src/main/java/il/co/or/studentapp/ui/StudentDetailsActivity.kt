package il.co.or.studentapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import il.co.or.studentapp.R
import il.co.or.studentapp.data.StudentRepository
import il.co.or.studentapp.util.Nav

class StudentDetailsActivity : AppCompatActivity() {

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvId = findViewById<TextView>(R.id.tvId)
        val cbChecked = findViewById<CheckBox>(R.id.cbCheckedReadOnly)
        val btnEdit = findViewById<Button>(R.id.btnEdit)

        currentId = intent.getStringExtra(Nav.EXTRA_STUDENT_ID)

        val student = currentId?.let { StudentRepository.getById(it) }
        if (student == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvName.text = student.name
        tvId.text = "ID: ${student.id}"
        cbChecked.isChecked = student.isChecked
        cbChecked.isEnabled = false

        btnEdit.setOnClickListener {
            val i = Intent(this, EditStudentActivity::class.java).apply {
                putExtra(Nav.EXTRA_STUDENT_ID, student.id) // משתמשים ב־ID כ-key
            }
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        val id = currentId ?: return
        val student = StudentRepository.getById(id) ?: return

        findViewById<TextView>(R.id.tvName).text = student.name
        findViewById<TextView>(R.id.tvId).text = "ID: ${student.id}"
        findViewById<CheckBox>(R.id.cbCheckedReadOnly).isChecked = student.isChecked

        // אם ה־ID השתנה בעריכה, נעדכן currentId כדי לא "לאבד" את הסטודנט
        currentId = student.id
    }
}
