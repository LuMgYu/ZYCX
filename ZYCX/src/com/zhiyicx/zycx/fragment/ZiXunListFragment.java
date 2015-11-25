package com.zhiyicx.zycx.fragment;

import qcjlibrary.adapter.ZhiXunAdapter;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.zycx.R;

/**
 * Created by Administrator on 2015/1/14.
 */
public class ZiXunListFragment extends BaseFragment {

	final private static String TAG = "ZiXunListFragment";
	private BAdapter mAdapter;
	private CommonListView mCommonListView;
	private int mType = 0;

	public static ZiXunListFragment newInstanse(int type) {
		ZiXunListFragment f = new ZiXunListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void initIntentData() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.listview_common_layout;
	}

	@Override
	public void initView() {
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		mAdapter = new ZhiXunAdapter(this, null);
		mCommonListView.setAdapter(mAdapter);
	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {

	}

	// @Override
	// public void onAttach(Activity activity) {
	// super.onAttach(activity);
	// }
	//
	// @Override
	// public OnTouchListListener getListView() {
	// return mListView;
	// }
	//
	// @Override
	// public void doRefreshHeader() {
	// if (mAdapter != null)
	// mAdapter.doRefreshHeader();
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// if (mCustView == null)
	// mCustView = inflater.inflate(R.layout.listview_common_layout,
	// container, false);
	// Bundle bundle = getArguments();
	// mType = bundle.getInt("type");
	// mListView = (CommonListView) mCustView
	// .findViewById(R.id.mCommonListView);
	// mListView.setOnItemListener(new LoadListView.OnItemListener() {
	// @Override
	// public void onClick(View view, int position, long id) {
	// Log.d(TAG, "get ZiXunlist item click position : " + position);
	// JSONObject jsonObject = mAdapter.getItem(position - 1);
	// try {
	// int _id = jsonObject.getInt("id");
	// int uid = jsonObject.getInt("uid");
	// String title = jsonObject.getString("title");
	// Intent intent = new Intent(mContext,
	// ZiXUnContentActivity.class);
	// intent.putExtra("id", _id);
	// intent.putExtra("uid", uid);
	// intent.putExtra("title", title);
	// // intent.putExtra("cid", mType);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_NEW_TASK);
	// mContext.startActivity(intent);
	// Anim.in(mContext);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// ArrayList<JSONObject> data = new ArrayList<JSONObject>();
	// mAdapter = new ZiXunListAdapter(this, data, mType);
	// mListView.setAdapter(mAdapter, System.currentTimeMillis(), mContext);
	// loadData(false);
	// return mCustView;
	// }
	//
	// @Override
	// public void loadData(boolean isLoadNew) {
	// if (mType == 0)
	// mAdapter.loadHomeInitData();
	// else
	// mAdapter.loadInitData();
	// mListView.setSelectionFromTop(0, 20);
	// }
	//
	// //
	// // mListView.setOnItemListener(new LoadListView.OnItemListener() {
	// // @Override
	// // public void onClick(View view, int position, long id) {
	// // Log.d(TAG, "get ZiXunlist item click position : " + position);
	// // JSONObject jsonObject = mAdapter.getItem(position - 1);
	// // try {
	// // int _id = jsonObject.getInt("id");
	// // int uid = jsonObject.getInt("uid");
	// // String title = jsonObject.getString("title");
	// // Intent intent = new Intent(mContext,
	// // ZiXUnContentActivity.class);
	// // intent.putExtra("id", _id);
	// // intent.putExtra("uid", uid);
	// // intent.putExtra("title", title);
	// // // intent.putExtra("cid", mType);
	// // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// // | Intent.FLAG_ACTIVITY_NEW_TASK);
	// // mContext.startActivity(intent);
	// // Anim.in(mContext);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// // }
	// // });
}
