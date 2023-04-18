package com.example.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()

//        val rmp =  EmployeeDatabase.getinstance(this).employeeDao()
        binding?.btnAdd?.setOnClickListener(){
            addrecord(employeeDao)
        }

        lifecycleScope.launch(){
            employeeDao.fetchAllEmployees().collect(){
               val list = ArrayList(it)
                setuplistofdataintorecyclerview(list,employeeDao)
            }
         }

    }

    fun addrecord(employeeDao: EmployeeDao){

        var name = binding?.etName?.text.toString()
        var email = binding?.etEmailId?.text.toString()

        lifecycleScope.launch{
            employeeDao.insert(EmployeeEntity(name= name, email= email))
            binding?.etName?.text?.clear()
            binding?.etEmailId?.text?.clear()

        }
    }

    private fun setuplistofdataintorecyclerview (employeelist : ArrayList<EmployeeEntity>,employeedao: EmployeeDao ){
        if (employeelist.isNotEmpty()){
            val itemAdapter = ItemAdapter(employeelist,
                {
                    updateId -> updaterecorddialogue(updateId,employeedao)
                },
                {
                  deleteId -> deleteRecordAlertDialog(deleteId,employeedao)
                }
            )

            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        }else
        {
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }


    fun updaterecorddialogue (id: Int, employeeDao: EmployeeDao){

        val updatedialog = Dialog(this, com.google.android.material.R.style.Theme_AppCompat_Dialog)
        updatedialog.setCancelable(false)
        var binding = DialogUpdateBinding.inflate(layoutInflater)
        updatedialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDao.fetchEmployeesbyid(id).collect {
                if (it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }
            }
        }
        binding.tvUpdate.setOnClickListener {

            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    employeeDao.update(EmployeeEntity(id, name, email))
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG)
                        .show()
                    updatedialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.tvCancel.setOnClickListener{
            updatedialog.dismiss()
        }
        //Start the dialog and display it on screen.
        updatedialog.show()
    }
    fun deleteRecordAlertDialog(id:Int,employeeDao: EmployeeDao) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                dialogInterface.dismiss() // Dialog will be dismissed
            }

        }


        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}