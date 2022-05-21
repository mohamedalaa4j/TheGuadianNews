package com.mohamed.theguadiannews.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohamed.theguadiannews.Model
import com.mohamed.theguadiannews.MyAdapter
import com.mohamed.theguadiannews.R
import com.mohamed.theguadiannews.databinding.FragmentNewsBinding
import org.json.JSONObject
import org.json.JSONTokener


class NewsFragment : Fragment(R.layout.fragment_news) {
    private var binding: FragmentNewsBinding? = null

    private var dataList = ArrayList<Model>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        apiCall()
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun apiCall() {

        val queue = Volley.newRequestQueue(context)

        val url =
            "https://content.guardianapis.com/search?q=&show-fields=thumbnail&order-by=newest&api-key=43f906b3-25e9-4762-9233-29811f58038b"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                parse(response.toString())

            },
            { error ->
                Log.e("api", error.toString())
                Toast.makeText(context, "Error getting the data!", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }

    private fun parse(data: String) {


        val jsonObject = JSONTokener(data).nextValue() as JSONObject
        val responseObject = jsonObject.getString("response")

        val responseObjectAsJsonObject = JSONTokener(responseObject).nextValue() as JSONObject

        val jsonArray = responseObjectAsJsonObject.getJSONArray("results")
        Log.i("ss", jsonArray.toString())

        for (i in 0 until jsonArray.length()) {

            // webTitle
            val webTitle = jsonArray.getJSONObject(i).getString("webTitle")
            Log.i("webTitle ", webTitle)

            val fields = jsonArray.getJSONObject(i).getString("fields")
            val fieldsJsonObject = JSONTokener(fields).nextValue() as JSONObject

            // thumbnail
            val thumbnail = fieldsJsonObject.getString("thumbnail")
            Log.i("thumbnail ", thumbnail)

            dataList.add(Model(webTitle,thumbnail))

            rvSetup()

        }
    }

    private fun rvSetup() {


        // Set the LayoutManager that this RecyclerView will use.
        binding?.rv?.layoutManager = LinearLayoutManager(context)
        // adapter instance is set to the recyclerview to inflate the items.
        val adapter = MyAdapter(dataList)
        binding?.rv?.adapter = adapter
    }
}