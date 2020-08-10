package com.liuzhenli.reader.utils.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.microedu.reader.R;

import java.util.ArrayList;
import java.util.List;

/**
 **********************************************
 * @author AalenChan
 * @功能 用来显示表情面板的View 
 **********************************************
 */
public class FacesView extends FrameLayout implements OnItemClickListener {
	private Context context;
	private OnFaceClickedListener onFaceClickedListener;
	
	/** 显示表情页的viewpager */
	private ViewPager vp_face;

	private RelativeLayout rootView;

	/** 表情页界面集合 */
	private ArrayList<View> pageViews;

	/** 游标显示布局 */
	private LinearLayout layout_point;

	/** 游标点集合 */
	private ArrayList<ImageView> pointViews;

	/** 表情集合 */
	private List<List<ChatFace>> faces;

	/** 表情数据填充器 */
	private List<FaceAdapter> faceAdapters;

	/** 当前表情页 */
	private int current = 0;

	public FacesView(Context context) {
		super(context);
		this.context = context;
	}

	public FacesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.faces_view, this);
		rootView = (RelativeLayout) view.findViewById(R.id.faces_root);
		vp_face = (ViewPager)view.findViewById(R.id.faces_vp_container);
		layout_point = (LinearLayout)view.findViewById(R.id.faces_indicator_container);
	}

	public FacesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	public void setOnFaceOptionsListener(OnFaceClickedListener listener){
		this.onFaceClickedListener = listener;
		
	}

	public void setFaceViewHeight(int height){
		ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
		layoutParams.height = height;
		rootView.setLayoutParams(layoutParams);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		faces = FaceConvertUtil.getInstace().faceLists;
		onCreate();
	}
	
	private void onCreate() {
		initView();
		initViewPager();
		initPoints();
		initData();

	}
	private void initView() {
		
	}
	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		pageViews = new ArrayList<View>();
		// 左侧添加空页
		View nullViewLeft = new View(context);
		// 设置透明背景
		nullViewLeft.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullViewLeft);

		// 中间添加表情页
		faceAdapters = new ArrayList<FaceAdapter>();
		for (int i = 0; i < faces.size(); i++) {
			GridView view = new GridView(context);
			FaceAdapter adapter = new FaceAdapter(context, faces.get(i));
			view.setAdapter(adapter);
			faceAdapters.add(adapter);
			view.setOnItemClickListener(this);
			view.setNumColumns(8);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(5, 0, 5, 0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
		}
		// 右侧添加空页面
		View nullViewRight = new View(context);
		// 设置透明背景
		nullViewRight.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullViewRight);
	}
	
	/**
	 * 初始化底部指示器
	 */
	private void initPoints() {
		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.dot1);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			layout_point.addView(imageView, layoutParams);
			if (i == 0 || i == pageViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.dot2);
			}
			pointViews.add(imageView);

		}
	}
	
	private void initData() {
		vp_face.setAdapter(new ViewPagerAdapter(pageViews));

		vp_face.setCurrentItem(1);
		current = 0;
		vp_face.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				// 描绘分页点
				draw_Point(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						vp_face.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
						pointViews.get(1).setBackgroundResource(R.drawable.dot2);
					} else {
						vp_face.setCurrentItem(arg0 - 1);// 倒数第二屏
						pointViews.get(arg0 - 1).setBackgroundResource(
								R.drawable.dot2);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});


	}
	
	/**
	 * 绘制游标背景
	 */
	public void draw_Point(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.drawable.dot2);
			} else {
				pointViews.get(i).setBackgroundResource(R.drawable.dot1);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (id < 0) return;
		ChatFace chatFace = (ChatFace) faceAdapters.get(current).getItem(position);
		if (chatFace.getId() == R.drawable.publish_delete_btn) {
			if (onFaceClickedListener != null) {
				onFaceClickedListener.onFaceDelete();
			}
		}
		if (!TextUtils.isEmpty(chatFace.getCharacter())) {
			if (onFaceClickedListener != null)
				onFaceClickedListener.onFaceSelect(chatFace);
		}

	}	
	
	public class ViewPagerAdapter extends PagerAdapter {

	    private List<View> pageViews;

	    public ViewPagerAdapter(List<View> pageViews) {
	        super();
	        this.pageViews=pageViews;
	    }

	    // 显示数目
	    @Override
	    public int getCount() {
	        return pageViews.size();
	    }

	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0 == arg1;
	    }

	    @Override
	    public int getItemPosition(Object object) {
	        return super.getItemPosition(object);
	    }

	    @Override
	    public void destroyItem(View arg0, int arg1, Object arg2) {
	        ((ViewPager)arg0).removeView(pageViews.get(arg1));
	    }

	    /***
	     * 获取每一个item,类于listview中的getview
	     */
	    @Override
	    public Object instantiateItem(View arg0, int arg1) {
	        ((ViewPager)arg0).addView(pageViews.get(arg1));
	        return pageViews.get(arg1);
	    }
	}
	public class FaceAdapter extends BaseAdapter {

	    private List<ChatFace> data;

	    private LayoutInflater inflater;

	    private int size=0;

	    public FaceAdapter(Context context, List<ChatFace> list) {
	        this.inflater= LayoutInflater.from(context);
	        this.data=list;
	        this.size=list.size();
	    }

	    @Override
	    public int getCount() {
	        return this.size;
	    }

	    @Override
	    public Object getItem(int position) {
	        return data.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    @SuppressLint("InflateParams")
	    @SuppressWarnings("deprecation")
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	ChatFace face=data.get(position);
	        ViewHolder viewHolder=null;
	        if(convertView == null) {
	            viewHolder=new ViewHolder();
	            convertView=inflater.inflate(R.layout.faces_item, null);
	            viewHolder.iv_face=(ImageView)convertView.findViewById(R.id.item_iv_face);
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder=(ViewHolder)convertView.getTag();
	        }
	        if(face.getId() == R.drawable.publish_delete_btn) {
	            convertView.setBackgroundDrawable(null);
	            viewHolder.iv_face.setImageResource(face.getId());
	        } else if(TextUtils.isEmpty(face.getCharacter())) {
	            convertView.setBackgroundDrawable(null);
	            viewHolder.iv_face.setImageDrawable(null);
	        } else {
	            viewHolder.iv_face.setTag(face);
	            viewHolder.iv_face.setImageResource(face.getId());
	        }

	        return convertView;
	    }

	    class ViewHolder {
	        public ImageView iv_face;
	    }
	}
	/**
	 **************************************************************
	 * @author AalenChan
	 * @接口	OnFaceOptionsListener
	 * 		<p>onFaceSelect(ChatFace ChatFace) 当表情被点击时
	 * 		<p>onFaceDelete() 当点击删除表情按钮时
	 **************************************************************
	 */
	public interface OnFaceClickedListener{
		public void onFaceSelect(ChatFace ChatFace);
		public void onFaceDelete();
	}
}
