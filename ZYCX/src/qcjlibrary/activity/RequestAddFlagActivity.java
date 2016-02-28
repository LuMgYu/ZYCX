package qcjlibrary.activity;

import java.util.ArrayList;
import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelRequestAsk;
import qcjlibrary.model.ModelRequestFlag;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;
import qcjlibrary.util.EditTextUtils;
import qcjlibrary.util.L;
import qcjlibrary.util.ToastUtils;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;

/**
 * author：qiuchunjia time：下午1:56:07 类描述：这个类是实现
 *
 */

public class RequestAddFlagActivity extends BaseActivity {
	private LinearLayout ll_add_flag;
	private EditText et_content;
	private TextView tv_add;
	private TextView tv_prompt;
	private List<ModelRequestFlag> mFlags = new ArrayList<ModelRequestFlag>();
	private String mAddFlags = "";
	private ModelRequestAsk mAsk;

	@Override
	public String setCenterTitle() {
		return "添加标签";
	}

	@Override
	public void initIntent() {
		mAsk = (ModelRequestAsk) getDataFromIntent(getIntent(), null);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_request_add_flag;
	}

	@Override
	public void initView() {
		ll_add_flag = (LinearLayout) findViewById(R.id.ll_add_flag);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_prompt = (TextView) findViewById(R.id.tv_prompt);
		titleSetRightTitle("提交");
	}

	private boolean isCommit = false;

	@Override
	public void initData() {
		sendRequest(mApp.getRequestImpl().getTopic(mAsk),
				ModelRequestFlag.class, REQUEST_GET);
		Title title = getTitleClass();
		title.tv_title_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFlag();
				mAsk.setTopics(mAddFlags);
				SociaxUIUtils.hideSoftKeyboard(getApplicationContext(), et_content);
				sendRequest(mApp.getRequestImpl().addQuestion(mAsk),
						ModelRequestAsk.class, REQUEST_GET);
				isCommit = true;
			}
		});
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		if (isCommit) {
			isCommit = false;
			Object object = DataAnalyze.parseDataByGson(str, class1);
			if (object instanceof ModelRequestAsk) {
				ModelRequestAsk ask = (ModelRequestAsk) object;
				mAsk.setQid(ask.getQid());
				mApp.startActivity_qcj(RequestAddFlagActivity.this,
						RequestSendTopicCommitedActivity.class,
						sendDataToBundle(mAsk, null));
			}
			judgeTheMsg(object);
			return object;
		}
		Object object = DataAnalyze.parseData(str, class1);
		if (object instanceof List<?>) {
			tv_prompt.setText("根据你的问题，推荐以下标签，最多选择3个:");
			mFlags = (List<ModelRequestFlag>) object;
			for (int i = 0; i < mFlags.size(); i++) {
				mFlags.get(i).setChoose(true); // 默认为选中
			}
			addDataToView(mFlags);
		} else if(object instanceof ModelMsg){
			ModelMsg msg = (ModelMsg) object;
			tv_prompt.setText("暂时没有相关问题的标签，请自己添加，最多添加3个:");
//			ToastUtils.showToast("暂时没有相关问题的标签，请自己添加");
		}
		return object;
	}

	@Override
	public void initListener() {
		tv_add.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add:
			String content = et_content.getText().toString();
			if (!TextUtils.isEmpty(content)) {
				if(!content.startsWith(" ")){
					//限制标签长度
					if(EditTextUtils.containsEmoji(content)){
						ToastUtils.showToast(this, "不可输入表情！");
						return;
					}
					if(content.length() > 10){
						ToastUtils.showLongToast(this, "不可超过10个字符");
						return;
					}
					//判断输入的标签是否已经存在
					boolean isExist = false;
					for (int i = 0; i < mFlags.size(); i++) {
						String flag = mFlags.get(i).getTitle();
						if(content.equals(flag)){
							isExist = true;
							ToastUtils.showLongToast(this, "该标签已经存在");
							return;
						}
					}
					if(!isExist){
						ModelRequestFlag flag = new ModelRequestFlag();
						flag.setTitle(content);
						if (mFlags != null) {
							mFlags.add(flag);
							addModelToView(flag);
						}
					}
					et_content.setText("");
					SociaxUIUtils.hideSoftKeyboard(getApplicationContext(), et_content);
				} else{
					ToastUtils.showToast(this, "内容输入不正确");
				}
			}
			break;

		}
	}

	/**
	 * 添加数据到界面
	 * 
	 * @param list
	 */
	private void addDataToView(List<ModelRequestFlag> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ModelRequestFlag flag = list.get(i);
				addModelToView(flag);
			}
		}
	}

	/**
	 * 添加单个model到view
	 * 
	 * @param i
	 * @param category
	 */
	private void addModelToView(ModelRequestFlag flag) {
		View view = mInflater.inflate(R.layout.item_request_add_flag, null);
		TextView tv_flag_name = (TextView) view.findViewById(R.id.tv_flag_name);
		ImageView iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
		tv_flag_name.setText(flag.getTitle());
		iv_choose.setTag(flag);
		countTheChooseAndDisplay(iv_choose);
		iv_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v instanceof ImageView) {
					ImageView imageView = (ImageView) v;
					countTheChooseAndDisplay(imageView);
				}
			}
		});
		ll_add_flag.addView(view);
	}

	/**
	 * 统计被选中的癌症并显示出来
	 * 
	 * @param imageView
	 */
	private void countTheChooseAndDisplay(ImageView imageView) {
		if (imageView != null) {
			ModelRequestFlag flag = (ModelRequestFlag) imageView.getTag();
			if (flag.isChoose()) {
				imageView.setImageResource(R.drawable.weixuanzhong02);
				flag.setChoose(false);
			} else {
				int count = 0; // 计算选择的癌症的个数
				for (int i = 0; i < mFlags.size(); i++) {
					if (mFlags.get(i).isChoose()) {
						count++;
					}
					if (count >= 3) {
						ToastUtils.showToast("最多只能选三个标签");
						return;
					}
				}
				imageView.setImageResource(R.drawable.xuanzhong02);
				flag.setChoose(true);
			}
		}
	}

	/**
	 * 获取癌症分类 用逗号隔开
	 */
	private boolean getFlag() {
		if (mFlags != null && mFlags.size() > 0) {
			for (int i = 0; i < mFlags.size(); i++) {
				if (mFlags.get(i).isChoose()) {
					mAddFlags = mAddFlags + mFlags.get(i).getTitle() + ",";
				}
			}
			if (!TextUtils.isEmpty(mAddFlags)) {
				mAddFlags = mAddFlags.substring(0, mAddFlags.length() - 1);
				return true;
			}
		}
		return false;
	}

}
