package com.example.mytodos

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),IToDoClick,IToDoDelete {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var toDoRepository: ToDoRepository
    private lateinit var todoDAO: TodoDAO
    private lateinit var database: TodoDatabase
    private var receiver = AirplaneModeChangeReceiver()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter=TodoAdapter(this,this)
        database=TodoDatabase.getDatabase(this)
        todoDAO = TodoDatabase.getDatabase(this).todoDAO()
        toDoRepository = ToDoRepository(todoDAO)
        mainViewModel= ViewModelProvider(this,MainViewModelFactory(toDoRepository))[MainViewModel::class.java]




        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        // Register the BroadcastReceiver with the IntentFilter
        registerReceiver(receiver, intentFilter)


        //display username and password from the login activity
        val pref=getSharedPreferences("Login_Activity", MODE_PRIVATE)
        val username=pref.getString("USERNAME","")!!
        val password=pref.getString("PASSWORD","")!!




        //displaying text on the welcome page
        binding.tvDisplay.text= "Welcome, $username"


        //Onclick listener event on logout button
        binding.btnLogout.setOnClickListener {
            clearSharedPreferenceData()
            val iPrev=Intent(this, LoginActivity::class.java)
            startActivity(iPrev)
        }

        binding.btnDownload.setOnClickListener{

            val dialogFragment = ConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "ConfirmationDialog")
        }



        //Add Button OnclickListener
        addTodos(username,password)

        //Recyclerview and adapter
        setLayoutManagerAndAdapter()

        //Update the list
        updateTodolist(username)
    }


    override fun onDestroy() {
        super.onDestroy()
            unregisterReceiver(receiver)

    }
    private fun addTodos(username : String,password:String) {
        binding.add.setOnClickListener {
            val iCreate=Intent(this@MainActivity,CreateUpdateActivity::class.java)
            iCreate.putExtra("USERNAME",username)
            iCreate.putExtra("PASSWORD",password)
            iCreate.putExtra("ID",0)
            iCreate.putExtra("TITLE","")
            iCreate.putExtra("BUTTON_TEXT","SAVE")
            Log.i("UUU",username)
            startActivity(iCreate)
        }
    }


    private fun setLayoutManagerAndAdapter() {
        binding.recyclerView.apply {
            layoutManager= LinearLayoutManager(this@MainActivity)
            adapter=todoAdapter
        }
    }


    private fun updateTodolist(username: String) {
        mainViewModel.getAllTodos(username)
        mainViewModel.alltodos.observe(this) { todolist ->
            todolist?.let {
                todoAdapter.updateTodoList(it)
            }
        }
    }

    override fun onItemClick(entity: Entity) {
        val iUpdate=Intent(this@MainActivity,UpdateSendAcitivity::class.java)
        iUpdate.putExtra("USERNAME",entity.username)
        iUpdate.putExtra("PASSWORD",entity.password)
        iUpdate.putExtra("ID",entity.id)
        iUpdate.putExtra("TITLE",entity.title)
        iUpdate.putExtra("BUTTON_TEXT","Update")
        startActivity(iUpdate)
        setLayoutManagerAndAdapter()
        updateTodolist(entity.username)
    }

    override fun onDeleteClick(entity: Entity) {
        mainViewModel.deleteTodo(entity)
        setLayoutManagerAndAdapter()
        updateTodolist(entity.username)
        Toast.makeText(this, "${entity.title} Deleted", Toast.LENGTH_SHORT).show()
    }



    private fun clearSharedPreferenceData() {
        val pref: SharedPreferences = getSharedPreferences("Login_Activity", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  pref.edit()
        editor.clear()
        editor.apply()
    }


}