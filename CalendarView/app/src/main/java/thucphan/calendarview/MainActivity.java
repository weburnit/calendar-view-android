package thucphan.calendarview;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.weburnit.calendar.ResizableCalendarView;
import com.weburnit.calendar.manager.CalendarManager;
import com.weburnit.calendar.widget.CalendarAdapter;
import com.weburnit.calendar.widget.CalendarItem;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarLayout mAppBarLayout;
    private ResizableCalendarView mCalendarView;
    private RecyclerView mRecyclerView;


    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("Section 1");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.setExpanded(true, true);
        mCalendarView = (ResizableCalendarView) findViewById(R.id.calendar_view);

        List<CalendarItem> data = new ArrayList<CalendarItem>();
        data.add(new CalendarItem() {
            @Override
            public String getPrice() {
                return "12$";
            }

            @Override
            public String getInformation() {
                return "Something";
            }

            @Override
            public String getSubject() {
                return "My subject";
            }

            @Override
            public String getPhoto() {
                return "https://d13yacurqjgara.cloudfront.net/users/62525/screenshots/2457251/fitness_calendar.png";
            }

            @Override
            public String getName() {
                return "Paul Item";
            }
        });

        mCalendarView.setListener(new ResizableCalendarView.OnDateSelect() {
            @Override
            public void onDateSelected(LocalDate date, CalendarAdapter adapter) {
                mCalendarView.selectDate(date);

//                adapter.addData(data);
            }
        });

//        mCalendarView.next();


        final ImageView arrow = (ImageView) findViewById(R.id.date_picker_arrow);

        RelativeLayout datePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                    isExpanded = false;
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                    isExpanded = true;
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        CalendarManager manager = new CalendarManager(LocalDate.now(), 2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCalendarView.init(manager, mRecyclerView);
//        mRecyclerView.setAdapter(new ClassAdapter());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
