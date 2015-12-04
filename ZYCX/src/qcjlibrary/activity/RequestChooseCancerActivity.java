package qcjlibrary.activity;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.model.ModelCancerCategory;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelRequest;
import qcjlibrary.model.ModelRequestAsk;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.ToastUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午4:31:08 类描述：这个类是实现
 *
 */

public class RequestChooseCancerActivity extends BaseActivity {
	private LinearLayout ll_choose_cancer;
	private List<ModelCancerCategory> mList;
	private boolean[] mChooseArray; // 统计是否选择
	private ModelRequestAsk mAsk;
	private String mCancerId = ""; // 癌症种类 用逗号隔开

	@Override
	public String setCenterTitle() {
		return "选择癌种";
	}

	@Override
	public void initIntent() {
		mAsk = (ModelRequestAsk) getDataFromIntent(getIntent(), null);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_request_choose_cancer;
	}

	@Override
	public void initView() {
		titleSetRightTitle("提交");
		ll_choose_cancer = (LinearLayout) findViewById(R.id.ll_choose_cancer);

	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.tv_title_right.setOnClickListener(this);
		sendRequest(mApp.getRequestImpl().index(null), ModelRequest.class, 0);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelRequest) {
			ModelRequest request = (ModelRequest) object;
			mList = request.getFenlei();
			addDataToView(mList);
		}
		if (judgeTheMsg(object)) {
			mApp.startActivity_qcj(this,
					RequestSendTopicCommitedActivity.class,
					sendDataToBundle(new Model(), null));
		}
		return object;
	}

	/**
	 * 添加数据到界面
	 * 
	 * @param list
	 */
	private void addDataToView(List<ModelCancerCategory> list) {
		if (list != null) {
			mChooseArray = new boolean[list.size()];
			for (int i = 0; i < list.size(); i++) {
				ModelCancerCategory category = list.get(i);
				View view = mInflater
						.inflate(R.layout.item_choose_cancer, null);
				TextView tv_cancer = (TextView) view
						.findViewById(R.id.tv_cancer);
				ImageView iv_choose = (ImageView) view
						.findViewById(R.id.iv_choose);
				tv_cancer.setText(category.getTitle());
				iv_choose.setTag(i);
				iv_choose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v instanceof ImageView) {
							ImageView imageView = (ImageView) v;
							countTheChooseAndDisplay(imageView);
						}
					}
				});
				ll_choose_cancer.addView(view);
			}
		}
	}

	/**
	 * 统计被选中的癌症并显示出来
	 * 
	 * @param imageView
	 */
	private void countTheChooseAndDisplay(ImageView imageView) {
		if (imageView != null) {
			int flag = (Integer) imageView.getTag();
			if (mChooseArray[flag]) {
				imageView.setImageResource(R.drawable.weixuanzhong02);
				mChooseArray[flag] = false;
			} else {
				int count = 0; // 计算选择的癌症的个数
				for (int i = 0; i < mChooseArray.length; i++) {
					if (mChooseArray[i]) {
						count++;
					}
					if (count >= 3) {
						ToastUtils.showToast("最多只能选三个癌种");
						return;
					}
				}
				imageView.setImageResource(R.drawable.xuanzhong02);
				mChooseArray[flag] = true;
			}
		}
	}

	@Override
	public void initListener() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			if (getCancerId()) {
				mAsk.setCid(mCancerId);
				ToastUtils.showToast("正在提交");
				sendRequest(mApp.getRequestImpl().addQuestion(mAsk),
						ModelMsg.class, REQUEST_GET);
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 获取癌症分类 用逗号隔开
	 */
	private boolean getCancerId() {
		if (mChooseArray != null) {
			for (int i = 0; i < mChooseArray.length; i++) {
				if (mChooseArray[i]) {
					mCancerId = mCancerId + mList.get(i).getId() + ",";
				}
			}
			if (mCancerId != null) {
				mCancerId = mCancerId.substring(0, mCancerId.length() - 1);
				return true;
			}
		}
		ToastUtils.showToast("请选择癌症分类！");
		return false;
	}
}
