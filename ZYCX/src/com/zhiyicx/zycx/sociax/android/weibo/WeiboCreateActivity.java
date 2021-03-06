package com.zhiyicx.zycx.sociax.android.weibo;

import java.io.File;
import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.android.ThinksnsAbscractActivity;
import com.zhiyicx.zycx.sociax.api.ApiStatuses;
import com.zhiyicx.zycx.sociax.component.CustomTitle;
import com.zhiyicx.zycx.sociax.component.LoadingView;
import com.zhiyicx.zycx.sociax.component.RightIsButton;
import com.zhiyicx.zycx.sociax.component.TSFaceView;
import com.zhiyicx.zycx.sociax.concurrent.Worker;
import com.zhiyicx.zycx.sociax.exception.ApiException;
import com.zhiyicx.zycx.sociax.exception.UpdateException;
import com.zhiyicx.zycx.sociax.exception.VerifyErrorException;
import com.zhiyicx.zycx.sociax.modle.Weibo;
import com.zhiyicx.zycx.sociax.unit.Compress;
import com.zhiyicx.zycx.sociax.unit.ImageUtil;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;
import com.zhiyicx.zycx.sociax.unit.WordCount;

@SuppressLint("HandlerLeak")
public class WeiboCreateActivity extends ThinksnsAbscractActivity {
	private static final String TAG = "WeiboCreate";
	private static EditText edit;
	private static Worker thread;
	private static Handler handler;
	private static LoadingView loadingView;
	private static ImageView camera;
	private static ImageView preview;
	private ImageView face;
	private TSFaceView tFaceView;
	private static RelativeLayout btnLayout;

	private RelativeLayout rl_left_1;
	private TextView tv_title_right;
	private TextView tv_title;

	private static final int CAMERA = 0;
	private static final int LOCATION = 1;
	private static final int AT_REQUEST_CODE = 3;
	private static final int TOPIC_REQUEST_CODE = 4;

