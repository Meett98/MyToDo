package com.example.mytodos.repository

import com.example.mytodos.db.TodoDAO
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.entity.Entity
import com.example.mytodos.entity.EntityPost

class TravelPostRepository(val travelpostDAO : TravelPostDao) {


    suspend fun insertTravelPost(entityPost: EntityPost)
    {
        travelpostDAO.insertTravelPost(entityPost)
    }
    suspend fun updateTravelPost(entityPost: EntityPost)
    {
        travelpostDAO.updateTravelPost(entityPost)
    }
    suspend fun deleteTravelPost(entityPost: EntityPost)
    {
        travelpostDAO.deleteTravelPost(entityPost)
    }
    fun getallTravelPost(username:String) : List<EntityPost>
    {
        return travelpostDAO.getAllTravelPost(username)
    }

}