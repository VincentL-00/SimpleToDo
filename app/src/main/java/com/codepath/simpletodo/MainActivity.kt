package com.codepath.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //Remove item from list
                listOfTasks.removeAt(position)
                //Notify the change to the adapter
                adapter.notifyDataSetChanged()

                //save changes
                saveItems()
            }
        }

        //Code for what happens when button is pressed
        /*findViewById<Button>(R.id.button).setOnClickListener{

            //Test log for button press
            //Log.i("Caren","User clicked on button")
        }
        */

        //listOfTasks.add("Do laundry")
        //listOfTasks.add("Go for a walk")

        loadItems()

        //Look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)


        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Set up button and input field for adding to list
        //reference button
        findViewById<Button>(R.id.button).setOnClickListener() {
            //grab text from input
            val userInputtedTask = inputTextField.text.toString()
            //add string to listOfTasks
            listOfTasks.add(userInputtedTask)
            //Tell adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //Reset field
            inputTextField.setText("")

            //Save to file
            saveItems()
        }
    }

    //Save inputted data by reading and writing from a file
    //Get the file
    fun getDataFile() : File {
        //Every line represents a specific task in listOfTasks
        return File(filesDir, "data.txt")
    }
    //Load items by reading every line in file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //Save items by writing them into our data file
    fun saveItems() {
        try {//catch err if file can't load
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}