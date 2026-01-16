package il.co.or.studentapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import il.co.or.studentapp.R
import il.co.or.studentapp.data.Student

class StudentsAdapter(
    private var items: List<Student>,
    private val onRowClick: (Student) -> Unit,
    private val onCheckedChange: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<StudentsAdapter.VH>() {

    fun submitList(newItems: List<Student>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_student_row, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val s = items[position]
        holder.tvName.text = s.name
        holder.tvId.text = "ID: ${s.id}"

        // אם יש לכם תמונה סטטית אמיתית - פה תעדכנו (כרגע אייקון דיפולט)
        holder.ivAvatar.setImageResource(android.R.drawable.sym_def_app_icon)

        // חשוב: למנוע טריגר כפול
        holder.cbChecked.setOnCheckedChangeListener(null)
        holder.cbChecked.isChecked = s.isChecked
        holder.cbChecked.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(s, isChecked)
        }

        holder.itemView.setOnClickListener { onRowClick(s) }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val cbChecked: CheckBox = itemView.findViewById(R.id.cbChecked)
    }
}
