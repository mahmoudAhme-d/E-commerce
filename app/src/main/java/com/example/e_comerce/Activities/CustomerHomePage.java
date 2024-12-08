package com.example.e_comerce.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.DatabaseAccess.DbAccsessCategory;
import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.JavaClasses.CategoryAdapter;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.ProductAdapter;
import com.example.e_comerce.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomePage extends AppCompatActivity {



    private ListView productListView;
    private ProductAdapter productAdapter;
    private List<Category> ListOfCateogry;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        productListView = findViewById(R.id.productListView);
        searchEditText = findViewById(R.id.searchEditText);
        DbAccsessCategory dbAccessProduct = new DbAccsessCategory(this);

        ListOfCateogry=  dbAccessProduct.getAllCategoriesWithProducts();


        if(ListOfCateogry.isEmpty())
        {

            Toast.makeText(CustomerHomePage.this, "is empty ", Toast.LENGTH_SHORT).show();

        }
        else
        {
            // Set up category RecyclerView
            CategoryAdapter categoryAdapter = new CategoryAdapter(this, ListOfCateogry, position -> {
                // Show products for the selected category
                List<Product> selectedProducts = ListOfCateogry.get(position).Products;
                updateProductList(selectedProducts);
            });

            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            categoryRecyclerView.setAdapter(categoryAdapter);

            // Set initial product list to the first category

            updateProductList(ListOfCateogry.get(0).Products);

        }


        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Update the product list in the ListView
    private void updateProductList(List<Product> products) {
        productAdapter = new ProductAdapter(this, products);
        productListView.setAdapter(productAdapter);
    }

    // Filter products across all categories based on the search query
    private void filterProducts(String query) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Category category : ListOfCateogry) {
            for (Product product : category.Products) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }

        updateProductList(filteredProducts); // Update the ListView with filtered results
    }


}