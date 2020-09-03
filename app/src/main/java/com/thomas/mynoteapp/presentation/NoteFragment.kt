package com.thomas.mynoteapp.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.BufferType
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thomas.mynoteapp.R
import com.thomas.mynoteapp.framework.NoteViewModel
import com.thomas.mynoteapp.utils.Utils
import com.thomas.mynoteapp.utils.convertToDate
import com.thomas.mynoteapp.utils.convertToTime
import kotlinx.android.synthetic.main.fragment_note.*


class NoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel

    private lateinit var timePicker: TimePickerDialog
    private lateinit var datePicker: DatePickerDialog

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
            viewModel.currentId = NoteFragmentArgs.fromBundle(it).noteId
        }
        if (viewModel.currentId != 0L)
            viewModel.getNote(viewModel.currentId)

        setUpTimePicker()

        setUpDatePicker()

        checkButton.setOnClickListener {
            viewModel.currentTitle = titleView.editText?.text.toString()
            viewModel.currentContent = contentView.editText?.text.toString()
            viewModel.currentReminder = Utils.getTime(
                timeView.editText?.text.toString(),
                dateView.editText?.text.toString()
            )
            viewModel.saveNote()
        }
        timeView.setStartIconOnClickListener {
            timePicker.show()
        }
        dateView.setStartIconOnClickListener {
            datePicker.show()
        }

        observeViewModel()
    }

    private fun setUpDatePicker() {
        datePicker = DatePickerDialog(requireContext())
        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            dateView.editText?.setText("$dayOfMonth/$month/$year", BufferType.EDITABLE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpTimePicker() {
        val currentHour = Utils.getCurrentHour()
        val currentMinute = Utils.getCurrentMinute()

        timePicker = TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minute ->
                timeView.editText?.setText("$hourOfDay:$minute")
            },
            currentHour,
            currentMinute,
            true
        )
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
                titleView.editText?.setText(it.title, BufferType.EDITABLE)
                contentView.editText?.setText(it.content, BufferType.EDITABLE)
                dateView.editText?.setText(it.reminder.convertToDate(), BufferType.EDITABLE)
                timeView.editText?.setText(it.reminder.convertToTime(), BufferType.EDITABLE)
            }
        })

        viewModel.toast.observe(this, Observer {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
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
                if (context != null && viewModel.currentId != 0L) {
                    MaterialAlertDialogBuilder(context!!)
                        .setTitle("Delete note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes") { dialog, which ->
                            viewModel.currentNote.value?.let {
                                viewModel.deleteNote(it)
                            }

                        }
                        .setNegativeButton("Cancel") { dialog, which -> }
                        .create().show()
                }
            }
        }
        return true
    }
}