	private Image image;
	private boolean hasImage = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreateNoTitle(savedInstanceState);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();
	}

	private void initView() {
		tFaceView = (TSFaceView) findViewById(R.id.face_view);
		edit = (EditText) findViewById(R.id.send_content);
		loadingView = (LoadingView) findViewById(LoadingView.ID);
		camera = (ImageView) findViewById(R.id.camera);
		preview = (ImageView) findViewById(R.id.preview);
		face = (ImageView) findViewById(R.id.face);
		btnLayout = (RelativeLayout) findViewById(R.id.btn_layout);
		this.setInputLimit();
		if (image == null)
			image = new Image();
		setBottomClick();
		checkIntentData();
		edit.clearFocus();
		tFaceView.setFaceAdapter(mFaceAdapter);
		/**** qcj添加title 并初始化 *********/
		rl_left_1 = (RelativeLayout) findViewById(R.id.rl_left_1);
		tv_title_right = (TextView) findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(View.VISIBLE);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("发布");
		tv_title_right.setText("提交");
		rl_left_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		tv_title_right.setOnClickListener(getRightListener());
		/**** qcj添加title end *********/

	}

	private void checkIntentData() {
		if (!getIntentData().containsKey("type"))
			return;
		int temp = getIntentData().getInt("type");
		if (temp > 0) {
			return;
		}
		String type = getIntentData().getString("type");
		if (type.equals("suggest")) {
			edit.setText("#Android建议反馈#");
		}
		if (type.equals("joinTopic")) {
			edit.setText(getIntentData().getString("topic"));
		}
		if (getIntent().getStringExtra("type") != null) {
			String t = getIntent().getStringExtra("type");
			if (t.equals("suggest")) {
				edit.setText("#Android建议反馈# ");
				edit.setSelection(edit.length());
			}
		}
	}

	String savePath;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bitmap btp = null;
			switch (requestCode) {
			case CAMERA:
				/*
				 * Bundle dataBundle = data.getExtras(); if(dataBundle == null){
				 * Uri imageUri = data.getData();
				 * System.out.println(imageUri.toString()); try { btp =
				 * MediaStore.Images.Media.getBitmap(this.getContentResolver(),
				 * imageUri); } catch (FileNotFoundException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } catch
				 * (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); System.out.println(e.toString()); }
				 * }else{ //btp = (Bitmap)dataBundle.get("data"); }
				 */

				// data.putExtra(MediaStore.EXTRA_OUTPUT, btp);
				try {
					// image.setImagePath(ImageUtil.saveFile(picName , btp));

					btp = Compress.compressPicToBitmap(new File(image
							.getImagePath()));
				} catch (Exception e) {
					Log.e(TAG, "file saving..." + e.toString());
				}
				break;
			case LOCATION:
				btp = checkImage(data);
				break;
			case AT_REQUEST_CODE:
				if (data != null)
					edit.append("@" + data.getStringExtra("at_name").toString()
							+ " ");
				edit.setSelection(edit.length());
				break;
			case TOPIC_REQUEST_CODE:
				if (data != null)
					edit.append("#"
							+ data.getStringExtra("recent_topic").toString()
							+ "# ");
				edit.setSelection(edit.length());
				break;
			}
			if (btp != null) {
				btp = Bitmap.createScaledBitmap(btp, preview.getWidth(),
						btnLayout.getHeight(), true);
				preview.setImageBitmap(btp);
				this.hasImage = true;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("finally")
	private Bitmap checkImage(Intent data) {
		Bitmap bitmap = null;
		try {
			Uri originalUri = data.getData();
			String path = getRealPathFromURI(originalUri);
			// path = path.substring(path.indexOf("/sdcard"), path.length());
			Log.d(TAG, "imagePath" + path);
			bitmap = Compress.compressPicToBitmap(new File(path));
			if (bitmap != null) {
				image.setImagePath(path);
			}
		} catch (Exception e) {
			Toast.makeText(WeiboCreateActivity.this, "图片加载失败",
					Toast.LENGTH_SHORT).show();
			Log.e("checkImage", e.getMessage());
		} finally {
			return bitmap;
		}
	}

	// MediaColumns.DATA
	@SuppressWarnings("deprecation")
	private String getRealPathFromURI(Uri contentUri) {
		Cursor cursor = null;
		String result = contentUri.toString();
		String[] proj = { MediaColumns.DATA };
		cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor == null)
			throw new NullPointerException("reader file field");
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径
			result = cursor.getString(column_index);
			try {
				// 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
				if (Integer.parseInt(Build.VERSION.SDK) < 14) {
					cursor.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "获取相册图片error:" + e.toString());
			}
		}
		return result;
	}

	private void setBottomClick() {
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(WeiboCreateActivity.this)
						.setTitle("选择来源").setItems(R.array.camera, image)
						.show();
			}
		});

		findViewById(R.id.topic).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// new TopicHelper(ThinksnsCreate.this, edit).insertTopicTips();
				Intent intent = new Intent(WeiboCreateActivity.this,
						AtTopicActivity.class);
				startActivityForResult(intent, TOPIC_REQUEST_CODE);
			}
		});

		findViewById(R.id.at).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// new AtHelper(ThinksnsCreate.this, edit).insertAtTips();
				Intent intent = new Intent(WeiboCreateActivity.this,
						AtContactActivity.class);
				startActivityForResult(intent, AT_REQUEST_CODE);
			}
		});

		face.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tFaceView.getVisibility() == View.GONE) {
					tFaceView.setVisibility(View.VISIBLE);
					face.setImageResource(R.drawable.key_bar);
					SociaxUIUtils.hideSoftKeyboard(WeiboCreateActivity.this,
							edit);
				} else if (tFaceView.getVisibility() == View.VISIBLE) {
					tFaceView.setVisibility(View.GONE);
					face.setImageResource(R.drawable.face_bar);
					SociaxUIUtils.showSoftKeyborad(WeiboCreateActivity.this,
							edit);
				}
			}
		});

		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tFaceView.getVisibility() == View.VISIBLE) {
					tFaceView.setVisibility(View.GONE);
					face.setImageResource(R.drawable.key_bar);
					SociaxUIUtils.showSoftKeyborad(WeiboCreateActivity.this,
							edit);
				}
			}
		});
	}

	private class Image implements DialogInterface.OnClickListener {
		private String imagePath = "";

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case LOCATION:
				locationImage();
				break;
			case CAMERA:
				cameraImage();
				break;
			default:
				dialog.dismiss();
			}
		}

		// 获取相机拍摄图片
		private void cameraImage() {
			if (!ImageUtil.isHasSdcard()) {
				Toast.makeText(WeiboCreateActivity.this, "使用相机前先插入SD卡",
						Toast.LENGTH_LONG).show();
				return;
			}
			// 启动相机
			Intent myIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			String picName = System.currentTimeMillis() + ".jpg";
			try {
				String path = ImageUtil.saveFilePaht(picName);
				File file = new File(path);
				Uri uri = Uri.fromFile(file);
				image.setImagePath(path);
				myIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			} catch (FileNotFoundException e) {
				Log.e(TAG, "file saving...");
			}
			startActivityForResult(myIntent, CAMERA);
		}

		private void locationImage() {
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/*");
			startActivityForResult(Intent.createChooser(getImage, "选择照片"),
					LOCATION);

		}

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}
	}

	private void setInputLimit() {
		TextView overWordCount = (TextView) findViewById(R.id.overWordCount);
		WordCount wordCount = new WordCount(edit, overWordCount);
		overWordCount.setText(wordCount.getMaxCount() + "");
		edit.addTextChangedListener(wordCount);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.create_new;
	}

	@Override
	public OnClickListener getRightListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit.getText().toString().trim().length() > 140) {
					Toast.makeText(getApplicationContext(),
							R.string.word_limit, Toast.LENGTH_SHORT).show();
				} else {
					camera.setEnabled(false);
					/*** qcj 注释 **/
					// getCustomTitle().getRight().setEnabled(false);
					/*** qcj 注释end **/
					// sendingButtonAnim(getCustomTitle().getRight());
					Thinksns app = (Thinksns) WeiboCreateActivity.this
							.getApplicationContext();
					thread = new Worker(app, "Publish data");
					handler = new ActivityHandler(thread.getLooper(),
							WeiboCreateActivity.this);
					Message msg = handler.obtainMessage();
					handler.sendMessage(msg);
				}
			}
		};
	}

	@Override
	protected CustomTitle setCustomTitle() {
		return new RightIsButton(this, this.getString(R.string.sendMessage));
	}

	@Override
	public int getRightRes() {
		return R.drawable.button_send;
	}

	@Override
	public String getTitleCenter() {
		return this.getString(R.string.publish);
	}

	UIHandler uHandler = new UIHandler();

	private final class ActivityHandler extends Handler {
		@SuppressWarnings("unused")
		private Context context;

		public ActivityHandler(Looper looper, Context context) {
			super(looper);
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			loadingView.show(edit);

			// 获取数据
			Thinksns app = thread.getApp();
			ApiStatuses statuses = app.getStatuses();
			try {
				boolean update = false;
				String editContent;

				if (hasImage) {
					editContent = edit.getText().toString().trim().length() == 0 ? "发布图片"
							: edit.getText().toString().trim();
				} else {
					editContent = edit.getText().toString().trim();
				}
				Weibo weibo = new Weibo();
				weibo.setContent(editContent);
				if (hasImage) {
					update = statuses.upload(weibo,
							new File(image.getImagePath()));
					checkSendResult(update);
				} else {
					if (editContent.length() == 0) {
						loadingView.error("微博不能为空", edit);
						loadingView.hide(edit);
						Message uiMsg = uHandler.obtainMessage();
						uiMsg.what = 1;
						uHandler.sendMessage(uiMsg);
						// clearSendingButtonAnim(getCustomTitle().getRight());
						// getCustomTitle().getRight().clearAnimation();
						// Log.e("temp","temp"+getCustomTitle().getRight());
						// getCustomTitle().getRight().setClickable(true);
					} else {
						update = statuses.update(weibo) >= 1;
						checkSendResult(update);
					}
				}

			} catch (VerifyErrorException e) {
				// clearSendingButtonAnim(getCustomTitle().getRight());
				loadingView.error(e.getMessage(), edit);
			} catch (ApiException e) {
				// clearSendingButtonAnim(getCustomTitle().getRight());
				loadingView.error(e.getMessage(), edit);
			} catch (UpdateException e) {
				// clearSendingButtonAnim(getCustomTitle().getRight());
				loadingView.error(e.getMessage(), edit);
			}
			thread.quit();
		}
	}

	private void checkSendResult(boolean update) {
		if (update) {
			getIntentData().putString(TIPS, "发布成功");
			loadingView.error("发布成功", edit);
			WeiboCreateActivity.this.finish();
		} else {
			// clearSendingButtonAnim(getCustomTitle().getRight());
			loadingView.error("发布失败", edit);
		}
	}

	class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				getCustomTitle().getRight().setEnabled(true);
				break;
			default:
				break;
			}
		}
	}

	private TSFaceView.FaceAdapter mFaceAdapter = new TSFaceView.FaceAdapter() {

		@Override
		public void doAction(int paramInt, String paramString) {
			// TODO Auto-generated method stub
			EditText localEditBlogView = WeiboCreateActivity.edit;
			int i = localEditBlogView.getSelectionStart();
			int j = localEditBlogView.getSelectionStart();
			String str1 = "[" + paramString + "]";
			String str2 = localEditBlogView.getText().toString();
			SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
			localSpannableStringBuilder.append(str2, 0, i);
			localSpannableStringBuilder.append(str1);
			localSpannableStringBuilder.append(str2, j, str2.length());
			SociaxUIUtils.highlightContent(WeiboCreateActivity.this,
					localSpannableStringBuilder);
			localEditBlogView.setText(localSpannableStringBuilder,
					TextView.BufferType.SPANNABLE);
			localEditBlogView.setSelection(i + str1.length());
			Log.v("Tag", localEditBlogView.getText().toString());
		}
	};

}
