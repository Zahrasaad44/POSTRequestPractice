package com.example.postrequestpractice

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postrequestpractice.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var namesLocs: NamesAndLocations
    private lateinit var recyclerAdapter: NameLocationAdapter

    val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

    var nameLocationID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        namesLocs = NamesAndLocations()
        recyclerAdapter = NameLocationAdapter(namesLocs)
        binding.nameLocationRV.adapter = recyclerAdapter
        binding.nameLocationRV.layoutManager = LinearLayoutManager(this)


        apiInterface?.getNamesLocations()?.enqueue(object : Callback<NamesAndLocations> {
            override fun onResponse(
                call: Call<NamesAndLocations>,
                response: Response<NamesAndLocations>
            ) {
                namesLocs = response.body()!!
                recyclerAdapter.updateRecyclerView(namesLocs)
            }

            override fun onFailure(call: Call<NamesAndLocations>, t: Throwable) {
                Log.d("main", "Unable to get data: $t")
            }
        })

        binding.addBtn.setOnClickListener {
            apiInterface!!.addNameLocation(
                NamesAndLocationsItem(
                    binding.locationET.text.toString(),
                    binding.nameET.text.toString(),
                    0
                )
            ).enqueue(object : Callback<NamesAndLocationsItem> {
                override fun onResponse(
                    call: Call<NamesAndLocationsItem>,
                    response: Response<NamesAndLocationsItem>
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Your name and location added!",
                        Toast.LENGTH_LONG
                    ).show()
                    recreate()
                }

                override fun onFailure(call: Call<NamesAndLocationsItem>, t: Throwable) {
                    Log.d("main", "Could not Post data")
                }

            })

            binding.nameET.text.clear()
            binding.locationET.text.clear()
        }

        binding.updateDeleteBtn.setOnClickListener {
            showDeleteUpdateDialog()
        }
    }

    fun showDeleteUpdateDialog() {
        val deleteUpdateDialog = Dialog(this)
        deleteUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        deleteUpdateDialog.setCancelable(true)
        deleteUpdateDialog.setContentView(R.layout.delete_update)

        val pkET = deleteUpdateDialog.findViewById<EditText>(R.id.pkET)
        val nameET = deleteUpdateDialog.findViewById<EditText>(R.id.nameET)
        val locationET = deleteUpdateDialog.findViewById<EditText>(R.id.locationET)
        val updateBtn = deleteUpdateDialog.findViewById<Button>(R.id.updateBtn)
        val deleteBtn = deleteUpdateDialog.findViewById<Button>(R.id.deleteBtn)

        //val apiInterface by lazy { APIClient().getClient()?.create(APIInterface::class.java) }

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        updateBtn.setOnClickListener {
            if (pkET.text.isNotEmpty() && nameET.text.isNotEmpty() && locationET.text.isNotEmpty()) {

                try {
                    nameLocationID = pkET.text.toString().toInt()

                    apiInterface?.updateNameLocation(
                        nameLocationID, NamesAndLocationsItem(
                            locationET.text.toString(),
                            nameET.text.toString(),
                            nameLocationID
                        )


                    )?.enqueue(object : Callback<NamesAndLocationsItem> {
                        override fun onResponse(
                            call: Call<NamesAndLocationsItem>,
                            response: Response<NamesAndLocationsItem>
                        ) {
                            Log.d("gg", "${response.code()}")
                            Log.d("aa", "${response.errorBody()}")

                            Toast.makeText(
                                applicationContext,
                                "Name and Location updated",
                                Toast.LENGTH_LONG
                            ).show()
                            recreate()
                        }

                        override fun onFailure(call: Call<NamesAndLocationsItem>, t: Throwable) {
                            Log.d("main", "Could not PUT name and location $t")
                            Toast.makeText(
                                applicationContext,
                                "Couldn't update name and location",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
                } catch (exception: Exception) {
                    Toast.makeText(applicationContext, "Invalid data", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG)
                    .show()
            }
        }

        deleteBtn.setOnClickListener {
            try {
                nameLocationID = pkET.text.toString().toInt()

                apiInterface?.deleteNameLocation(nameLocationID)?.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Toast.makeText(
                            applicationContext,
                            "Name and Location deleted",
                            Toast.LENGTH_LONG
                        ).show()
                        recreate()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("main", "Could not DELETE name and location $t")
                        Toast.makeText(
                            applicationContext,
                            "Couldn't delete name and location",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
            } catch (exception: Exception) {
                Toast.makeText(applicationContext, "Invalid PK", Toast.LENGTH_LONG).show()
            }
        }
        deleteUpdateDialog.show()
    }
}