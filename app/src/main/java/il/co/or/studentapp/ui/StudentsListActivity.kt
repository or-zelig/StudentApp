package il.co.or.studentapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import il.co.or.studentapp.R
import il.co.or.studentapp.data.StudentRepository
import il.co.or.studentapp.util.Nav

class StudentsListActivity : AppCompatActivity() {

    private lateinit var adapter: StudentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        val rv = findViewById<RecyclerView>(R.id.rvStudents)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        adapter = StudentsAdapter(
            items = StudentRepository.getAll(),
            onRowClick = { student ->
                val i = Intent(this, StudentDetailsActivity::class.java).apply {
                    putExtra(Nav.EXTRA_STUDENT_ID, student.id)
                }
                startActivity(i)
            },
            onCheckedChange = { student, checked ->
                StudentRepository.setChecked(student.id, checked)
            }
        )

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btnAdd.setOnClickListener {
            startActivity(Intent(this, NewStudentActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(StudentRepository.getAll())
    }
}
