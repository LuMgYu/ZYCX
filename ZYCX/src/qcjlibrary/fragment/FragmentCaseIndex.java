package qcjlibrary.fragment;

import qcjlibrary.fragment.base.BaseFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午4:03:45 类描述：这个类是实现
 *
 */

public class FragmentCaseIndex extends BaseFragment {
	private RelativeLayout rl_mycase;
	private RelativeLayout rl_down;
	private RelativeLayout rl_history;
	private RelativeLayout rl_my;
	private TextView tv_name;
	private TextView tv_gender;
	private TextView tv_age;
	private TextView tv_update_day;
	private TextView tv_create_day;
	private TextView tv_look;
	private RelativeLayout rl_nodata;
	private TextView tv_edit;
	private LinearLayout ll_notify;
	private LinearLayout ll_food;

	@Override
	public void initIntentData() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_case_index;
	}

	@Override
	public void initView() {
		rl_mycase = (RelativeLayout) findViewById(R.id.rl_mycase);
		rl_down = (RelativeLayout) findViewById(R.id.rl_down);
		rl_history = (RelativeLayout) findViewById(R.id.rl_history);
		rl_my = (RelativeLayout) findViewById(R.id.rl_my);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_update_day = (TextView) findViewById(R.id.tv_update_day);
		tv_create_day = (TextView) findViewById(R.id.tv_create_day);
		tv_look = (TextView) findViewById(R.id.tv_look);
		rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		rl_mycase = (RelativeLayout) findViewById(R.id.rl_mycase);
		ll_notify = (LinearLayout) findViewById(R.id.ll_notify);
		ll_food = (LinearLayout) findViewById(R.id.ll_food);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		rl_mycase.setOnClickListener(this);
		rl_down.setOnClickListener(this);
		rl_history.setOnClickListener(this);
		tv_look.setOnClickListener(this);
		tv_edit.setOnClickListener(this);
		ll_notify.setOnClickListener(this);
		ll_food.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_mycase:

			break;

		case R.id.rl_down:

			break;
		case R.id.rl_history:

			break;
		case R.id.tv_look:

			break;
		case R.id.tv_edit:

			break;
		case R.id.ll_notify:

			break;
		case R.id.ll_food:

			break;

		}

	}

}
