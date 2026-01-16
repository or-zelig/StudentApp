package il.co.or.studentapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import il.co.or.studentapp.MainActivity
import il.co.or.studentapp.R
import il.co.or.studentapp.data.StudentRepository
import il.co.or.studentapp.util.Nav

class StudentDetailsActivity : AppCompatActivity() {

    private var currentId: String? = null

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult

        val data = result.data ?: return@registerForActivityResult

        if (data.getBooleanExtra(Nav.RESULT_DELETED, false)) {
            finish()
            return@registerForActivityResult
        }

        val updatedId = data.getStringExtra(Nav.RESULT_UPDATED_ID)
        if (!updatedId.isNullOrBlank()) {
            currentId = updatedId
        }

        currentId?.let { renderStudent(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        currentId = intent.getStringExtra(Nav.EXTRA_STUDENT_ID)
        val id = currentId ?: run {
            finish()
            return
        }

        renderStudent(id)

        // Edit
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val current = currentId ?: return@setOnClickListener
            val i = Intent(this, EditStudentActivity::class.java).apply {
                putExtra(Nav.EXTRA_STUDENT_ID, current)
            }
            editLauncher.launch(i)
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val id = currentId ?: return@setOnClickListener
            StudentRepository.delete(id)
            finish()
        }

        findViewById<Button>(R.id.btnBackToList).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnBackToMain).setOnClickListener {
            val i = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(i)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        // אם משהו השתנה (גם בלי ActivityResult), נרענן
        currentId?.let { renderStudent(it) }
    }

    private fun renderStudent(id: String) {
        val student = StudentRepository.getById(id)
        if (student == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.tvName).text = student.name
        findViewById<TextView>(R.id.tvId).text = "ID: ${student.id}"

        findViewById<CheckBox>(R.id.cbCheckedReadOnly).apply {
            isChecked = student.isChecked
            isEnabled = false
        }

        // אם ה-Repository אצלך מאפשר שינוי ID, נשמור את החדש כדי לא "לאבד" סטודנט
        currentId = student.id
    }
}
