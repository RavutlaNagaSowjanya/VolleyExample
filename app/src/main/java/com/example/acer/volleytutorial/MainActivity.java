package com.example.acer.volleytutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.volleytutorial.Adapters.BookAdapter;
import com.example.acer.volleytutorial.model.BookModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    RequestQueue requestQueue;
    String bookurl="https://www.googleapis.com/books/v1/volumes?q=android";
    String book_title,book_desc,book_author,book_image;
    ArrayList<BookModel> bookArrayList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler);

        requestQueue=Volley.newRequestQueue(this);
        bookArrayList=new ArrayList<>();
        fetchData();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));



    }
    public void fetchData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, bookurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               //Toast.makeText(MainActivity.this, "positive response\n" + response, Toast.LENGTH_SHORT).show();

                parseData(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "error,no data available", Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(stringRequest);
    }

    public void parseData(String response)
    {
        try
        {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("items");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object=jsonArray.getJSONObject(i);
                JSONObject volumeInfo=object.getJSONObject("volumeInfo");
                book_title=volumeInfo.optString("title");
                book_desc=volumeInfo.optString("description");
                JSONArray authors=volumeInfo.getJSONArray("authors");
                book_author=authors.getString(0);
                JSONObject imageLinks=volumeInfo.getJSONObject("imageLinks");
                book_image=imageLinks.optString("thumbnail");

                BookModel bookModel=new BookModel(book_author,book_image,book_desc,book_title);
              //  Toast.makeText(this, book_author+"\n"+book_title+"\n"+book_desc, Toast.LENGTH_LONG).show();
                bookArrayList.add(bookModel);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        BookAdapter bookAdapter=new BookAdapter(this,bookArrayList);
        recyclerView.setAdapter(bookAdapter);

    }
}
