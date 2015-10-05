package com.muhil.zohokart.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.muhil.zohokart.models.Account;
import com.muhil.zohokart.models.Category;
import com.muhil.zohokart.models.Product;
import com.muhil.zohokart.models.SubCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "zohocart", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "Creating tables");
        db.execSQL("create table categories (_id integer primary key not null, name text)");
        db.execSQL("create table sub_categories (_id integer primary key not null, category_id integer, name text)");
        db.execSQL("create table products (_id integer, category_id integer, brand text, title text, description text, thumbnail text, price real, stars real, ratings integer, primary key (_id, category_id))");
        db.execSQL("create table accounts(name text, email text not null primary key, password text, phone_number text, date_of_birth date)");
        Log.d("DB", "accounts table created");
        db.execSQL("create table wishlist (_id integer not null primary key, added_on datetime default current_timestamp)");
        db.execSQL("create table cart (_id integer not null primary key, quantity integer default 1, added_on datetime default current_timestamp)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "Dropping tables");
        db.execSQL("drop table if exists categories");
        db.execSQL("drop table if exists sub_categories");
    }

    public int addCategories(List<Category> categories) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (Category category : categories) {
            ContentValues contentValue = new ContentValues();
            contentValue.put("_id", category.getId());
            contentValue.put("name", category.getName());
            sqLiteDatabase.insert("categories", null, contentValue);
        }
        sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, "categories");
    }

    public int addSubCategories(List<SubCategory> subCategories) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (SubCategory subCategory : subCategories) {
            ContentValues contentValue = new ContentValues();
            contentValue.put("_id", subCategory.getId());
            contentValue.put("category_id", subCategory.getCategoryId());
            contentValue.put("name", subCategory.getName());
            sqLiteDatabase.insert("sub_categories", null, contentValue);
        }
        sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, "sub_categories");
    }

    public int addProducts(List<Product> products) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (Product product : products) {
            ContentValues contentValue = new ContentValues();
            contentValue.put("_id", product.getId());
            contentValue.put("category_id", product.getCategoryId());
            contentValue.put("brand", product.getBrand());
            contentValue.put("title", product.getTitle());
            contentValue.put("description", product.getDescription());
            contentValue.put("thumbnail", product.getThumbnail());
            contentValue.put("price", product.getPrice());
            contentValue.put("stars", product.getStars());
            contentValue.put("ratings", product.getRatings());

            sqLiteDatabase.insert("products", null, contentValue);
        }
        sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, "products");
    }


    public boolean hasAccount(String email) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from accounts where email = ?",
                new String[]{email});
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }

    public boolean addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        contentValues.put("name", account.getName());
        contentValues.put("email", account.getEmail());
        contentValues.put("password", account.getPassword());
        contentValues.put("phone_number", account.getPhoneNumber());
        contentValues.put("date_of_birth", account.getDateOfBirth());
        sqLiteDatabase.insert("accounts", null, contentValues);

        Cursor cursor = sqLiteDatabase.rawQuery("select * from accounts where email = ?",
                new String[]{account.getEmail()});
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }

    public Account getAccountIfAvailable(String email, String password) {
        Account account = new Account();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from accounts where email = ? " +
                "and password = ? ", new String[]{email, password});
        if (cursor.getCount() == 1) {

            while (cursor.moveToNext()) {
                account.setName(cursor.getString(0));
                account.setEmail(email);
                account.setPassword(password);
                account.setPhoneNumber(cursor.getString(3));
                account.setDateOfBirth(cursor.getString(4));
            }

        } else {
            return null;
        }

        cursor.close();
        return account;
    }

    public boolean checkWishlist(int productId) {
        boolean result;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from wishlist where _id = ?",
                new String[]{String.valueOf(productId)});
        result = cursor.getCount() == 1;
        cursor.close();
        return result;

    }

    public boolean checkInCart(int productId) {
        boolean result;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from cart where _id = ?",
                new String[]{String.valueOf(productId)});
        result = cursor.getCount() == 1;
        cursor.close();
        return result;
    }

    public boolean addToWishlist(int productId) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        contentValues.put("_id", productId);
        Long rowID = sqLiteDatabase.insert("wishlist", null, contentValues);
        return rowID != -1;
    }

    public boolean addToCart(int productId) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        contentValues.put("_id", productId);
        Long rowID = sqLiteDatabase.insert("cart", null, contentValues);
        return rowID != -1;
    }

    public boolean removeFromWishList(int productId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int rowsAffected = sqLiteDatabase.delete("wishlist", "_id = ? ", new String[]{String.valueOf(productId)});
        return rowsAffected != -1;
    }

    public boolean removeFromCart(int productId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int rowsAffected = sqLiteDatabase.delete("cart", "_id = ? ", new String[]{String.valueOf(productId)});
        return rowsAffected != -1;
    }

    public int numberOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, "categories");
    }

    public List<Category> getCategories() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select _id, name from categories", null);
        List<Category> categories = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Category category = new Category(id, name);
            categories.add(category);
        }
        cursor.close();
        return categories;
    }

    public Map<Integer, List<SubCategory>> getSubCategoriesByCategory() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select _id, category_id, name from sub_categories", null);
        Map<Integer, List<SubCategory>> subCategoriesBycategory = new HashMap<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            SubCategory subCategory = new SubCategory(id, categoryId, name);
            if (subCategoriesBycategory.get(categoryId) != null) {
                subCategoriesBycategory.get(categoryId).add(subCategory);
            } else {
                List<SubCategory> subCategories = new ArrayList<>();
                subCategories.add(subCategory);
                subCategoriesBycategory.put(categoryId, subCategories);
            }

        }
        cursor.close();
        return subCategoriesBycategory;
    }

    public List<Product> getProductsForSubCategory(int subCategoryId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select _id, category_id, brand, title, description, thumbnail," +
                        " price, stars, ratings from products where category_id = ? ",
                new String[]{String.valueOf(subCategoryId)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String brand = cursor.getString(2);
            String title = cursor.getString(3);
            String description = cursor.getString(4);
            String thumbnail = cursor.getString(5);
            double price = cursor.getDouble(6);
            double stars = cursor.getDouble(7);
            Log.d("STARS", String.valueOf(stars));
            int ratings = cursor.getInt(8);

            Product product = new Product(id, categoryId, brand, title, description, thumbnail, price, stars, ratings);
            products.add(product);
        }
        cursor.close();
        return products;
    }

    public List<Product> getProductsFromWishList() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from wishlist order by added_on asc", null);
        Log.d("WISHLIST_CURSOR", String.valueOf(cursor.getCount()));
        List<Integer> productsInWishList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            productsInWishList.add(id);
        }
        if (!productsInWishList.isEmpty()) {
            for (Integer productId : productsInWishList) {
                cursor = sqLiteDatabase.rawQuery("select _id, category_id, brand, title, description, thumbnail, " +
                        "price, stars, ratings from products where _id = ?", new String[]{String.valueOf(productId)});
                Log.d("WISHLIST_CURSOR", String.valueOf(cursor.getCount()));
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    int categoryId = cursor.getInt(1);
                    String brand = cursor.getString(2);
                    String title = cursor.getString(3);
                    String description = cursor.getString(4);
                    String thumbnail = cursor.getString(5);
                    double price = cursor.getDouble(6);
                    double stars = cursor.getDouble(7);
                    int ratings = cursor.getInt(8);

                    Product product = new Product(id, categoryId, brand, title, description, thumbnail, price, stars, ratings);
                    products.add(product);
                }

            }
        } else {
            return null;
        }
        cursor.close();
        return products;
    }

    public List<Product> getProductsFromCart() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from cart order by added_on asc", null);
        Log.d("CART_CURSOR", String.valueOf(cursor.getCount()));
        List<Integer> productsInCart = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            productsInCart.add(id);
        }
        if (!productsInCart.isEmpty()) {
            for (Integer productId : productsInCart) {
                cursor = sqLiteDatabase.rawQuery("select _id, category_id, brand, title, description, thumbnail, " +
                        "price, stars, ratings from products where _id = ?", new String[]{String.valueOf(productId)});
                Log.d("CART_CURSOR", String.valueOf(cursor.getCount()));
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    int categoryId = cursor.getInt(1);
                    String brand = cursor.getString(2);
                    String title = cursor.getString(3);
                    String description = cursor.getString(4);
                    String thumbnail = cursor.getString(5);
                    double price = cursor.getDouble(6);
                    double stars = cursor.getDouble(7);
                    int ratings = cursor.getInt(8);

                    Product product = new Product(id, categoryId, brand, title, description, thumbnail, price, stars, ratings);
                    products.add(product);
                }

            }
        } else {
            return null;
        }
        cursor.close();
        return products;
    }
}
