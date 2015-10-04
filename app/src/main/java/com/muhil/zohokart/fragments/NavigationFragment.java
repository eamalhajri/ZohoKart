package com.muhil.zohokart.fragments;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.muhil.zohokart.R;
import com.muhil.zohokart.models.Category;
import com.muhil.zohokart.models.SubCategory;
import com.muhil.zohokart.utils.DBHelper;

import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment {

    LinearLayout menuLinearLayout, subCategoriesMenu;
    TextView subCategoryName, categoryName;
    View subCategoryMenuItem,categoryMenuItem;
    ToggleButton dropdown;
    List<Category> categories;
    List<SubCategory> subCategories;
    Map<Integer, List<SubCategory>> subCategoriesByCategory;
    Communicator communicator;

    public NavigationFragment() {
        // Required empty public constructor
    }

    public void setCommunicator(Communicator communicator){
        this.communicator = communicator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        menuLinearLayout = (LinearLayout) view.findViewById(R.id.navigation_linear_layout);
        DBHelper dbHelper = new DBHelper(getActivity());
        categories = dbHelper.getCategories();
        Log.d("NAV", "number of categories from db = " + categories.size());
        subCategoriesByCategory = dbHelper.getSubCategoriesByCategory();
        Log.d("NAV", "number of sub-categories from db = " + subCategoriesByCategory.size());
        for (Map.Entry<Integer, List<SubCategory>> entry : subCategoriesByCategory.entrySet()) {
            List<SubCategory> subCategories = entry.getValue();
            for (SubCategory subCategory : subCategories) {
                Log.d("NAV", subCategory.toString());
            }
        }
        for (Category category : categories) {

            categoryMenuItem = View.inflate(getActivity(), R.layout.navigation_menu_row, null);
            categoryName = (TextView) categoryMenuItem.findViewById(R.id.categoryName);
            categoryName.setText(category.getName());

            if ((subCategories = subCategoriesByCategory.get(category.getId())) != null){
                subCategoriesMenu = new LinearLayout(getActivity());
                subCategoriesMenu.setOrientation(LinearLayout.VERTICAL);
                subCategoriesMenu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                subCategoriesMenu.setVisibility(View.GONE);
                for (SubCategory subCategory : subCategories) {
                    subCategoryMenuItem = View.inflate(getActivity(), R.layout.navigation_menu_item_row, null);
                    subCategoryName = (TextView) subCategoryMenuItem.findViewById(R.id.subCategoryItem);
                    subCategoryName.setText(subCategory.getName());
                    subCategoryMenuItem.setTag(subCategory);
                    subCategoryMenuItem.setId(subCategory.getId());

                    subCategoryMenuItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SubCategory subCategory = (SubCategory) v.getTag();
                            Toast.makeText(getActivity(), "Sub-category id: " + subCategory.getId(), Toast.LENGTH_SHORT).show();
                            communicator.closeDrawer();
                        }
                    });

                    subCategoriesMenu.addView(subCategoryMenuItem);
                }
                categoryMenuItem.setTag(subCategoriesMenu);

                categoryMenuItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final LinearLayout subMenuTag = (LinearLayout) v.getTag();
                        if (subMenuTag.getVisibility() == View.GONE) {

                            expand(subMenuTag);
                            dropdown = (ToggleButton) v.findViewById(R.id.dropdown);
                            dropdown.setChecked(true);


                        } else if (subMenuTag.getVisibility() == View.VISIBLE) {

                            collapse(subMenuTag);
                            dropdown = (ToggleButton) v.findViewById(R.id.dropdown);
                            dropdown.setChecked(false);

                        }

                    }
                });

                menuLinearLayout.addView(categoryMenuItem);
                menuLinearLayout.addView(subCategoriesMenu);

            }
        }

    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }


        };

        // 1dp/ms
        a.setDuration(500);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(500);
        v.startAnimation(a);
    }

    public interface Communicator {
        void closeDrawer();
    }

}
