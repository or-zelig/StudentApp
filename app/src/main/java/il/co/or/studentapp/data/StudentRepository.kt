package il.co.or.studentapp.data

object StudentRepository {
    val students: MutableList<Student> = mutableListOf()

    fun getAll(): List<Student> = students.toList()

    fun getById(id: String): Student? = students.firstOrNull { it.id == id }

    fun exists(id: String): Boolean = students.any { it.id == id }

    fun add(student: Student) {
        students.add(student)
    }

    fun update(originalId: String, updated: Student): Boolean {
        val idx = students.indexOfFirst { it.id == originalId }
        if (idx == -1) return false
        students[idx] = updated
        return true
    }

    fun delete(id: String): Boolean {
        val s = getById(id) ?: return false
        return students.remove(s)
    }

    fun setChecked(id: String, checked: Boolean): Boolean {
        val s = getById(id) ?: return false
        s.isChecked = checked
        return true
    }
}
