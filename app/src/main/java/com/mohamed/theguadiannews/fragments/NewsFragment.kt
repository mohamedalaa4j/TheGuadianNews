package com.mohamed.theguadiannews.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohamed.theguadiannews.Model
import com.mohamed.theguadiannews.MyAdapter
import com.mohamed.theguadiannews.R
import com.mohamed.theguadiannews.databinding.FragmentNewsBinding
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewsFragment : Fragment(R.layout.fragment_news) {
    private var binding: FragmentNewsBinding? = null

    private var dataList = ArrayList<Model>()

    var page = 1
    var totalPages = 1
    var isLoading = false
    val adapter = MyAdapter(dataList)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        apiCall()

        //region RecyclerView

        // Set the LayoutManager that this RecyclerView will use.
        val linearLayoutManager = LinearLayoutManager(context)
        binding?.rv?.layoutManager = linearLayoutManager
        // adapter instance is set to the recyclerview to inflate the items.
        //val adapter = MyAdapter(dataList)
        binding?.rv?.adapter = adapter

        /////region Endless RV code
        binding?.rv?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0){
                    val visibleItemCount = linearLayoutManager.childCount
                    val pastVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading){

                        if ((visibleItemCount + pastVisibleItem) >= total){
                            //Toast.makeText(context, "end", Toast.LENGTH_SHORT).show()

                            if (totalPages > page) {
                                page++
                            }else{isLoading = true}
                            isLoading = true
                            apiCall()
                            isLoading = false
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        //endregion
        //endregion
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun apiCall() {

        val queue = Volley.newRequestQueue(context)

        val url =
            "https://content.guardianapis.com/search?q=&show-fields=thumbnail&order-by=newest&page=${page}&api-key=43f906b3-25e9-4762-9233-29811f58038b"

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

        val pages = responseObjectAsJsonObject.getString("pages")
        totalPages = pages.toInt()

        val jsonArray = responseObjectAsJsonObject.getJSONArray("results")

        for (i in 0 until jsonArray.length()) {

            // webTitle
            val webTitle = jsonArray.getJSONObject(i).getString("webTitle")
            Log.i("webTitle ", webTitle)

            // webPublicationDate
            val webPublicationDate = jsonArray.getJSONObject(i).getString("webPublicationDate")
            Log.i("date", webPublicationDate)
            val date = parseDate(webPublicationDate)

            var thumbnail = ""
            try {
                val fields = jsonArray.getJSONObject(i).getJSONObject("fields")
                thumbnail = fields.getString("thumbnail")
                Log.i("tt", thumbnail)
            } catch (e: Exception) {
            }

            dataList.add(Model(webTitle, date, thumbnail))

            adapter.notifyDataSetChanged()
           // rvSetup()

        }
    }

//    private fun rvSetup() {
//
//
//        // Set the LayoutManager that this RecyclerView will use.
//        val linearLayoutManager = LinearLayoutManager(context)
//        binding?.rv?.layoutManager = linearLayoutManager
//        // adapter instance is set to the recyclerview to inflate the items.
//        val adapter = MyAdapter(dataList)
//        binding?.rv?.adapter = adapter
//
//        /////////
//        binding?.rv?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0){
//                    val visibleItemCount = linearLayoutManager.childCount
//                    val pastVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
//                    val total = adapter.itemCount
//
//                    if (!isLoading){
//
//                        if ((visibleItemCount + pastVisibleItem) >= total){
//                            Toast.makeText(context, "end", Toast.LENGTH_SHORT).show()
//
//                            if (totalPages > page) {
//                                page++
//                            }else{isLoading = true}
//                            isLoading = true
//                            apiCall()
//                            isLoading = false
//                        }
//                    }
//                }
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })
//        /////////
//
//
//    }

    private fun parseDate(date: String): String {

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
       val mDate = formatter.parse(date)
      //  Log.i("datep", mDate.toString())
        return mDate!!.toString().take(10)

    }

}