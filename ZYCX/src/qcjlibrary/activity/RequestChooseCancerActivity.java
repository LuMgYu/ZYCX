package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午4:31:08 类描述：这个类是实现
 *
 */

public class RequestChooseCancerActivity extends BaseActivity {
	private LinearLayout ll_choose_cancer;

	@Override
	public String setCenterTitle() {
		return "选择癌种";
	}

	@Override
	public void initIntent() {

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

	}

	@Override
	public void initListener() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:

			break;

		default:
			break;
		}

	}
}