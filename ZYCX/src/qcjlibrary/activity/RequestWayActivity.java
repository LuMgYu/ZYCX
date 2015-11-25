package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.model.base.Model;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.HomeActivity;

/**
 * author：qiuchunjia time：下午4:31:08 类描述：这个类是实现
 *
 */

public class RequestWayActivity extends BaseActivity {
	private TextView tv_common;
	private TextView tv_zhuanye;

	@Override
	public String setCenterTitle() {
		return "提问方式";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_request_way;
	}

	@Override
	public void initView() {
		titleSetRightTitle("关闭");
		tv_common = (TextView) findViewById(R.id.tv_common);
		tv_zhuanye = (TextView) findViewById(R.id.tv_zhuanye);

	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.tv_title_right.setOnClickListener(this);

	}

	@Override
	public void initListener() {
		tv_zhuanye.setOnClickListener(this);
		tv_common.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			onBackPressed();
			break;

		case R.id.tv_common:
			mApp.startActivity_qcj(this, RequestSendTopicActivity.class,
					sendDataToBundle(new Model(), null));
			break;
		case R.id.tv_zhuanye:
			mApp.startActivity_qcj(this, RequestSendTopicActivity.class,
					sendDataToBundle(new Model(), null));
			break;
		}

	}
}
