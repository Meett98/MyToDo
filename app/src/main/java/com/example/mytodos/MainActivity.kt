package com.example.mytodos

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter=TodoAdapter(this,this)
        database=TodoDatabase.getDatabase(this)
        todoDAO = TodoDatabase.getDatabase(this).todoDAO()
        toDoRepository = ToDoRepository(todoDAO)
        mainViewModel= ViewModelProvider(this,MainViewModelFactory(toDoRepository)).get(MainViewModel::class.java)



        //display username and password from the login activity
        val pref=getSharedPreferences("Login_Activity", MODE_PRIVATE)
        val username=pref.getString("USERNAME","Meet")!!
        val password=pref.getString("PASSWORD","1234")!!



        //displaying text on the welcome page
        binding.tvDisplay.text="Welcome, "+username


        //Onclick listener event on logout button
        binding.btnLogout.setOnClickListener {
            clearSharedPreferenceData()
            val iPrev=Intent(this, LoginActivity::class.java)
            startActivity(iPrev)
        }



        //Add Button OnclickListener
        addTodos(username,password)

        //Recyclerview and adapter
        setLayoutManagerAndAdapter()

        //Update the list
        UpdateTodolist(username)
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


    private fun UpdateTodolist(username: String) {
        mainViewModel.getAllTodos(username)
        mainViewModel.alltodos.observe(this) { todolist ->
            todolist?.let {
                todoAdapter.updateTodoList(it)
            }
        }
    }

    override fun onItemClick(entity: Entity) {
        val iUpdate=Intent(this@MainActivity,CreateUpdateActivity::class.java)
        iUpdate.putExtra("USERNAME",entity.username)
        iUpdate.putExtra("PASSWORD",entity.password)
        iUpdate.putExtra("ID",entity.id)
        iUpdate.putExtra("TITLE",entity.title)
        iUpdate.putExtra("BUTTON_TEXT","Update")
        startActivity(iUpdate)
        setLayoutManagerAndAdapter()
        UpdateTodolist(entity.username)
    }

    override fun onDeleteClick(entity: Entity) {
        mainViewModel.deleteTodo(entity)
        setLayoutManagerAndAdapter()
        UpdateTodolist(entity.username)
        Toast.makeText(this, "${entity.title} Deleted", Toast.LENGTH_SHORT).show()
    }



    private fun clearSharedPreferenceData() {
        val pref: SharedPreferences = getSharedPreferences("Login_Activity", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  pref.edit()
        editor.clear()
        editor.apply()
    }

}