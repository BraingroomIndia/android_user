package com.braingroom.user.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.braingroom.user.R;

public class TermsConditionActivity extends AppCompatActivity {
    TextView textView;
    String termsCondition="<div style=\"padding:20px;\"><h2 class=\"main\">Description of Service</h2>\n" +
            "        <h4 class=\"text-primary\">Class Type (Fixed/Flexible) and Booking related:</h4>\n" +
            "\t\t\n" +
            "       \t<li>Listing classes in braingroom.com is free. Anyone can browse, choose, book and pay for classes through braingroom.com</li>\n" +
            "<li>For successful bookings, a Booking Confirmation email will be sent to your designated email address and a booking success SMS will be sent to your registered mobile number.</li>\n" +
            "<li>Braingroom has three types of class posts – Normal (Fixed &amp; Flexible Classes) and Catalogue Classes.</li>\n" +
            "\n" +
            "<h3 class=\"text-primary\" style=\"font-family: serif;\">Fixed classes: </h3>\n" +
            "<div class=\"fix\">\n" +
            "\t<li>These type of classes will have fixed timing, date &amp; place. </li>\n" +
            "<li>Fixed classes once booked cannot be cancelled by the user.</li>\n" +
            "<li>Fixed classes cannot be posted before one month from the date of its commencement. And it will be valid / live only till the last occurrence of the class.</li>\n" +
            "</div>\n" +
            "<br>\n" +
            "<h3 class=\"text-primary\" style=\"font-family: serif;\">Flexible class: </h3>\n" +
            "<div class=\"fix\">\n" +
            "<li>For these type of classes, after booking, users can call vendors and fix the class at a mutually convenient time &amp; place.</li>\n" +
            "<li>As per the policy, vendor should start a flexible class within one month from the class booking date. </li>\n" +
            "<li>Flexible classes will be valid (or live in Braingroom) from “Class validity date-from” to “Class validity date – to”. This time period cannot be more than 6 months.</li>\n" +
            "</div>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Catalogue Class Listings related:</h4>\n" +
            "<li>Many renowned tutors &amp; experienced academies post their classes in braingroom.com’s catalogue area for institutional booking (bulk booking) alone. Price for these catalogue classes will be discussed and decided by tutor/academy and organizer (Braingroom user) through series of queries mediated by Braingroom admin.</li>\n" +
            "<li>For the service of coordinating catalogue classes, Braingroom collects a 15% commission on total class fees paid by the organizer to the tutor/academy.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Class Gift Coupons &amp; Gift Cards related:</h4>\n" +
            "<li>Specific classes can be gifted as Gift cards or Gift coupons with specific denomination through Braingroom.</li>\n" +
            "<li>All Gift Coupons and Gift Cards for individuals and corporates will be sent on the same date or on the chosen date, whichever has been chosen by the user. Intimation for the same will be sent through both mail and SMS.</li>\n" +
            "<li>Gift classes to NGOs will give the user the option to choose the NGO &amp; segment (under which a micro class will be conducted at that NGO) to send a Gift. Admin will moderate the proceedings for this between user and NGO.</li>\n" +
            "<li>For “Gift for NGO” case, admin will finalize the vendor &amp; class, based on the location and vendor availability. Admin will try to make it on a mutually convenient date where user can also attend it but still it is not a mandate as vendor availability will be given more priority than the user chosen date.</li>\n" +
            "<li>Gift Coupon id should be redeemed within 2 months of gift receiving date. After which coupon will get invalid &amp; money associated with it will be transferred to BrainGroom.</li>\n" +
            "<li>Specific fixed or flexible classes gifted under “Gift Cards” will also come under corresponding fixed / flexible rules and terms &amp; conditions as mentioned above</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Class Quality &amp; Guarantee related:</h4>\n" +
            "<li>We provide all class related information in the description area &amp; vendor related information in the profile area. So it is the user’s responsibility to choose the right class and refunds are only applicable in the case of tutor/academy cancelling the class in the beginning or in the middle of the class. </li>\n" +
            "<li>Braingroom is not responsible for the quality of content offered by the vendor as we do only profile check and are not responsible for the training materials / content provided by the vendor to user</li>\n" +
            "<li>Braingroom guarantees the credibility of profile information provided only by the “registered &amp; verified” vendors. For other unverified vendors, we are not responsible for the credibility of the details provided in their profile information.</li>\n" +
            "<li>Reviews are to be filled by the users after they attend a class, booked through Braingroom. </li>\n" +
            "<li>Reviews are purely the opinion of the users and Braingroom will not force or manipulate the users to provide specific views. </li>\n" +
            "<li>Braingroom shall also not be responsible for any of the reviews or comments made by the users.</li>\n" +
            "<br>\n" +
            "<h2 class=\"main\">Payments, Cancellation and Refunds\n" +
            "<br>\n" +
            "</h2><h4 class=\"text-primary\">Pricing related:</h4>\n" +
            "<li>Braingroom.com collects a 10% commission of the listing price for each class sold.</li>\n" +
            "<li>For the service of coordinating catalogue classes, Braingroom collects a 15% commission on total class fees paid by the organizer to the tutor/academy.</li>\n" +
            "<li>Classes listed on Braingroom which are also listed on your own website or through third-party providers (other e-commerce sites) should be priced uniformly. This is to prevent price discrimination to Braingroom users.</li>\n" +
            "<li>For the purpose of marketing and acquiring user base Braingroom reserves all rights to reduce the price of the classes. But this reduction will be done only by compensating Braingroom’s commission and will not affect the vendor in their pricing.</li>\n" +
            "<li>The vendors might enter a partnership agreement to provide the Braingroom users with special prices.</li>\n" +
            "<li>For flexible classes, it is the tutor/academy’s responsibility to enter the class start code &amp; end code to ensure the class progress is getting tracked in the system for payment processing. Only after getting the end code, the payment will be processed</li>\n" +
            "<li>If vendor fails to enter these codes, the money will be with Braingroom and vendor has one month to re-enter the code / call Braingroom to provide code &amp; process payment, failing which the vendors lose their right to claim the money.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Payment Processing related:</h4>\n" +
            "<li>Within 3 working days after the fixed / flexible class completion, the tutors’ / coaching institutes (vendors) will receive a sales summary via email. </li>\n" +
            "<li>Payment happens in three cycles in a month at Braingroom. It will happen on every 10th, 20th &amp; 30th of a month. During these cycles, Braingroom will make payment to your designated bank account via Internet Banking. </li>\n" +
            "<li>All classes completed between 28th of previous month &amp; 7th of current month will be paid on 10th of current month; </li>\n" +
            "<li>All classes completed between 8th &amp; 17th of every month will be paid on 20th of that month; </li>\n" +
            "<li>All classes completed between 18th &amp; 27th of every month will be paid on 30th of that month</li>\n" +
            "<li>Classes happening between 8th to 10th, 18th to 20th and 28th to 30th of every month will be processed in the next cycle only.</li>\n" +
            "<li>As a Braingroom partner, you can offer special vendor rates to Braingroom to stand out in the crowd.</li>\n" +
            "<li>Discounts related to Group Classes &amp; Bulk bookings are provided solely by the vendor and not by BrainGroom. Hence BrainGroom will not be responsible for any issue arising based on this.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Vendor Initiated Cancellations:</h4>\n" +
            "<li>Please ensure that a minimum of 2 days’ advance notice is given to attendees who have booked the cancelled class</li>\n" +
            "<li> tutor/ academy, when you cancel 3 classes in a row, your account will be first temporarily withdrawn and a review will be conducted for further listing of classes. If there is a credibility issue, Braingroom reserves the right to deactivate the vendor account from Braingroom.</li>\n" +
            "<li>vendors are purely responsible for ensuring that attendees are notified and aware of the cancellation.</li><li> vendor cancels a class without any notification to the user / BrainGroom then the user can either, choose to call / mail Braingroom &amp; inform about the incident. </li>\n" +
            "<li>rnatively, the user can also inform about the class cancellation incident through the automatic feedback form sent to the user’s registered mail id (sent the day after the class date) </li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Customer Initiated Cancellations:</h4>\n" +
            "<li>No refund for customer initiated cancellations.</li>\n" +
            "<li>A learner can try to reschedule a class by directly talking to vendor. Braingroom will not mediate this. But if the vendor is ready to reschedule it then we will make payment accordingly for flexible class &amp; fixed classes (on actual / new class completion date).</li>\n" +
            "<li>Rescheduled classes should be notified to Braingroom before the user attends the class for ease of making payments.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Refunds related:</h4>\n" +
            "<li>Refund is possible only in the case where tutor/academy is cancelling the class before / in between or not conducting the classes for the mentioned time period.</li>\n" +
            "<li>If a vendor fails to start a flexible class within one month, then the learner is eligible for refund and vendor will be placed in the red list for further investigation.</li>\n" +
            "<li>If a vendor cancels three consecutive classes, then his account &amp; associated email ids will be deactivated from braingroom.com, until further investigations are carried out and Braingroom reserves right to decide if the vendor account will be reactivated or otherwise.</li>\n" +
            "<li>Tutors &amp; Academies should make sure the 10% (normal classes) &amp; 15% (catalogue classes) commissions deducted from every BrainGroom class booking is from the listing price and is not over and above the listing price.</li>\n" +
            "<li>If a vendor fails to attend the fixed catalogue class without prior notice, then the learner is eligible for refund and investigation will be carried out to avoid such instances in the future.</li>\n" +
            "<li>If a vendor cancels three consecutive catalogue classes, then his account &amp; associated email ids will be deactivated from braingroom.com until further investigation.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Featured listing related:</h4>\n" +
            "<li>Tutors/Academies can promote their Classes in featured list in home page &amp; category page by making payments accordingly.</li>\n" +
            "<li>Payments for home page &amp; category page featured listings will be different and will change from time to time based on demand so vendor should check that under “Promote Class” tab before booking.</li>\n" +
            "<li>Classes will be listed in featured lists through a bidding system. Slots will be allocated based on availability and first come first served basis.</li>\n" +
            "<li>For unsuccessful bids, money will be refunded in three to five days.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Class Approvals &amp; Rejections related:</h4>\n" +
            "<li>Class postings, Adding class to Catalogue requests and Featured promote class requests – all these come under admin verification; Admin’s decision will be final in all these cases.</li>\n" +
            "<li>Vendors can call our helpline and can ask the reason for their rejection to improve and repost. Still admin decision is final.</li>\n" +
            "<li>Vendors posting obscene &amp; irrelevant class posts will be deactivated at once. Also class posts hurting sentiments of certain individuals / group of people will also be removed at once.</li>\n" +
            "<li>Vendors taking Braingroom’s help to upload classes, should be aware that it is their responsibility to provide the right description, details &amp; pictures. Braingroom cannot be responsible for any such mismatch in the information provided.</li>\n" +
            "<li>To avoid data mismatch, as a procedure once the class summary and details are written by BrainGroom (on behalf of vendors), we will send it to the vendor and the vendor needs to reply within 48hrs if there is any correction.</li>\n" +
            "<li>/If there is no correction sent to Braingroom within 48 hours, then we will put it in the website and the full responsibility lies with the vendors. For corrections mailed within 48 hours, we will make those changes, get confirmation and then publish it in BrainGroom.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Issue Resolution related:</h4>\n" +
            "<li>Any issue raised on the vendor by user or the ones raised on the user by vendor will all be closed within thirty days.</li>\n" +
            "<li>All the issues related to class timing / attendance should be raised within 3 to 5 days of the occurrence. Issues raised after this time period will be considered null &amp; void.</li>\n" +
            "<li>Only issues related to class timings, vendor / user attendance, discrepancy in vendor details provided in profile information (for verified vendors) will be accepted by BrainGroom helpdesk.</li>\n" +
            "<li>During the issue resolution, payment will be parked at BrainGroom account.</li>\n" +
            "<br>\n" +
            "<h4 class=\"text-primary\">Promocode / Offers related:</h4>\n" +
            "<li>There is a maximum discount amount or the value per transaction with the promo codes on the total transaction amount, whichever is lower.</li>\n" +
            "<li>The discount code is valid only on Class Bookings and can be redeemed on Braingroom mobile app and website. </li>\n" +
            "<li>The offer is valid for a limited time period only!</li>\n" +
            "<li>Braingroom reserves the right to withdraw or modify this offer at any time and without prior notice. In case of any dispute, the decision of Braingroom will be final.</li>\n" +
            "<li>This offer cannot be combined with any other Offers/Coupons/Promotions at Braingroom.</li>\n" +
            "<br>\n" +
            "<h2 class=\"main\">Connect Discussion Forums and Groups</h2>\n" +
            "<li>All ideas or opinions expressed in the discussion forum and group are subjected to user views &amp; opinions and hence BrainGroom cannot be held responsible for the same. However, hateful, obscene and revengeful posts / comments with “spam” (highlights from other users) will be verified by admin &amp; will be removed at once.</li>\n" +
            "<li>All blog articles posted by Vendors are their own responsibilities. We don’t support any of their claims or views mentioned in those articles</li>\n" +
            "<li>All meeting / activity requests posted by fellow users are based on their own initiatives / interests and BrainGroom has no hidden/direct interest in those. Hence any cancellation or changes with respect to activity requests will also be the specific users’ responsibility and not Braingroom’s.</li>\n" +
            "<li>Spam content will be removed within at max 48 hours of issue highlights from the users.</li>\n" +
            "<br>\n" +
            "<h2 class=\"main\">Marketing and Intellectual Property</h2>\n" +
            "<li>You grant permission to Braingroom to use material from your listed classes (copywriting, images, etc) for marketing purposes across various channels such as Search Engines and Social Media. This material still remains your Intellectual Property.</li>\n" +
            "<li>All materials posted under class description are vendor’s responsibilities. So Braingroom will not take responsibility for any inconsistencies or misleading details in class information. </li>\n" +
            "<li>Braingroom will only take responsibility of details provided by Braingroom’s “verified vendors”</li>\n" +
            "<li>To get verified by Braingroom, vendors should pay Rs. 5000 &amp; get their information audited &amp; verified by Braingroom executives and after successful completion of verification, vendors profile will have “Braingroom verified” symbol in their profiles.</li>\n" +
            "\n" +
            "\n" +
            "<br>\n" +
            "<h2 class=\"main\">Videos and Photos:</h2>\n" +
            "<li>Braingroom is offering photo shot on priority for preferred vendors &amp; partners right now. Other interested vendors can raise a request for the same and BrainGroom will attend to it as per availability. However, this will come with an additional cost.</li>\n" +
            "<li>Braingroom has an upper limit for Photos &amp; Videos (w.r.t both count &amp; size) that a vendor can upload under his profile and under connect part. If a user exceeds this BrainGroom has the right to remove the most recently added photo / video without user permission.</li>\n" +
            "<li>Braingroom will request the vendors for a video and photo session. The decision to accept / reject the request solely depends on the vendor’s.</li>\n" +
            "<li>Braingroom will have full rights over the photo and the video content taken by our in-house personnel and will use them solely for the purpose of marketing the vendor’s classes through the website.</li>\n" +
            "<li>In the event the vendor does not provide access to Braingroom personnel or provide with any photo’s Braingroom has all rights to use an appropriate photo that will closely relate to the type/category of the classes.</li>\n" +
            "<li>All photos used in the website are legally obtained through appropriate channels by Braingroom.</li>\n" +
            "<li>Braingroom will reserve all rights to reject any photo or video content provided by the vendor if the admin decides that it is not relevant to the class content. </li>\n" +
            "<li>Braingroom is using Google maps to map classes to their addresses. Any issue arising due to mismatch / inconsistency in location mapping will not be borne by BrainGroom as it is being generated by a Third party.</li>\n" +
            "\n" +
            "<br>\n" +
            "<h2 class=\"main\">Force Majeure</h2>\n" +
            "<p>For the purpose of this Agreement, an “Event of Force Majeure” means any circumstance not within the reasonable control of the Party affected, but only if and to the extent that (i) such circumstance, despite the exercise of reasonable diligence and the observance of Good Utility Practice, cannot be, or be caused to be, prevented, avoided or removed by such Party, and (ii) such circumstance materially and adversely affects the ability of the Party to perform its obligations under this Agreement, and such Party has taken all reasonable precautions, due care and reasonable alternative measures in order to avoid the effect of such event on the Party’s ability to perform its obligations under this Agreement and to mitigate the consequences thereof.</p>\n" +
            "</div>";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spanned spanned = Html.fromHtml(termsCondition);
        setContentView(R.layout.activity_terms_condtion);
        textView= (TextView) findViewById(R.id.terms);
        textView.setText(spanned);
    }
}
