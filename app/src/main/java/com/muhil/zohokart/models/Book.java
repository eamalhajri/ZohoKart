package com.muhil.zohokart.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by muhil-ga42 on 19/10/15.
 */
public class Book implements IProduct
{

    private int id;
    @SerializedName("sub_category_id")
    private int categoryId;
    private String name;
    private String publisher;
    private String author;
    private String binding;
    private double price;
    private String thumbnail;
    private double stars;
    private int ratings;

    public static Map<String, Map<String, FilterPair>> FILTER_OPTIONS;
    public static Map<String, FilterPair> FILTER_OPTIONS_GROUP;
    public static List<String> SELECTION_ARGS;

    static
    {
        FILTER_OPTIONS_GROUP = new LinkedHashMap<>();
        FILTER_OPTIONS = new LinkedHashMap<>();

        SELECTION_ARGS = new ArrayList<>();
        SELECTION_ARGS.add(String.valueOf(1000));
        FILTER_OPTIONS_GROUP.put("Rs. 1000 and below", new FilterPair(Product.FILTER_PRICE_LESSER_THAN, SELECTION_ARGS));

        SELECTION_ARGS = new ArrayList<>();
        SELECTION_ARGS.add(String.valueOf(1001));
        SELECTION_ARGS.add(String.valueOf(5000));
        FILTER_OPTIONS_GROUP.put("Rs. 1001 - Rs. 5000", new FilterPair(Product.FILTER_PRICE_RANGE, SELECTION_ARGS));

        SELECTION_ARGS = new ArrayList<>();
        SELECTION_ARGS.add(String.valueOf(5001));
        SELECTION_ARGS.add(String.valueOf(10000));
        FILTER_OPTIONS_GROUP.put("Rs. 5001 - Rs. 10000", new FilterPair(Product.FILTER_PRICE_RANGE, SELECTION_ARGS));

        SELECTION_ARGS = new ArrayList<>();
        SELECTION_ARGS.add(String.valueOf(10001));
        FILTER_OPTIONS_GROUP.put("Rs. 10001 and above", new FilterPair(Product.FILTER_PRICE_GREATER_THAN, SELECTION_ARGS));

        FILTER_OPTIONS.put("Price", FILTER_OPTIONS_GROUP);

    }

    public Book()
    {
    }

    public Book(int id, int categoryId, String name, String publisher, String author, String binding, double price, String thumbnail, double stars, int ratings)
    {
        this.author = author;
        this.binding = binding;
        this.categoryId = categoryId;
        this.id = id;
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.ratings = ratings;
        this.stars = stars;
        this.thumbnail = thumbnail;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public int getCategoryId()
    {
        return categoryId;
    }

    @Override
    public String getBrand()
    {
        return publisher;
    }

    @Override
    public String getTitle()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return author + ", " + binding;
    }

    @Override
    public String getThumbnail()
    {
        return thumbnail;
    }

    @Override
    public double getPrice()
    {
        return price;
    }

    @Override
    public double getStars()
    {
        return stars;
    }

    @Override
    public int getRatings()
    {
        return ratings;
    }

    @Override
    public String getWarranty()
    {
        return null;
    }

    @Override
    public Product getProduct()
    {
        return new Product(this.getId(), this.getCategoryId(), this.getBrand(),
                this.getTitle(), this.getDescription(), this.getThumbnail(), this.getPrice(),
                this.getStars(), this.getRatings(), this.getWarranty());
    }
}
