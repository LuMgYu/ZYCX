package qcjlibrary.adapter;

import java.util.List;
import com.zhiyicx.zycx.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.OnTabselectedListener;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.listview.base.pinnedheaderlistview.SectionedBaseAdapter;
import qcjlibrary.model.ModelCancerCategory;
import qcjlibrary.model.ModelRequest;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.SpanUtil;

public class RequestCommonAdapter extends SectionedBaseAdapter {

	private ModelRequestItem mRequestData;
	private int page = 0;
	private LayoutInflater inflator;

	public RequestCommonAdapter(BaseActivity activity, List<Model> list) {
		super(activity, list);
		// TODO 自动生成的构造函数存根
	}

	public RequestCommonAdapter(BaseActivity activity, ModelRequestItem mRequestData) {
		super(activity, null);
		this.mRequestData = mRequestData;
		if (activity != null) {
			this.inflator = LayoutInflater.from(activity);
		}
	}

	@Override
	public Object getItem(int section, int position) {
		// TODO 赶时间，先将就粗暴的解决一哈…………
		if(position == -1){
			return mList.get(0);
		} else{
			position = index - 2;
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public int getSectionCount() {
		// TODO 自动生成的方法存根
		return 1;
	}

	@Override
	public int getCountForSection(int section) {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public View getItemView(int section, int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_request_answer, null);
			initView(convertView, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position);
		return convertView;
//		 LinearLayout layout = null;
//		 if (convertView == null) {
//		 LayoutInflater inflator = (LayoutInflater) parent.getContext()
//		 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		 layout = (LinearLayout)
//		 inflator.inflate(R.layout.item_me_choose_province, null);
//		 } else {
//		 layout = (LinearLayout) convertView;
//		 }
//		 return layout;
	}

	private void bindDataToView(ViewHolder holder, int position) {
		if (holder != null) {
			ModelRequestItem modelRequestItem = (ModelRequestItem) mList.get(position);
			if (modelRequestItem != null) {
				holder.tv_title.setText("");
				Drawable drawable = mBaseActivity.getResources().getDrawable(R.drawable.q);
				holder.tv_title.setText(SpanUtil.setImageSpan("xx  "+" "+modelRequestItem.getQuestion_content(), 0, 2, drawable));
//				holder.tv_title.append("	" + modelRequestItem.getQuestion_content());
				holder.tv_answer.setVisibility(View.GONE);
				holder.tv_advice.setVisibility(View.GONE);
				holder.tv_expert_answer.setVisibility(View.GONE);
				if (modelRequestItem.getAnswercontent() != null && !modelRequestItem.getAnswercontent().equals("")) {
					holder.tv_answer.setVisibility(View.VISIBLE);
					Drawable anwerDrable = mBaseActivity.getResources().getDrawable(R.drawable.a);
					holder.tv_answer.setText("");
					holder.tv_answer.setText(SpanUtil.setImageSpan("xx  "+" "+modelRequestItem.getAnswername() + ":" + modelRequestItem.getAnswercontent(), 0, 2, anwerDrable));
//					holder.tv_answer.append(
//							"	" + modelRequestItem.getAnswername() + ":" + modelRequestItem.getAnswercontent());
				}
				if (modelRequestItem.getIs_recommend() != null) {
					if (modelRequestItem.getIs_recommend().equals("1")) {
						holder.tv_advice.setVisibility(View.VISIBLE);
					}
				}
				if (modelRequestItem.getIs_expert() != null) {
					if (modelRequestItem.getIs_expert().equals("1")) {
						holder.tv_answer.setVisibility(View.GONE);
						holder.tv_expert_answer.setVisibility(View.VISIBLE);
						holder.tv_expert_answer.setText("");
						String expert = "专家建议："+modelRequestItem.getAnswercontent();
						holder.tv_expert_answer.setText(SpanUtil.setForegroundColorSpan(expert, 0, 5,
								mBaseActivity.getResources().getColor(R.color.text_yellow)));
//						holder.tv_expert_answer.append(SpanUtil.setForegroundColorSpan("专家建议：", 0, 0,
//								mBaseActivity.getResources().getColor(R.color.text_yellow)));
//						holder.tv_expert_answer.append(modelRequestItem.getAnswercontent());
					}
				}
				holder.tv_date.setText(modelRequestItem.getTime());
				holder.tv_num.setText(modelRequestItem.getAnswer_count());
				holder.iv_request_line.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initView(View convertView, ViewHolder holder) {
		if (convertView != null && holder != null) {
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_advice = (TextView) convertView.findViewById(R.id.tv_advice);
			holder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
			holder.tv_expert_answer = (TextView) convertView.findViewById(R.id.tv_expert_answer);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.iv_request_line = (ImageView) convertView.findViewById(R.id.iv_request_line);
		}
	}

	@Override
	public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
		LinearLayout layout;
		if(convertView == null && inflator != null){
			layout = (LinearLayout) inflator.inflate(R.layout.item_null, null);
		} else{
			layout = (LinearLayout) convertView;
		}
//		RelativeLayout tab;
//		if (convertView == null && inflator != null) {
//			tab = (RelativeLayout) inflator.inflate(R.layout.item_request_tab, null);
//			tv_1 = (TextView) tab.findViewById(R.id.tv_1);
//			tv_2 = (TextView) tab.findViewById(R.id.tv_2);
//			tv_3 = (TextView) tab.findViewById(R.id.tv_3);
//			tv_4 = (TextView) tab.findViewById(R.id.tv_4);
//		} else {
//			tab = (RelativeLayout) convertView;
//		}
//		tv_1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetTextView();
//				tv_1.setTextColor(green);
//				mRequestData.setType("0");
//				refreshNew();
//				Log.d("Cathy", "tv_1");
//				if (l != null) {
//					l.onTabSelectde(0);
//				}
//			}
//
//		});
//		tv_2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetTextView();
//				tv_2.setTextColor(green);
//				mRequestData.setType("1");
//				refreshNew();
//				Log.d("Cathy", "tv_2");
//				if (l != null) {
//					l.onTabSelectde(1);
//				}
//			}
//		});
//		tv_3.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetTextView();
//				tv_3.setTextColor(green);
//				mRequestData.setType("2");
//				Log.d("Cathy", "tv_3");
//				refreshNew();
//				if (l != null) {
//					l.onTabSelectde(2);
//				}
//			}
//		});
//		tv_4.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetTextView();
//				tv_4.setTextColor(green);
//				Log.d("Cathy", "tv_4");
//				if (l != null) {
//					l.onTabSelectde(3);
//				}
//			}
//		});

		return layout;
	}

	private TextView tv_1;
	private TextView tv_2;
	private TextView tv_3;
	private TextView tv_4;
	private int green = mApp.getActivity().getResources().getColor(R.color.text_green);
	private int gray = mApp.getActivity().getResources().getColor(R.color.text_gray);
	private void resetTextView() {
		tv_1.setTextColor(gray);
		tv_2.setTextColor(gray);
		tv_3.setTextColor(gray);
		tv_4.setTextColor(gray);
	}

	@Override
	public void refreshNew() {
		page = 0;
		mRequestData.setPage(page);
		Log.i("anwer", mRequestData.toString() + "");
		sendRequest(mApp.getRequestImpl().index(mRequestData), ModelRequest.class, 0, REFRESH_NEW);
	}

	@Override
	public void refreshHeader(Model item, int count) {
		refreshNew();
	}

	@Override
	public void refreshFooter(Model item, int count) {
		if (item instanceof ModelRequestItem) {
			ModelRequestItem data = (ModelRequestItem) item;
			page++;
			data.setPage(page);
			data.setType(mRequestData.getType());
			sendRequest(mApp.getRequestImpl().index(data), ModelRequest.class, 0, REFRESH_FOOTER);
		}

	}

	@Override
	public int getTheCacheType() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public Object getReallyList(Object object, Class type2) {
		if (object instanceof ModelRequest) {
			ModelRequest request = (ModelRequest) object;
			if(isLoading()){
				setLoading(false);
			}
			return request.getList();
		}
		return null;
	}

	private OnTabselectedListener l;

	public void setOnTabselectedListener(OnTabselectedListener l) {
		this.l = l;
	}
	
	private int index;
	public void setItemIndex(int index){
		this.index = index;
	}

}
