package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.MeChooseAddressAdapter;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelMeAddress;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:33:01 类描述：这个类是实现
 *
 */

public class MeChooseProvinceActivity extends BaseActivity {
	private CommonListView mCommonListView;
	private BAdapter mAdapter;

	@Override
	public String setCenterTitle() {
		return "省份";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.listview_common_layout;
	}

	@Override
	public void initView() {
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		mCommonListView.setDividerHeight(0);
		mAdapter = new MeChooseAddressAdapter(this, new ModelMeAddress());
		mCommonListView.setAdapter(mAdapter);
		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ModelMeAddress address = (ModelMeAddress) parent
						.getItemAtPosition(position);
				address.setWholeAddress(address.getTitle() + " ");
				address.setWholeId(address.getArea_id() + ",");
				mCommonListView.stepToNextActivity(address,
						MeChooseCityActivity.class);
				MeChooseProvinceActivity.this.finish();
			}
		});
	}

	@Override
	public void initData() {
	}

	@Override
	public void initListener() {

	}

	@Override
	public void onClick(View v) {
	}

}