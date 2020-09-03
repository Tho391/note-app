package com.thomas.mynoteapp.presentation

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thomas.core.data.Note
import com.thomas.mynoteapp.R
import com.thomas.mynoteapp.framework.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note.*


class NoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel
    private var currentNote = Note("", "", 0L, 0L)
    private var noteId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        arguments?.let {
            noteId = NoteFragmentArgs.fromBundle(it).noteId
        }
        if (noteId != 0L)
            viewModel.getNote(noteId)

        checkButton.setOnClickListener {
            if (titleView.editText?.text.toString()
                    .isNotEmpty() || contentView.editText?.text.toString().isNotEmpty()
            ) {
                val time = System.currentTimeMillis()
                currentNote.title = titleView.editText?.text.toString()
                currentNote.content = contentView.editText?.text.toString()
                currentNote.updateTime = time
                if (currentNote.id == 0L)
                    currentNote.creationTime = time
                viewModel.saveNote(currentNote)
            } else {
                Navigation.findNavController(it).popBackStack()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this, Observer {
            if (it) {
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                hideKeyboard()
                Navigation.findNavController(titleView).popBackStack()
            } else
                Toast.makeText(
                    context,
                    "Something went wrong, please try again",
                    Toast.LENGTH_SHORT
                ).show()

        })

        viewModel.currentNote.observe(this, Observer { note ->
            note?.let {
                currentNote = it
                titleView.editText?.setText(it.title, TextView.BufferType.EDITABLE)
                contentView.editText?.setText(it.content, TextView.BufferType.EDITABLE)
            }
        })
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(titleView.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteNote -> {
                if (context != null && noteId != 0L) {
                    MaterialAlertDialogBuilder(context!!)
                        .setTitle("Delete note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes") { dialog, which ->
                            viewModel.deleteNote(
                                currentNote
                            )
                        }
                        .setNegativeButton("Cancel") { dialog, which -> }
                        .create().show()
                }
            }
        }
        return true
    }
}