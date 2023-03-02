
package com.example.realtimedatabasereusuablecodedoc


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantViewEdit : AppCompatActivity() {
    private var TAG: String? = null
    private lateinit var database: DatabaseReference
    private var restaurantMenu: Menu? = null
    private lateinit var rv: RecyclerView
    private lateinit var msAdapter: LocationViewEditMultiselectAdapter
    //private var restaurantArrayList: ArrayList<RestaurantDC> = ArrayList<RestaurantDC>()
    private lateinit var restaurantArrayList: ArrayList<RestaurantDC>
    var onComplete : (()-> Unit)? = null
    var first : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_view_edit)

        rv = findViewById(R.id.restaurantList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        //Toast.makeText(this@RestaurantViewEdit, "size of arraylist is: " +restaurantArrayList.size, Toast.LENGTH_SHORT).show()
        //getUserData()

        //menuItemRecyclerView = findViewById(R.id.menuItemList)
        //menuItemRecyclerView.layoutManager = LinearLayoutManager(this)
        //menuItemRecyclerView.setHasFixedSize(true)
        restaurantArrayList = arrayListOf<RestaurantDC>()
        getUserData()


        first = 0

        /*
        val rv: RecyclerView = findViewById(R.id.restaurantList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        msAdapter = LocationViewEditMultiselectAdapter(restaurantArrayList){ show -> showDeleteMenu(show)}
        rv.adapter = msAdapter
        */


        //menuItemArrayList = arrayListOf<MenuItemDC>()
        //getUserData()


        /*
        val sample: RestaurantDC = RestaurantDC("sample1", "sample", "sample", "sample", "sample", "sample")
        val sample2: RestaurantDC = RestaurantDC("sample2", "sample", "sample", "sample", "sample", "sample")
        restaurantArrayList.add(sample)
        restaurantArrayList.add(sample2)



        if (restaurantArrayList.isNotEmpty()){
            Toast.makeText(this@RestaurantViewEdit, "size of arraylist now is: " +restaurantArrayList.size, Toast.LENGTH_SHORT).show()
            for (i in restaurantArrayList){
                val id :String? = i.restaurantID
                Toast.makeText(this@RestaurantViewEdit, "size of arraylist now is: " +restaurantArrayList.size, Toast.LENGTH_SHORT).show()
                Toast.makeText(this@RestaurantViewEdit, id, Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this@RestaurantViewEdit, "it is empty yo", Toast.LENGTH_SHORT).show()
        }
        */

            //Toast.makeText(this@RestaurantViewEdit, "size of arraylist now is: " +restaurantArrayList.size, Toast.LENGTH_SHORT).show()


        /*
        Toast.makeText(this@RestaurantViewEdit, "right after size", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this@RestaurantViewEdit, "size of arraylist now is: " +restaurantArrayList.size, Toast.LENGTH_SHORT).show()
        Toast.makeText(this@RestaurantViewEdit,"this should pop last", Toast.LENGTH_SHORT).show()
        msAdapter = LocationViewEditMultiselectAdapter(restaurantArrayList){ show -> showDeleteMenu(show)}
        val rv: RecyclerView = findViewById(R.id.restaurantList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = msAdapter
        */



    }


    private fun getUserData(){
        //delay(1000L)
        database = FirebaseDatabase.getInstance().getReference("Users/Restaurants/")
        //Toast.makeText(this@RestaurantViewEdit, "Did getUser at least run?", Toast.LENGTH_SHORT).show()


        //if (first == 1) {

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //Toast.makeText(this@RestaurantViewEdit, "children #: " + snapshot.childrenCount, Toast.LENGTH_SHORT).show()
                    restaurantArrayList.clear()
                    for (menuItemSnapshot in snapshot.children) {
                        val menuItem = menuItemSnapshot.getValue(RestaurantDC::class.java)
                        //Toast.makeText(this@RestaurantViewEdit, menuItem!!.name.toString(), Toast.LENGTH_SHORT).show()
                        restaurantArrayList.add(menuItem!!)


                        /*
                        if (happened){
                            Toast.makeText(this@RestaurantViewEdit, "yay", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this@RestaurantViewEdit, restaurantArrayList.get(0).name, Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@RestaurantViewEdit, "off with yer head", Toast.LENGTH_SHORT).show()
                        }

                         */

                    }
                    //Toast.makeText(this@RestaurantViewEdit, "size:" + restaurantArrayList.size.toString(), Toast.LENGTH_SHORT).show()
                    //val intent = Intent(this@RestaurantViewEdit, RestaurantViewEdit::class.java)
                    //startActivity(intent)
                    msAdapter =
                        LocationViewEditMultiselectAdapter(restaurantArrayList) { show ->
                            showDeleteMenu(show)
                        }
                    rv.adapter = msAdapter
                }
                //Toast.makeText(this@RestaurantViewEdit, "partial method complete", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
       // }

        //Toast.makeText(this@RestaurantViewEdit, "complete method complete", Toast.LENGTH_SHORT).show()
    }

    /*
    fun showDelete(show: Boolean){
        restaurantMenu?.findItem(R.id.delete)?.isVisible = show
    }

    fun showEdit(show: Boolean){
        restaurantMenu?.findItem(R.id.edit)?.isVisible = show
    }

     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        restaurantMenu = menu
        menuInflater.inflate(R.menu.view_edit_menu,restaurantMenu)
        showDeleteMenu(false)
        //showEdit(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "part a")
        when (item.itemId){
            R.id.delete -> { delete() }
            //Log.d(TAG, "part b")



        }
        return super.onOptionsItemSelected(item)
    }

    fun showDeleteMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.delete)?.isVisible = show
    }

    private fun delete(){
        Log.d(TAG,"partb")
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Location(s)")
        alertDialog.setMessage("Do you want to delete these locations?")
        alertDialog.setPositiveButton("Delete"){_,_ ->
            msAdapter.deleteSelectedItem()
            showDeleteMenu(false)
        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }




}
