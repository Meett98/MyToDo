package com.example.mytodos.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodos.adapter.ITravelPostClick
import com.example.mytodos.adapter.TravelPostAdapter
import com.example.mytodos.broadcastreceiver.AirplaneModeChangeReceiver
import com.example.mytodos.databinding.ActivityMainBinding
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.db.TravelPostDatabase
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.fragment.ConfirmationDialogFragment
import com.example.mytodos.repository.TravelPostRepository
import com.example.mytodos.viewmodel.TravelPostViewModel
import com.example.mytodos.viewmodel.TravelPostViewModelFactory

class MainActivity : AppCompatActivity(),ITravelPostClick {
    private lateinit var binding: ActivityMainBinding
    private var receiver = AirplaneModeChangeReceiver()

    private lateinit var travelPostAdapter: TravelPostAdapter
    private lateinit var travelPostViewModel: TravelPostViewModel
    private lateinit var travelPostRepository: TravelPostRepository
    private lateinit var travelPostDao: TravelPostDao
    private lateinit var travelPostDatabase: TravelPostDatabase



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //for to-do list
//        todoAdapter= TodoAdapter(this,this)
//        database= TodoDatabase.getDatabase(this)
//        todoDAO = TodoDatabase.getDatabase(this).todoDAO()
//        toDoRepository = ToDoRepository(todoDAO)
//        mainViewModel= ViewModelProvider(this, MainViewModelFactory(toDoRepository))[MainViewModel::class.java]


        travelPostAdapter = TravelPostAdapter(this)
        travelPostDatabase = TravelPostDatabase.getDatabase(this)
        travelPostDao = TravelPostDatabase.getDatabase(this).travelpostDAO()
        travelPostRepository = TravelPostRepository(travelPostDao)
        travelPostViewModel = ViewModelProvider(this,TravelPostViewModelFactory(travelPostRepository))[TravelPostViewModel::class.java]


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

        addPost(username,password)
        setLayoutManagerAndAdapterForTravelPost()
        updateTravelPost(username)


    }



    private fun addPost(username: String, password: String) {
        binding.add.setOnClickListener {
            val iCreate=Intent(this@MainActivity, CreatePostActivity::class.java)
            iCreate.putExtra("USERNAME",username)
            iCreate.putExtra("PASSWORD",password)
            iCreate.putExtra("ID",0)
            iCreate.putExtra("TITLE","")
            iCreate.putExtra("LOCATION","")
            iCreate.putExtra("BUTTON_TEXT","SAVE")
            startActivity(iCreate)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
            unregisterReceiver(receiver)

    }



    private fun setLayoutManagerAndAdapterForTravelPost() {
        binding.recyclerView.apply {
            layoutManager= LinearLayoutManager(this@MainActivity)
            adapter=travelPostAdapter
        }
    }



    private fun updateTravelPost(username: String) {
        travelPostViewModel.getAllTravelPost(username)
        travelPostViewModel.allTravelPost.observe(this){travelPostlist ->
            travelPostlist?.let{
                travelPostAdapter.updateTravelPostList(it)

            }
        }
    }

    override fun onPostItemClick(entityPost: EntityPost) {
        val iUpdate=Intent(this@MainActivity, UpdatePostActivity::class.java)
        iUpdate.putExtra("USERNAME",entityPost.username)
        iUpdate.putExtra("PASSWORD",entityPost.password)
        iUpdate.putExtra("ID",entityPost.id)
        iUpdate.putExtra("POSTTITLE",entityPost.posttitle)
        iUpdate.putExtra("LOCATION",entityPost.location)
        iUpdate.putExtra("BUTTON_TEXT","Update")
        startActivity(iUpdate)

        setLayoutManagerAndAdapterForTravelPost()
        updateTravelPost(entityPost.username)
    }




    private fun clearSharedPreferenceData() {
        val pref: SharedPreferences = getSharedPreferences("Login_Activity", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  pref.edit()
        editor.clear()
        editor.apply()
    }




}