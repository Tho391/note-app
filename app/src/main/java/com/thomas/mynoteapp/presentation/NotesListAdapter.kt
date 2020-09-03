package com.thomas.mynoteapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thomas.core.data.Note
import com.thomas.mynoteapp.R
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotesListAdapter(private val notes: ArrayList<Note>, private val action: ListAction) :
    RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout = view.noteLayout
        private val noteTitle = view.title
        private val noteContent = view.content
        private val noteDate = view.date
        private val wordCount = view.wordCount
        fun bind(note: Note) {
            noteTitle.text = note.title
            noteContent.text = note.content

            val sdf = SimpleDateFormat("MMM dd, HH:mm:ss ", Locale.getDefault())
            val resultDate = Date(note.updateTime)
            noteDate.text = "Last update ${sdf.format(resultDate)}"

            wordCount.text = "Word: $note.wordCount.toString()"

            layout.setOnClickListener { action.onClick(note.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    fun updateNote(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}