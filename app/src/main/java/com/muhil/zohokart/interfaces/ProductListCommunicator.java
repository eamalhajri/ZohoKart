package com.muhil.zohokart.interfaces;

import com.muhil.zohokart.models.Product;

import java.util.List;

/**
 * Created by muhil-ga42 on 21/10/15.
 */
public interface ProductListCommunicator
{
    void openFilter();
    void showProductDetailFragment(int position, List<Product> products);
    void showMainFragment();
    void releaseDrawer();
    void invalidateOptions();
    int getCurrentItemPosition();
}