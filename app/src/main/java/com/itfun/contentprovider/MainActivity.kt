package com.itfun.contentprovider

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    var cols = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, Array(1){Manifest.permission.READ_CONTACTS}, 111)
        }else{
            readContact()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            readContact()
    }

    private fun readContact() {
        var from = listOf<String>(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER).toTypedArray()
        var to = intArrayOf(android.R.id.text1, android.R.id.text2)
        var rs = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        var adapter = SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, rs, from, to, 0)

        val listview = findViewById<ListView>(R.id.listview)
        listview.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                rs = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, "${  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
                    Array(1){"%$newText%"},
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                adapter.changeCursor(rs)
                return true
            }
        })


    }
}