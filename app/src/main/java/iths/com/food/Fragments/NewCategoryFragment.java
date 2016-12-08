package iths.com.food.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Helper.ImageAdapter;
import iths.com.food.R;

public class NewCategoryFragment extends Fragment {

    private ViewPager mViewPager;
    EditText addCategoryName;
    ImageAdapter adapterView;
    DatabaseHelper db;
    ViewGroup v;
    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveButtonPressed();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_add_category, container, false);

        Button b = (Button) v.findViewById(R.id.save_button);
        b.setOnClickListener(saveButtonListener);

        addCategoryName = (EditText) v.findViewById(R.id.add_category_editText);
        mViewPager = (ViewPager) v.findViewById(R.id.viewPageAndroid);
        adapterView = new ImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);

        return v;
    }
    public void saveButtonPressed() {

        db = new DatabaseHelper(getActivity());

        EditText etCategoryName = (EditText) v.findViewById(R.id.add_category_editText);
        String categoryName = etCategoryName.getText().toString();

        if (categoryName.length() == 0) {
            Context context = getActivity();
            CharSequence text = "Enter category name!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            int iconId = mViewPager.getCurrentItem() + 1;
            Toast.makeText(getActivity(), "Category Saved", Toast.LENGTH_SHORT).show();
            db.insertCategory(categoryName, iconId);
        }
    }
}

