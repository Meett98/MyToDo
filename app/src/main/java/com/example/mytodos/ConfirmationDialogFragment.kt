package com.example.mytodos

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setMessage("Are you sure you want to download?")
            .setPositiveButton("Yes") { _, _ ->
                // Start the download process here
                val idownload = Intent(requireActivity(),DownloadActivity::class.java)
                startActivity(idownload)
            }
            .setNegativeButton("No", null)
            .create()
    }
}