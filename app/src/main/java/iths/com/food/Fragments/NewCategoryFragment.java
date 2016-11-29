package iths.com.food.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import iths.com.food.Helper.ImageAdapter;
import iths.com.food.Model.Category;
import iths.com.food.R;





public class NewCategoryFragment extends Fragment {


    public static ViewPager mViewPager;
    EditText addCategoryName;
    ImageAdapter adapterView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_add_category, container, false);


        addCategoryName = (EditText) v.findViewById(R.id.add_category_editText);
        mViewPager = (ViewPager) v.findViewById(R.id.viewPageAndroid);
        adapterView = new ImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);




        return v;
    }
}

