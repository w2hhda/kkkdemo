package com.example.kkk.kkkdemo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.ViewCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.SystemClock;
import master.flame.danmaku.ui.widget.DanmakuView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.sv_danmaku_v)
    DanmakuView mDanmakuView;

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

    private String[] string = new String[]{
            "魅族 PRO 7 看起来棒棒哒",
            "Flyme 6 用起来简直完美",
            "魅族换机助手超级好用也",
            "魅族国际流量简直方便的不要不要的",
            "PRO 7的背面屏幕看起来很有创意",
            "Flyme 6的图库贴纸也很好玩啊",
            "今晚一起吃饭么？",
            "今天几点回家，我做了你最喜欢的菜。",
            "你在干什么，打电话也不接，快点回我消息！",
            "现在方便么，我们谈一下接下来的合作吧。",
            "据说发现了“天使粒子”，我们赶紧注册个商标吧。",
            "你今天的专利提案已经通过审核了。"
    };

    private int[] icon = new int[] {
      R.drawable.notification,
            R.drawable.share,
            R.drawable.zp_relation_accpet,
            R.drawable.zp_authority_accept
    };

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

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


        initDanmuku(mDanmakuView);
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
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 2);
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mContext = DanmakuContext.create();
        mContext.setDanmakuBold(true);
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(12f)
                .setCacheStuffer(new ViewCacheStuffer<DanViewHolder>() {

                    @Override
                    public DanViewHolder onCreateViewHolder(int viewType) {
                        return new DanViewHolder(View.inflate(getApplicationContext(), R.layout.layout_view_cache, null));
                    }

                    @Override
                    public void onBindViewHolder(int viewType, final DanViewHolder viewHolder, BaseDanmaku danmaku, AndroidDisplayer.DisplayerConfig displayerConfig, TextPaint paint) {
                        if (paint != null) {
                            viewHolder.mText.getPaint().set(paint);
                        }
                        viewHolder.mText.setText(danmaku.text);
                        viewHolder.mText.setTextColor(danmaku.textColor);
                        viewHolder.mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, danmaku.textSize);
                        viewHolder.mIcon.setImageResource(icon[(danmaku.text.length())%(icon.length)]);
                    }

                    @Override
                    public void releaseResource(BaseDanmaku danmaku) {

                    }

                    @Override
                    public void prepare(BaseDanmaku danmaku, boolean fromWorkerThread) {

                    }

                }, null).setMaximumLines(maxLinesPair).preventOverlapping(overlappingEnablePair);
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
        danmakuView.getConfig().setDanmakuMargin(10);
    }

    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.textSize = sp2px(13);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(mDanmakuView.getCurrentTime());
        mDanmakuView.addDanmaku(danmaku);
    }

    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String content = string[(int) (SystemClock.uptimeMillis() % string.length)];
                    addDanmaku(content, false);
                    SystemClock.sleep(6000);
                }
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    private class DanViewHolder extends ViewCacheStuffer.ViewHolder {
        private final ImageView mIcon;
        private final TextView mText;

        public DanViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mText = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
