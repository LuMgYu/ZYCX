package qcjlibrary.adapter;

import com.zhiyicx.zycx.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import qcjlibrary.activity.ExperienceCycleDetail;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelExperienceDetailItem1;
import qcjlibrary.model.ModelExperiencePostDetail;
import qcjlibrary.model.ModelExperiencePostDetailItem;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.SpanUtil;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class ExperienceCycleAdapter extends BAdapter {
	private ModelExperienceDetailItem1 mData;

	public ExperienceCycleAdapter(BaseActivity activity, ModelExperienceDetailItem1 detailItem1) {
		super(activity, null);
		this.mData = detailItem1;
	}

	public ExperienceCycleAdapter(BaseFragment fragment, ModelExperienceDetailItem1 detailItem1) {
		super(fragment, null);
		this.mData = detailItem1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = initView(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position);
		return convertView;
	}

	private void bindDataToView(ViewHolder holder, int position) {
		if (holder != null) {
			holder.tv_date_year.setVisibility(View.VISIBLE);
			if (position > 0) {
				holder.tv_date_year.setVisibility(View.GONE);
			}
			ModelExperiencePostDetailItem detailItem = (ModelExperiencePostDetailItem) mList.get(position);
			String mon = DateUtil.StampToMonth(detailItem.getCtime());
			if(mon.equals("0")){
				mon = "1";
			}
			holder.tv_date_month.setText(mon + "月");
			holder.tv_date_day.setText(DateUtil.StampToDay(detailItem.getCtime()));
			holder.tv_date_week.setText(DateUtil.StampToWeek(detailItem.getCtime()));
			holder.tv_date_year.setText(DateUtil.StampToYear(detailItem.getCtime()));
			holder.tv_date_content.setText(detailItem.getContent());
			holder.tv_howmany.setText("");
			holder.tv_howmany.append("第");
			holder.tv_howmany.append(SpanUtil.setForegroundColorSpan(detailItem.getChildCount() + "", 0, 0,
					mBaseActivity.getResources().getColor(R.color.text_yellow)));
			holder.tv_howmany.append("篇");
			if (detailItem.getIs_praise().equals("1")) {
				holder.iv_hand.setImageResource(R.drawable.zanicon02_red);
				holder.tv_zhengnengliang.setTextColor(mBaseActivity.getResources().getColor(R.color.text_red));
			} else {
				holder.iv_hand.setImageResource(R.drawable.zanicon);
				holder.tv_zhengnengliang.setTextColor(mBaseActivity.getResources().getColor(R.color.text_more_gray));
			}
			holder.tv_zhengnengliang.setText("正能量(" + detailItem.getPraiseCount() + ")");
			holder.rl_zan.setTag(detailItem);
			holder.rl_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ModelExperiencePostDetailItem detailItem = (ModelExperiencePostDetailItem) v.getTag();
					mBaseActivity.sendRequest(mApp.getExperienceImpl().doPraise(detailItem), ModelMsg.class,
							REQUEST_GET);
				}
			});
			holder.tv_more.setTag(detailItem);
			holder.tv_more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ModelExperiencePostDetailItem item = (ModelExperiencePostDetailItem) v.getTag();
					mApp.startActivity_qcj(mBaseActivity, ExperienceCycleDetail.class,
							mBaseActivity.sendDataToBundle(item, null));
				}
			});

		}
	}

	/**
	 * 初始化布局
	 * 
	 * @param convertView
	 * @param holder
	 */
	private View initView(ViewHolder holder) {
		View view = null;
		if (holder != null) {
			view = mInflater.inflate(R.layout.item_experience_cycle, null);
			holder.tv_date_month = (TextView) view.findViewById(R.id.tv_date_month);
			holder.tv_date_day = (TextView) view.findViewById(R.id.tv_date_day);
			holder.tv_date_week = (TextView) view.findViewById(R.id.tv_date_week);
			holder.tv_date_year = (TextView) view.findViewById(R.id.tv_date_year);
			holder.tv_date_content = (TextView) view.findViewById(R.id.tv_date_content);
			holder.tv_more = (TextView) view.findViewById(R.id.tv_more);
			holder.tv_howmany = (TextView) view.findViewById(R.id.tv_howmany);
			holder.rl_zan = (RelativeLayout) view.findViewById(R.id.rl_zan);
			holder.iv_hand = (ImageView) view.findViewById(R.id.iv_hand);
			holder.tv_zhengnengliang = (TextView) view.findViewById(R.id.tv_zhengnengliang);

		}
		return view;
	}

	@Override
	public void refreshNew() {
		sendRequest(mApp.getExperienceImpl().postDetail(mData), ModelExperiencePostDetail.class, REQUEST_GET,
				REFRESH_NEW);
	}

	@Override
	public void refreshHeader(Model item, int count) {
		sendRequest(mApp.getExperienceImpl().postDetail(mData), ModelExperiencePostDetail.class, REQUEST_GET,
				REFRESH_NEW);
	}

	@Override
	public void refreshFooter(Model item, int count) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getTheCacheType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getReallyList(Object object, Class type2) {
		if (object instanceof ModelExperiencePostDetail) {
			ModelExperiencePostDetail detail = (ModelExperiencePostDetail) object;
			return detail.getList();
		}
		return null;
	}
}
