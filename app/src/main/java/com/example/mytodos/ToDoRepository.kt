package com.example.mytodos

class ToDoRepository(val gettodoDAO : TodoDAO) {


    suspend fun insertToDoRepo(entity: Entity)
    {
        gettodoDAO.insertTodo(entity)
    }
    suspend fun updateToDoRepo(entity: Entity)
    {
        gettodoDAO.updateTodo(entity)
    }
    suspend fun deleteToDoRepo(entity: Entity)
    {
        gettodoDAO.deleteTodo(entity)
    }
    fun getallToDoRepo(username:String) : List<Entity>
    {
        return gettodoDAO.getAllTodos(username)
    }

}