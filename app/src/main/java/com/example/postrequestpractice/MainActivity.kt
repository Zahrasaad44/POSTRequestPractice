package com.example.postrequestpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postrequestpractice.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var namesLocs: NamesAndLocations
    private lateinit var recyclerAdapter: NameLocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        namesLocs = NamesAndLocations()
        recyclerAdapter = NameLocationAdapter(namesLocs)
        binding.nameLocationRV.adapter = recyclerAdapter
        binding.nameLocationRV.layoutManager = LinearLayoutManager(this)

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

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
                    Toast.makeText(applicationContext,"Your name and location added!", Toast.LENGTH_LONG).show()
                    recreate()
                }

                override fun onFailure(call: Call<NamesAndLocationsItem>, t: Throwable) {
                    Log.d("main", "Could not Post data")
                }

            })

            binding.nameET.text.clear()
            binding.locationET.text.clear()
        }
    }
}