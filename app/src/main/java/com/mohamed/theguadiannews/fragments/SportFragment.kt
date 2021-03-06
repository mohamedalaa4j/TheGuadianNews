package com.mohamed.theguadiannews.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohamed.theguadiannews.Model
import com.mohamed.theguadiannews.MyAdapter
import com.mohamed.theguadiannews.R
import com.mohamed.theguadiannews.UrlOpener
import com.mohamed.theguadiannews.databinding.FragmentSportBinding
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SportFragment : Fragment(R.layout.fragment_sport) {
    private var binding: FragmentSportBinding? = null

    private var dataList = ArrayList<Model>()

    //region Variables

    var page = 1
    var totalPages = 1

    var isLoading = false
    val adapter = MyAdapter(dataList)
    var searchKeyword = ""
    //endregion


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportBinding.bind(view)

        ///// Stop automatic starting ShimmerEffect
        binding?.shimmerFrameLayout?.stopShimmer()
        binding?.shimmerFrameLayout?.visibility = View.INVISIBLE
        binding?.toolBar?.searchBar?.clearFocus()

        //region SearchView Listener
        binding?.toolBar?.searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding?.toolBar?.searchBar?.clearFocus()
                // Toast.makeText(context, binding?.toolBar?.searchBar?.query, Toast.LENGTH_SHORT).show()
                searchKeyword = binding?.toolBar?.searchBar?.query.toString()
                Log.i("search", searchKeyword)

                binding?.rv?.visibility = View.INVISIBLE
                binding?.shimmerFrameLayout?.startShimmer()
                binding?.shimmerFrameLayout?.visibility = View.VISIBLE
                apiCallSearch()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding?.shimmerFrameLayout?.stopShimmer()
                    binding?.shimmerFrameLayout?.visibility = View.GONE
                    binding?.rv?.visibility = View.VISIBLE
                }, 1000)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchKeyword = binding?.toolBar?.searchBar?.query.toString()
                Log.i("search", searchKeyword)
                //apiCallSearch()
                return false

            }
        })
        //endregion

        ///// Start ShimmerEffect with apiCall()
        binding?.shimmerFrameLayout?.startShimmer()
        binding?.shimmerFrameLayout?.visibility = View.VISIBLE

        apiCall()

        ///// Stop ShimmerEffect the response
        Handler(Looper.getMainLooper()).postDelayed({
            binding?.shimmerFrameLayout?.stopShimmer()
            binding?.shimmerFrameLayout?.visibility = View.GONE
            binding?.rv?.visibility = View.VISIBLE
        }, 1500)

        //region RecyclerView

        // Set the LayoutManager that this RecyclerView will use.
        val linearLayoutManager = LinearLayoutManager(context)
        binding?.rv?.layoutManager = linearLayoutManager
        // adapter instance is set to the recyclerview to inflate the items.
        //val adapter = MyAdapter(dataList)
        binding?.rv?.adapter = adapter

        /////region Endless RV code
        binding?.rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = linearLayoutManager.childCount
                    val pastVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading) {

                        if ((visibleItemCount + pastVisibleItem) >= total) {
                            //Toast.makeText(context, "end", Toast.LENGTH_SHORT).show()

                            if (totalPages > page) {
                                page++
                            } else {
                                isLoading = true
                            }
                            isLoading = true
                            // apiCall()
//                            binding?.rv?.visibility = View.INVISIBLE
//                            binding?.shimmerFrameLayout?.startShimmer()
//                            binding?.shimmerFrameLayout?.visibility = View.VISIBLE
                            apiCall()
//                            Handler(Looper.getMainLooper()).postDelayed({
//                                binding?.shimmerFrameLayout?.stopShimmer()
//                                binding?.shimmerFrameLayout?.visibility = View.GONE
//                                binding?.rv?.visibility = View.VISIBLE
//                            }, 1500)
                            isLoading = false
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        //endregion
        ///// Listener
        adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val newsUrl: String = dataList[position].webUrl

                // Url opener
                val intent = Intent(context, UrlOpener::class.java)
                intent.putExtra("url", newsUrl)
                startActivity(intent)


            }
        })
        //endregion

        binding?.toolBar?.tvCancel?.setOnClickListener {
            binding?.toolBar?.tvCancel?.clearFocus()
            binding?.toolBar?.searchBar?.setQuery("", false)
            dataList.clear()
            apiCall()
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onResume() {
        super.onResume()
        binding?.rv?.visibility = View.INVISIBLE
        binding?.shimmerFrameLayout?.startShimmer()
        binding?.shimmerFrameLayout?.visibility = View.VISIBLE
        //  apiCall()
        Handler(Looper.getMainLooper()).postDelayed({
            binding?.shimmerFrameLayout?.stopShimmer()
            binding?.shimmerFrameLayout?.visibility = View.GONE
            binding?.rv?.visibility = View.VISIBLE
        }, 800)


    }


    private fun apiCall() {

        val queue = Volley.newRequestQueue(context)

        val url =
            // "https://content.guardianapis.com/search?q=&show-fields=thumbnail&order-by=newest&page=${page}&api-key=43f906b3-25e9-4762-9233-29811f58038b"
            "https://content.guardianapis.com/search?section=sport&show-fields=thumbnail&order-by=newest&page=$page&api-key=43f906b3-25e9-4762-9233-29811f58038b"

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

    ///// Api Call with user's search keyword
    private fun apiCallSearch() {

        val queue = Volley.newRequestQueue(context)

        val url =
            "https://content.guardianapis.com/search?q=$searchKeyword&section=sport&show-fields=thumbnail&section=sport&order-by=newest&page-size=50&api-key=43f906b3-25e9-4762-9233-29811f58038b"
        Log.i("link", url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                parseSearch(response.toString())

            },
            { error ->
                Log.e("api", error.toString())
                Toast.makeText(context, "Error getting the data!", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }


    private fun parse(data: String) {

        //dataList.clear()

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

            // webUrl
            val webUrl = jsonArray.getJSONObject(i).getString("webUrl")
            Log.i("webUrl ", webUrl)

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


            dataList.add(Model(webTitle, date, thumbnail, webUrl))
            adapter.notifyDataSetChanged()

            // rvSetup()

        }

    }

    ///// parse function for apiCallSearch()
    private fun parseSearch(data: String) {

        dataList.clear()

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

            // webUrl
            val webUrl = jsonArray.getJSONObject(i).getString("webUrl")
            Log.i("webUrl ", webUrl)

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


            dataList.add(Model(webTitle, date, thumbnail, webUrl))
            adapter.notifyDataSetChanged()

            // rvSetup()

        }

    }


    private fun parseDate(date: String): String {

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val mDate = formatter.parse(date)
        return mDate!!.toString().take(10)

    }
}