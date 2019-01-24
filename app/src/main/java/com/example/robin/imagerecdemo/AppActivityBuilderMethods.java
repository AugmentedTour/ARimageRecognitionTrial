package com.example.robin.imagerecdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AppActivityBuilderMethods extends AppCompatActivity {

    private final int SYMBOL_SIZE = 100;
    private final String blue = "#00427f";


    //Adds a title to the selected view.
    public TextView titleBuilder (String title, LinearLayout layout) {
        TextView view = new TextView(this);
        view.setTextSize(40);
        view.setPadding(15,15,25,15);
        view.setTypeface(Typeface.SERIF, Typeface.BOLD);

        String firstLetter = title.substring(0,1);
        String rest = title.substring(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml("<font color=\"" + blue + "\">" + firstLetter + "</font><font color=\"#000000\">" + rest + "</font>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            view.setText(Html.fromHtml("<font color=\"" + blue + "\">" + firstLetter + "</font><font color=\"#000000\">" + rest + "</font>"));
        }

        layout.addView(view);
        return view;
    }

    //Adds a smaller title to the selected view. Recommended for sub-pages
    public TextView subTitleBuilder (String title, LinearLayout layout) {
        TextView view = new TextView(this);
        view.setTextSize(25);
        view.setGravity(Gravity.CENTER);
        view.setPadding(15,15,15,15);
        view.setTypeface(Typeface.SERIF, Typeface.BOLD);

        String firstLetter = title.substring(0,1);
        String rest = title.substring(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml("<font color=\"" + blue + "\">" + firstLetter + "</font><font color=\"#000000\">" + rest + "</font>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            view.setText(Html.fromHtml("<font color=\"" + blue + "\">" + firstLetter + "</font><font color=\"#000000\">" + rest + "</font>"));
        }

        layout.addView(view);
        return view;
    }

    //Adds a generic textView for all of your textView needs.
    public TextView textViewBuilder (String string, LinearLayout layout) {
        TextView view = new TextView(this);
        view.setText(string);
        view.setTextSize(18);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.setPadding(10,10,10,10);

        layout.addView(view);
        return view;
    }

    //Builds a button that is a link
    //text: text displayed on the button | url: url it opens | layout: the layout to build the button in
    public Button linkButtonBuilder (String text, String url, boolean isImportant, LinearLayout layout) {
        Button button = new Button(this);
        button.setText(text);
        if (text.length() > 30) {
            button.setTextSize(15);
        } else {
            button.setTextSize(18);
        }
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));
        button.setPadding(5,5,5,5);
        button.setAllCaps(false);

        if (isImportant) {
            button.setBackgroundResource(R.drawable.important);
        } else {
            button.setBackgroundResource(R.drawable.not_important);
        }
        button.setTextColor(Color.WHITE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
        params.topMargin = 10;
        params.bottomMargin = 15;

        final String theURL = url;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(theURL));
                startActivity(intent);
            }
        });

        layout.addView(button);
        return button;
    }

    //Builds a button that is a link
    //text: text displayed on the button | currentActivity: current context, send as thisMethod.this
    //sendingActivity: activity it opens, please send as activity.class | layout: the layout to build the button in
    public Button activityButtonBuilder (String text, Context currentActivity, Class sendingActivity, boolean isImportant, LinearLayout layout) {
        Button button = new Button(this);
        button.setText(text);
        if (text.length() > 30) {
            button.setTextSize(15);
        } else {
            button.setTextSize(18);
        }
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));
        button.setPadding(5,5,5,5);
        button.setAllCaps(false);
        button.setTextColor(Color.WHITE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
        params.topMargin = 10;
        params.bottomMargin = 15;

        if (isImportant) {
            button.setBackgroundResource(R.drawable.important);
        } else {
            button.setBackgroundResource(R.drawable.not_important);
        }

        final Context theCurrentActivity = currentActivity;
        final Class theActivity = sendingActivity;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = theCurrentActivity;
                Class startSelectedActivity = theActivity;
                Intent startChildActivityIntent = new Intent(context, startSelectedActivity);

                startActivity(startChildActivityIntent);
            }
        });

        layout.addView(button);
        return button;
    }

    //creates a textView that dials but doesn't call the sent number.
    public TextView phoneBuilder (String information, final String number, LinearLayout layout) {
        TextView view = new TextView(this);
        view.setText("Click to dial " + information + " \nat: " + number);
        view.setTextSize(15);
        view.setHeight(175);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.setPadding(50,40,30,20);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        layout.addView(view);
        return view;
    }

    //call this method to add the all genders bathroom symbol
    public ImageView hasAllGendersBathroom (LinearLayout layout) {
        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.drawable.all_genders_bathroom_background);
        view.setImageResource(R.drawable.all_gender_bathrooms_symbol);
        view.setLayoutParams(new LinearLayout.LayoutParams(SYMBOL_SIZE, SYMBOL_SIZE));
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = 5;
        params.bottomMargin = 5;
        params.leftMargin = 5;
        params.rightMargin = 5;
        view.setPadding(5,5,5,5);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast message = Toast.makeText(getApplicationContext(),
                        "This building contains an all genders restroom.",
                        Toast.LENGTH_LONG);

                message.show();
            }
        });

        layout.addView(view);
        return view;
    }

    //call this method to add the accessible symbol
    public ImageView isAccessible (LinearLayout layout) {
        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.drawable.accessible_background);
        view.setImageResource(R.drawable.accessible_symbol);
        view.setLayoutParams(new LinearLayout.LayoutParams(SYMBOL_SIZE, SYMBOL_SIZE));
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = 5;
        params.bottomMargin = 5;
        params.leftMargin = 5;
        params.rightMargin = 5;
        view.setPadding(5,5,5,5);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast message = Toast.makeText(getApplicationContext(),
                        "This building is accessible.",
                        Toast.LENGTH_LONG);

                message.show();
            }
        });

        layout.addView(view);
        return view;
    }

    //call this method to add the information symbol
    public ImageView hasHelp (LinearLayout layout) {
        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.drawable.info_background);
        view.setImageResource(R.drawable.info_symbol);
        view.setLayoutParams(new LinearLayout.LayoutParams(SYMBOL_SIZE, SYMBOL_SIZE));
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = 5;
        params.bottomMargin = 5;
        params.leftMargin = 5;
        params.rightMargin = 5;
        view.setPadding(5,5,5,5);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast message = Toast.makeText(getApplicationContext(),
                        "This building contains an information center.",
                        Toast.LENGTH_LONG);

                message.show();
            }
        });

        layout.addView(view);
        return view;
    }

    //call this method to add the computers symbol
    public ImageView hasComputers (LinearLayout layout) {
        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.drawable.computers_background);
        view.setImageResource(R.drawable.computers_symbol);
        view.setLayoutParams(new LinearLayout.LayoutParams(SYMBOL_SIZE, SYMBOL_SIZE));
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = 5;
        params.bottomMargin = 5;
        params.leftMargin = 5;
        params.rightMargin = 5;
        view.setPadding(5,5,5,5);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast message = Toast.makeText(getApplicationContext(),
                        "This building contains computers and printers.",
                        Toast.LENGTH_LONG);

                message.show();
            }
        });

        layout.addView(view);
        return view;
    }


    // ---- These two are used for the toolbar. ----
    //This one inflates the menu and adds items to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.standard_menu, menu);
        return true;
    }

    //handles the buttons pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_button:
                Context context = this;
                Class startSelectedActivity = HelpActivity.class;
                Intent startChildActivityIntent = new Intent(context, startSelectedActivity);
                startActivity(startChildActivityIntent);
                return true;
            case R.id.back_button:
//                Intent startBackActivityIntent = new Intent(this, BuildingSelect.class);
//                startActivity(startBackActivityIntent);
                return true;
            default:
                // This means that the pressed button wasn't recognized
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
