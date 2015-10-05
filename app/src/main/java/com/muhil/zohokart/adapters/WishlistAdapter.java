package com.muhil.zohokart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.muhil.zohokart.R;
import com.muhil.zohokart.fragments.WishlistFragment;
import com.muhil.zohokart.models.Product;
import com.muhil.zohokart.utils.DBHelper;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by muhil-ga42 on 04/10/15.
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    List<Product> wishlist;
    Context context;
    WishlistFragment wishlistFragment;
    DBHelper dbHelper;
    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public WishlistAdapter(Context context, List<Product> wishlist, WishlistFragment wishlistFragment) {
        this.context = context;
        this.wishlistFragment = wishlistFragment;
        this.wishlist = wishlist;
        dbHelper = new DBHelper(context);
    }

    @Override
    public WishlistAdapter.WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.wishlist_item_row, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WishlistAdapter.WishlistViewHolder holder, int position) {

        holder.removeProductView.setTag(wishlist.get(position));
        holder.addToCart.setTag(wishlist.get(position));
        holder.title.setText(wishlist.get(position).getTitle());
        holder.description.setText(wishlist.get(position).getDescription());
        holder.price.setText("Rs. " + String.valueOf(decimalFormat.format(wishlist.get(position).getPrice())));
        Picasso.with(context).load(wishlist.get(position).getThumbnail()).into(holder.displayImage);

        if (dbHelper.checkInCart(wishlist.get(position).getId())){

            holder.addToCart.setVisibility(View.GONE);
            holder.goToCart.setVisibility(View.VISIBLE);

        }

        holder.removeProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = (Product) v.getTag();
                if (dbHelper.removeFromWishList(product.getId())) {
                    Toast.makeText(context, "Product removed from wishlist", Toast.LENGTH_SHORT).show();
                    int position = wishlist.indexOf(product);
                    wishlist.remove(position);
                    notifyItemRemoved(position);
                    if (wishlist.size() == 0) {
                        wishlistFragment.switchViewElement();
                    }
                }
                else {
                    Toast.makeText(context, "error while removing from wishlist.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = (Product) v.getTag();
                if (dbHelper.addToCart(product.getId())) {
                    Toast.makeText(context, "product added to cart.", Toast.LENGTH_SHORT).show();
                    v.setVisibility(View.GONE);
                    holder.goToCart.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "error while adding to cart.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    class WishlistViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, description;
        ImageView displayImage;
        FrameLayout removeProductView, addToCart, goToCart;

        public WishlistViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
            displayImage = (ImageView) itemView.findViewById(R.id.displayImage);
            removeProductView = (FrameLayout) itemView.findViewById(R.id.removeProduct);
            addToCart = (FrameLayout) itemView.findViewById(R.id.add_to_cart_action);
            goToCart = (FrameLayout) itemView.findViewById(R.id.go_to_cart_action);


        }
    }

}
