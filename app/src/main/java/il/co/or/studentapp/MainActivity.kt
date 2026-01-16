package il.co.or.studentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import il.co.or.studentapp.data.Student
import il.co.or.studentapp.data.StudentRepository
import il.co.or.studentapp.ui.StudentsListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        seedIfEmpty()

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnOpenStudents).setOnClickListener {
            startActivity(Intent(this, StudentsListActivity::class.java))
        }
    }

    private fun seedIfEmpty() {
        if (StudentRepository.getAll().isNotEmpty()) return

        StudentRepository.add(Student(id = "123", name = "Alice", isChecked = false))
        StudentRepository.add(Student(id = "456", name = "Bob", isChecked = true))
        StudentRepository.add(Student(id = "789", name = "Charlie", isChecked = false))
    }
}
