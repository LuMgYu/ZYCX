package qcjlibrary.adapter;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;

import qcjlibrary.model.ModelNotifyNotice;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class NotifyAdapter extends BAdapter {

	public NotifyAdapter(BaseActivity activity, List<Model> list) {
		super(activity, list);
	}

	public NotifyAdapter(BaseFragment fragment, List<Model> list) {
		super(fragment, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_msg_notify, null);
			initView(convertView, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position);
		return convertView;
	}

	private void bindDataToView(ViewHolder holder, int position) {
		if (holder != null) {

			ModelNotifyNotice notice = (ModelNotifyNotice) mList.get(position);
			if (notice != null) {
				if (notice.getType().equals("answer")) {
					holder.tv_notify.setText("问答通知");
					holder.tv_notify_content.setText(notice.getAnswer_content());
				} else if (notice.getType().equals("weiba")) {
					holder.tv_notify.setText("经历小组通知");
					holder.tv_notify_content.setText(notice.getContent());
				} else if (notice.getType().equals("notice")) {
					holder.tv_notify.setText("系统通知");
					holder.tv_notify_content.setText(notice.getContent());
				}
				holder.tv_notify_date.setText(notice.getTime());
				String is_read = notice.getIs_read();
				if(is_read != null && is_read.equals("0")){
					holder.iv_praise_item.setVisibility(View.VISIBLE);
				} else{
					holder.iv_praise_item.setVisibility(View.GONE);
				}
			}

		}
	}

	/**
	 * 初始化布局
	 * 
	 * @param convertView
	 * @param holder
	 */
	private void initView(View convertView, ViewHolder holder) {
		if (convertView != null && holder != null) {
			holder.iv_msg_notify = (ImageView) convertView.findViewById(R.id.iv_msg_notify);
			holder.tv_notify = (TextView) convertView.findViewById(R.id.tv_notify);
			holder.tv_notify_content = (TextView) convertView.findViewById(R.id.tv_notify_content);
			holder.tv_notify_date = (TextView) convertView.findViewById(R.id.tv_notify_date);
			holder.iv_praise_item = (ImageView) convertView.findViewById(R.id.iv_praise_item);
		}
	}

	@Override
	public void refreshNew() {

		sendRequest(mApp.getNotifyImpl().noticelist(null), ModelNotifyNotice.class, REQUEST_GET, REFRESH_NEW);

	}

	@Override
	public void refreshHeader(Model item, int count) {
		refreshNew();
	}

	@Override
	public void refreshFooter(Model item, int count) {
		if(isLoading()){
			setLoading(false);
		}
		dismissTheProgress();
//		if(item instanceof ModelNotifyNotice){
//			ModelNotifyNotice data = (ModelNotifyNotice)item;
//			data.setLastid(data.getId());
//			sendRequest(mApp.getNotifyImpl().noticelist(data), ModelNotifyNotice.class, REQUEST_GET, REFRESH_FOOTER);
//		}
	}

	@Override
	public int getTheCacheType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		return DataAnalyze.parseData(str, class1);
	}

	@Override
	public Object getReallyList(Object object, Class type2) {
		if(isLoading()){
			setLoading(false);
		}
		return object;
	}

}
