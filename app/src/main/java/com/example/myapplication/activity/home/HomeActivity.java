package com.example.myapplication.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.adapter.ProductDetailAdapter;
import com.example.myapplication.adapter.SlideAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.R;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.Slide;
import com.example.myapplication.model.resObj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private SlideAdapter photoAdapter;

    private List<Slide> mListPhoto;

    private Timer mTimer;

    private TextView btnsearch;

    private TextView txtTimer;

    CountDownTimer Timer;

    private RecyclerView.Adapter adapter;

//    private ProductDetailAdapter productDetailAdapter;
    private  RecyclerView listViewLastBook, listViewForYou, listViewBestSeller, listViewFlashSale;
    List<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        AnhXa();
        ClickOneThing();
        onStart();
        setSlideHome();
        setViewFlashSale();
        setViewLastBook();
        setViewForYou();
        setViewBestSeller();
    }

    public void ClickOneThing() {
//        btnsearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
//                startActivity(intent);
//            }
//        });

        btnsearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.my_view_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setBackgroundResource(R.drawable.white_backgroud);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        long now = System.currentTimeMillis();
        long midnight = getMidnight(now);
        CountDownTimer timer = new CountDownTimer(midnight - now, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (60 * 60 * 1000));
                int minutes = (int) ((millisUntilFinished / (60 * 1000)) % 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                txtTimer.setText(time);
            }

            @Override
            public void onFinish() {
                txtTimer.setText("00:00:00");
            }
        };
        timer.start();
    }

    private long getMidnight(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    public void AnhXa() {
        btnsearch = findViewById(R.id.editTextTextPersonName);
        txtTimer = findViewById(R.id.txtGio);
        listViewLastBook = findViewById(R.id.view);
        listViewFlashSale = findViewById(R.id.viewflash);
        listViewForYou = findViewById(R.id.viewforyou);
        listViewBestSeller = findViewById(R.id.viewbestseller);
        viewPager = findViewById(R.id.viewpaper);
        circleIndicator = findViewById(R.id.circle_indicator);
    }

    private void setSlideHome() {
        mListPhoto = getListPhoto();
        photoAdapter = new SlideAdapter(this, mListPhoto);
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoSlideImages();
    }

    private void setViewFlashSale() {
        ApiService.apiService.getLowestProduct().enqueue(new Callback<resObj>() {
            @Override
            public void onResponse(Call<resObj> call, Response<resObj> response) {
                if (response.isSuccessful()) {
                    products = response.body().getData();
                    adapter = new ProductDetailAdapter(products, HomeActivity.this);
                    listViewFlashSale.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    listViewFlashSale.setLayoutManager(linearLayoutManager);
                    listViewFlashSale.setAdapter(adapter);
                    //productDetailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<resObj> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    private List<Slide> getListPhoto() {
        List<Slide> list= new ArrayList<>();
        list.add(new Slide(R.drawable.slide_home_1));
        list.add(new Slide(R.drawable.slide_home_2));
        list.add(new Slide(R.drawable.slide_home_3));

        return list;

    }
    private void setViewLastBook() {
        ApiService.apiService.getNewProduct().enqueue(new Callback<resObj>() {
            @Override
            public void onResponse(Call<resObj> call, Response<resObj> response) {
                if (response.isSuccessful()) {
                    products = response.body().getData();
                    adapter = new ProductDetailAdapter(products, HomeActivity.this);
                    listViewLastBook.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    listViewLastBook.setLayoutManager(linearLayoutManager);
                    listViewLastBook.setAdapter(adapter);
                    //productDetailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<resObj> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    private void setViewBestSeller() {
        ApiService.apiService.getBestSellerProduct().enqueue(new Callback<resObj>() {
            @Override
            public void onResponse(Call<resObj> call, Response<resObj> response) {
                if (response.isSuccessful()) {
                    products = response.body().getData();
                    adapter = new ProductDetailAdapter(products, HomeActivity.this);
                    listViewBestSeller.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    //RecyclerView.LayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                    listViewBestSeller.setLayoutManager(linearLayoutManager);
                    listViewBestSeller.setAdapter(adapter);
                    //productDetailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<resObj> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    public void setViewForYou() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        listViewForYou.setLayoutManager(layoutManager);
        ArrayList<Product> news = new ArrayList<>();

        adapter = new ProductDetailAdapter(news, this);
        listViewForYou.setAdapter(adapter);
    }

    private void autoSlideImages() {
        if (mListPhoto == null || mListPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init Timer
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem += 1;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}