package com.example.kkk.kkkdemo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.SystemClock;
import master.flame.danmaku.ui.widget.DanmakuView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity2 extends FragmentActivity {

    @BindView(R.id.sv_danmaku_v)
    DanmakuView mDanmakuView2;

    @BindView(R.id.op)
    View operationView;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.call)
    TextView call;

    @BindView(R.id.op_text)
    View textOperationView;

    @BindView(R.id.zfbView)
    View zfbView;

    @BindView(R.id.op_call)
    View callOperationView;

    private DanmakuContext mContext;

    private DanmakuView currentDanmakuView;

    private String[] string = new String[]{
            "晚上去哪吃饭。。。", "还有50个bug没解", "什么玩意？", "卧槽", "将改革进行到底", "中国上榜公司数量115家,中国五大行“霸占”利润榜", "双重标准？！特朗普集团为雇外国工人向政府寻求签证"
    };

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    private SpannableStringBuilder createSpannable(String content, Drawable drawable) {
        String text = content;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")),
                0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    public void onCall1Click(View view) {
        callOperationView.setVisibility(View.GONE);
    }

    public void onZFClick(View view) {
        zfbView.setVisibility(View.GONE);
    }

    public void onZFBClick(View view) {
        if (zfbView.getVisibility() == View.VISIBLE) {
            zfbView.setVisibility(View.GONE);
        } else {
            zfbView.setVisibility(View.VISIBLE);
        }
    }

    public void onQieHuanClick(View view) {
        zfbView.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 2);
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 1)
                .setDuplicateMergingEnabled(false)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair)
                .setScrollSpeedFactor(10.2f)
                .setDanmakuMargin(40);

        initDanmuku(mDanmakuView2);
    }

    public void onText1Click(View view) {
        textOperationView.setVisibility(View.GONE);
    }

    public void onTextClick(View view) {
        operationView.setVisibility(View.GONE);
        textOperationView.setVisibility(View.VISIBLE);
        Observable.timer(3000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                textOperationView.setVisibility(View.GONE);
            }
        });
    }

    public void onCallClick(View view) {
        operationView.setVisibility(View.GONE);
        callOperationView.setVisibility(View.VISIBLE);
    }

    private void initDanmuku(final DanmakuView danmakuView) {
        currentDanmakuView = danmakuView;
        currentDanmakuView.clear();
        currentDanmakuView.clearDanmakusOnScreen();
        danmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void drawingFinished() {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void prepared() {
                danmakuView.start();
                generateSomeDanmaku();
            }
        });
        danmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

            @Override
            public boolean onDanmakuClick(IDanmakus danmakus) {
                BaseDanmaku latest = danmakus.last();
                if (null != latest) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean onDanmakuLongClick(IDanmakus danmakus) {
                operationView.setVisibility(View.VISIBLE);
                Observable.timer(3000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        operationView.setVisibility(View.GONE);
                    }
                });
                BaseDanmaku latest = danmakus.last();
                if (null != latest) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean onViewClick(IDanmakuView view) {
                return false;
            }
        });
        danmakuView.prepare(parser, mContext);
        danmakuView.showFPS(false);
        danmakuView.enableDanmakuDrawingCache(true);
    }

    private void addDanmaKuShowTextAndImage(String content, boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, 48, 48);
        SpannableStringBuilder spannable = createSpannable(content, drawable);
        danmaku.text = spannable;
        danmaku.textSize = sp2px(20);
        danmaku.setTime(currentDanmakuView.getCurrentTime());
        danmaku.textColor = Color.WHITE;
        currentDanmakuView.addDanmaku(danmaku);
    }


    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 向弹幕View中添加一条弹幕
     *
     * @param content    弹幕的具体内容
     * @param withBorder 弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(currentDanmakuView.getCurrentTime());
        danmaku.borderColor = Color.GREEN;
        currentDanmakuView.addDanmaku(danmaku);
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String content = string[(int) (SystemClock.uptimeMillis() % 7)];
                    addDanmaKuShowTextAndImage(content, false);
                    SystemClock.sleep(5500);
                }
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmakuView2 != null && mDanmakuView2.isPrepared() && mDanmakuView2.isPaused()) {
            mDanmakuView2.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView2 != null) {
            // dont forget release!
            mDanmakuView2.release();
            mDanmakuView2= null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView2 != null) {
            // dont forget release!
            mDanmakuView2.release();
            mDanmakuView2 = null;
        }
    }


    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
}
