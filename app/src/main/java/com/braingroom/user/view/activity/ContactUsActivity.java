package com.braingroom.user.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import com.braingroom.user.R;

public class ContactUsActivity extends AppCompatActivity {
    TextView contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String heading1="\nBusiness Hours\n\n";
        String heading2="\n\nGeneral Queries\n\n";
        String heading3="\n\nVendor Services\n\n";
        String subheading1="For Class Posting Queries:\n";
        String business ="Monday-Saturday\t:\t10am to 7pm\n Sunday\t\t Closed";
        String query="Help Desk\t:\t044 49507392\n\nProduct or Transaction Related\t:\t7550021666\n\nMail-id\t:\tcontactus@braingroom.com";
        String vendor="manoj@braingroom.com\nsangeetha@braingroom.com\nsheela@braingroom.com\n\nFor contact\t:\n7550021666,9962584477,9962084477";
        Spanned contactUs=(Spanned) TextUtils.concat(heading(heading1),normal(business),heading(heading2),normal(query),heading(heading3),subheading(subheading1),normal(vendor));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        contact= (TextView) findViewById(R.id.contact);
        contact.setText(contactUs);

    }
    public Spannable heading(String text){
        Spannable span= new SpannableString(text);
        span.setSpan(new RelativeSizeSpan(2f),0,text.length(),0);
        return span;

    }
    public Spannable subheading(String text){
        Spannable span=new SpannableString(text);
        span.setSpan(new RelativeSizeSpan(1.5f),0,text.length(),0);
        return span;
    }
    public Spannable normal(String text){
        return new SpannableString(text);
    }
}
