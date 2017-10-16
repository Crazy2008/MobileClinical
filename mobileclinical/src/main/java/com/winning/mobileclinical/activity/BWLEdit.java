package com.winning.mobileclinical.activity;

import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("ShowToast")
public class BWLEdit extends Activity {

//	private LinearLayout container, luyinContainer;
//	private EditText note;
////	private ImageView tuya;
//	private Button cancleBtn, submitBtn;
//	private ProgressDialog proDialog;
//	private Intent intent;
//	private String lb,czlb;
	public final static int CAMERA_RESULT = 8888;
	public final static int TUYA_RESULT = 8889;
	public final static int ALBUM_RESULT = 8890;
//	private PopupWindow pop;
//	private ListView cameralist;
//	String mPhotoPath,mPhotoFileName;						//���ձ���·�����ļ���
//	boolean updateTitle,updateContent;
//	List <Boolean> updatePhoto=new ArrayList<Boolean>();
//	//¼�����
////	ProgressBar progressbar;
////	Button bwl_record;    									//¼����ť
////	LinearLayout file_container,bwl_palycontainer;			//��������
////	TextView bwl_filename,bwl_exectime;	    				//�ļ���ǰʱ����
////	boolean isPaulse=true,isfinish;      					//�Ƿ���ͣ
////	String totalText;
////	int totaltime,current;
//	
//	Memorandum memorandum=Memorandum.getInstance();
//	Handler UIhandler  = new Handler();
//	MediaModel recordMedia=new MediaModel();
//	TextWatcher watcher;
//	
//	private CheckBox bqshare;					//bwl001
////	private TextView fxr;						//bwl001
////	private TextView bqsharetext;				//bwl001
//	private boolean sdexiting;					//bwl002
//	private LinearLayout record_container;
////	private Button addtext;
//	private Button addaudio;
//	private Button addphoto;
//	private Button addsketch;
//	private AudioRecorder audioRecorder;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.bwleditnew);
//		if (Environment.getExternalStorageState().equals(
//				android.os.Environment.MEDIA_MOUNTED)) {
//			sdexiting = true;					//bwl002
//		} else {
//			sdexiting = false;					//bwl002
//			Toast.makeText(BWLEdit.this, "SD��������,����ʹ��Сͼģʽ", 400).show();
//			//Toast.makeText(BWLEdit.this, "SD��������", 400).show();
//			//finish();							//bwl002
//		}
//		init();  								//bwl002
//	}
//	
//	
//	/**
//	 * ��Դ�ļ���ɾ��ֻ�����ݿ���ڵ�ý����Դ��
//	 */
//	public void delete(final MediaModel media,final LinearLayout layout){
//		DialogHelper.showAlert(this, "��ʾ", "ȷ��Ҫɾ���� ��",R.drawable.error_icon, 
//				new dialogListener() {
//					@Override
//					public void onOk(Dialog dialog) {
//						//�ж���ݿ��Ƿ����
//						if(media.getId()!=null){
//							media.setState(3);
//						}else{
//							memorandum.getMediaList().remove(media);
//							
//							//ɾ���ļ�
//							File file=new File(getMkDir()+"/"+media.getFileName());
//							file.delete();
//						}
//						
//						if("1".equals(media.getLb())){			//ͼƬɾ��
//							container.removeView(layout);
//							
//						}else if("2".equals(media.getLb())){	//¼��ɾ��
////							findViewById(R.id.record_container).setVisibility(View.GONE);
////							findViewById(R.id.bwl_luyin_edit).setVisibility(View.GONE);
////							isPaulse=true;
////							edit.removeTextChangedListener(watcher);
////							recordMedia.setFileName(null);
////							//text��ʾ�����ļ�
////							bwl_filename.setText("�����ļ�");
////							bwl_exectime.setText("");
////							edit.setText("");
////							progressbar.setProgress(0);
//							
//						}else if("4".equals(media.getLb())){	//Ϳѻ
////							tuya.setImageBitmap(null);
//						}
//						dialog.dismiss();
//					}
//
//					@Override
//					public void oncancel(Dialog dialog) {
//						// TODO Auto-generated method stub
//						
//					}
//				}
//			);
//	} 
//	
//	
//	private void init() {
//		
//		intent = getIntent();
//		lb = intent.getExtras().getString("lb");
//		czlb = intent.getExtras().getString("czlb");
//		note = (EditText)findViewById(R.id.bwl_edit_nr);
////		title = (EditText) findViewById(R.id.bwl_edit_title);
//		//bwl001
//		bqshare = (CheckBox)findViewById(R.id.bwl_edit_bqshare);
////		fxr = (TextView)findViewById(R.id.bwl_edit_fxr);
////		bqsharetext = (TextView)findViewById(R.id.bwl_edit_bqsharetext);
//		//ͼƬ
//		container = (LinearLayout) findViewById(R.id.container);
//		
//		//Ϳѻ
////		tuya = (ImageView) findViewById(R.id.bwl_edit_tuya);
//		
//		record_container = (LinearLayout)findViewById(R.id.record_container);
//		
//		cancleBtn = (Button) findViewById(R.id.bwl_edit_cancle);
//		submitBtn = (Button) findViewById(R.id.bwl_edit_complete);
//		
//		addaudio = (Button)findViewById(R.id.bwl_addaudio);
//		addaudio.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				addAudio();
//			}
//		});
//		addphoto = (Button)findViewById(R.id.bwl_addphoto);
//		addphoto.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				addPhoto();
//			}
//		});
//		addsketch = (Button)findViewById(R.id.bwl_addsketch);
//		addsketch.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				intent = new Intent(BWLEdit.this, TuYaActivity.class);
//				startActivityForResult(intent, TUYA_RESULT);
//			}
//		});
//		
//		audioRecorder = (AudioRecorder)findViewById(R.id.bwl_audiorecorder);
//		audioRecorder.setRecorderListener( new AudioRecorderListener() {
//			
//			@Override
//			public void onStop(AudioRecorder recorder) {
//				AudioPlayer player = new AudioPlayer(BWLEdit.this);
//				player.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//				player.setPath(recorder.getPath());
//				record_container.addView(player);
//				View divider = new View(BWLEdit.this);
//				divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewUtil.diptopx(BWLEdit.this, 3)));
//				divider.setBackgroundColor(Color.TRANSPARENT);
//				record_container.addView(divider);
//				audioRecorder.setPath(null);
//				audioRecorder.setVisibility(View.GONE);
//			}
//			
//			@Override
//			public void onStart(AudioRecorder recorder) {}
//		});
//		
//		cancleBtn.setOnClickListener(btnLinstener);
//		submitBtn.setOnClickListener(btnLinstener);
//
//		final Bwl bwl = memorandum.getBwl();
//		//"edit".equals(czlb)
//		if (bwl!=null) {// �༭
//			note.setText(bwl.getContents());
////			title.setText(bwl.getTitle());
//			//bwl001
////			if("1".equals(bwl.getType()))
////			{
////				bqshare.setChecked(true);
////				fxr.setText("������:"+bwl.getCjhsname());
////			}
////			else
////			{
////				bqshare.setVisibility(View.INVISIBLE);
////				bqsharetext.setVisibility(View.INVISIBLE);
////				fxr.setVisibility(View.INVISIBLE);
////			}
//			//bwl001
//			//���£������ݱ䶯���м���
////			if(bwl.getXh()!=null){
////				note.addTextChangedListener(new TextWatcher() {
////					
////					@Override
////					public void onTextChanged(CharSequence s, int start, int before, int count) {
////						
////					}
////					
////					@Override
////					public void beforeTextChanged(CharSequence s, int start, int count,
////							int after) {
////						
////					}
////					
////					@Override
////					public void afterTextChanged(Editable s) {
////						bwl.setContents(note.getText().toString());
////						updateContent=true;
////					}
////				});
////				
////				title.addTextChangedListener(new TextWatcher() {
////					
////					@Override
////					public void onTextChanged(CharSequence s, int start, int before, int count) {
////					}
////					
////					@Override
////					public void beforeTextChanged(CharSequence s, int start, int count,
////							int after) {
////					}
////					
////					@Override
////					public void afterTextChanged(Editable s) {
////						bwl.setTitle(title.getText().toString());
////						updateTitle=true;
////					}
////				});
////			}
//			
//			
//			proDialog = ProgressDialog.show(BWLEdit.this, "", "���ڻ�ȡ���...",
//					true, false);
//			LoadThread load = new LoadThread();
//			new Thread(load).start();
//		}
////		else {
////			if (lb.equals("����")) {
////				takePhoto();
////			}
////			if (lb.equals("���")) {
////				getAlbum();
////			}
////			if (lb.equals("����")) {
////				note.requestFocus();
////			}
////			if (lb.equals("Ϳѻ")) {
////
////				/*if (MultiModel.getModel().getTuyaPath() != null
////						&& !MultiModel.getModel().getTuyaPath().equals("")) {
////					File f = new File(MultiModel.getModel().getTuyaPath());
////					f.delete();
////				}*/
////				
////				intent = new Intent(BWLEdit.this, TuYaActivity.class);
////				startActivityForResult(intent, TUYA_RESULT);
////
////			}
////			if (lb.equals("¼��")) {
////				LuyiThread luyi = new LuyiThread();
////				new Thread(luyi).start();
////			}
////		}
//
//	}
//
//	
//	protected void addPhoto() {
//		View layout = LayoutInflater.from(BWLEdit.this).inflate(R.layout.camera_check_layout, null);
//		cameralist = (ListView) layout.findViewById(R.id.camera_ListView);
//
//		// ����������
//		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
//
//		HashMap<String, Object> map0 = new HashMap<String, Object>();
//		map0.put("listitem_image", R.drawable.pop_image);
//		map0.put("listitem_text", "����");
//		listItem.add(map0);
//		HashMap<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("listitem_image", R.drawable.pop_gallery);
//		map1.put("listitem_text", "���");
//		listItem.add(map1);
//		// �����������Item�������Ӧ��Ԫ��
//		SimpleAdapter listItemAdapter = new SimpleAdapter(BWLEdit.this, listItem,R.layout.camera_list_layout, 
//				new String[] {"listitem_image", "listitem_text" },
//				new int[] { R.id.listitem_image,R.id.listitem_text });
//
//		cameralist.setAdapter(listItemAdapter);
//		cameralist.setOnItemClickListener(new OnItemClickListener() {
//					public void onItemClick(android.widget.AdapterView<?> parent,View view, int position, long id) {
//						switch (position) {
//						case 0:
//							takePhoto();
//							break;
//						case 1:
//							getAlbum();
//							break;
//						}
//						if (pop != null) {
//							pop.dismiss();
//						}
//					};
//				});
//
//		pop = new PopupWindow(layout, 200,LayoutParams.WRAP_CONTENT);
//		pop.setBackgroundDrawable(new BitmapDrawable());
//		pop.setFocusable(true);
//		pop.setOutsideTouchable(true);
//		pop.update();
//
//		if (pop.isShowing()) {
//			pop.dismiss();
//		} else {
//			pop.showAsDropDown(addphoto, 0, 15);
//		}
//	}
//
//
//	protected void addAudio() {
//		String mRecordDir = getMkDir()+ "/" + getRecordFileName();
//		audioRecorder.setPath(mRecordDir);
//		audioRecorder.setVisibility(View.VISIBLE);
//		audioRecorder.start();
//	}
//
//
//	@SuppressLint("HandlerLeak")
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			int flag = msg.arg1;
//			
//			if (flag == 0) {
//				Toast.makeText(BWLEdit.this, "�ϴ�ʧ��", Toast.LENGTH_LONG)
//						.show();
//			}
//			
//			if (flag == 1) { // ���·������򱾵ص�bwl��ݿ�
//				int saveType=memorandum.getBwl().getSave_type();
//				
//				if(saveType==1){
//					BwlLocalSubmit bwlThread = new BwlLocalSubmit();
//					new Thread(bwlThread).start();
//				}else{
//					BwlServerSubmit bwlThread = new BwlServerSubmit();
//					new Thread(bwlThread).start();
//				}
//				
//			}
//			
//			if (flag == 2) { // ����ɹ�����³ɹ�
//				deleteFileByType(1);
//				memorandum.clearData();
//				Intent intent = new Intent(BWLEdit.this, BWLNew.class);
//				setResult(BWLNew.EDIT_RESULT, intent);
//				BWLEdit.this.finish();
//			}
//			if (flag == 3) { // download ͼƬ����Ƶ
//				
//				//��������Ƶ����Ҫ����
//				
//				DownloadThread download = new DownloadThread();
//				new Thread(download).start();
//			}
//			if (flag == 4) { // ���������������ļ�
//				int lb = msg.arg2;
//				if (lb == 2) {
////					final MediaModel m = (MediaModel) msg.obj;
////					bwl_filename.setText((String) m.getFileName());
////					edit.setText(m.getDescription());
////					edit.setTag(m.getId());
////					edit.setBackgroundResource(R.drawable.bwl_input);
////					
////					//��ʾ¼�����
//////					takeRecord(m);
//					
//				} else {
//					final MediaModel m = (MediaModel) msg.obj;
//					byte[] data = m.getData();
//					if (data != null) {
//						BitmapFactory.Options opts = new BitmapFactory.Options();
//						opts.inJustDecodeBounds = true;
//						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
//								data.length,opts);
//						opts.inJustDecodeBounds = false;
//						int scale = (int) (opts.outWidth / (float) 320);//BWL002
//						if (scale <= 0)//BWL002
//						{
//							scale = 1;//BWL002
//						}
//						opts.inSampleSize = scale;//BWL002
//						
//						bitmap = BitmapFactory.decodeByteArray(data, 0,
//								data.length,opts);
//						
//						if (lb == 1) {
//							final LinearLayout layout = new LinearLayout(
//									BWLEdit.this);
//							layout.setOrientation(LinearLayout.VERTICAL);
//
//							ImageView image = new ImageView(BWLEdit.this);
//							image.setAdjustViewBounds(true);
//							/*image.setLayoutParams(new LayoutParams(
//									LayoutParams.MATCH_PARENT,
//									LayoutParams.WRAP_CONTENT));*/
//							image.setLayoutParams(new LayoutParams(
//									LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//							image.setPadding(2, 2, 2, 2);
//							image.setImageBitmap(bitmap);
//							layout.addView(image);
//							final EditText et = new EditText(BWLEdit.this);
//							et.setTag(m.getId());
//							et.setHint("ע��");
//							et.setHintTextColor(Color.LTGRAY);
//							et.setBackgroundResource(R.drawable.bwl_input);
//							et.setText(m.getDescription());
//							
//							image.setOnLongClickListener(new OnLongClickListener() {
//								
//								@Override
//								public boolean onLongClick(View v) {
//									delete(m,layout);
//									return false;
//								}
//							});
//							
//							et.addTextChangedListener(new TextWatcher() {
//								
//								@Override
//								public void onTextChanged(CharSequence s, int start, int before, int count) {
//									m.setDescription(et.getText().toString());
//								}
//								
//								@Override
//								public void beforeTextChanged(CharSequence s, int start, int count,
//										int after) {
//									
//								}
//								
//								@Override
//								public void afterTextChanged(Editable s) {
//									m.setDescription(et.getText().toString());
//									m.setState(2);
//								}
//							});
//
//							layout.addView(et);
//							layout.setPadding(0, 5, 0, 5);
//							container.addView(layout);
//							container.setVisibility(View.VISIBLE);
//						}
////						if (lb == 4) {
////							tuya.setImageBitmap(bitmap);
////							
////							tuya.setOnLongClickListener(new OnLongClickListener() {
////								
////								@Override
////								public boolean onLongClick(View v) {
////									delete(m,null);
////									return false;
////								}
////							});
////						}
//					}
//				}
//
//			}
//
//			if (flag == 5) {
////				takeRecord(recordMedia);
//			}
//			if (flag == 6) {
//				Toast.makeText(BWLEdit.this, "û������Ҫ��������", Toast.LENGTH_LONG)
//						.show();
//			}
//			
//		}
//	};
//
//	//����ͼƬ��Ϳѻ��ʼ��
//	public void initLocalPicView(String type,final MediaModel m){
//		
//		String path=m.getSrc()+m.getFileName();
//		 BitmapFactory.Options options=new BitmapFactory.Options(); 
//		 options.inJustDecodeBounds = false; 
//		 options.inSampleSize = 2;
//		 Bitmap bitmap= BitmapFactory.decodeFile(path,options);
//		
//		if ("1".equals(type)) {
//			final LinearLayout layout = new LinearLayout(
//					BWLEdit.this);
//			layout.setOrientation(LinearLayout.VERTICAL);
//
//			ImageView image = new ImageView(BWLEdit.this);
//			image.setAdjustViewBounds(true);
//			image.setLayoutParams(new LayoutParams(
//					LayoutParams.MATCH_PARENT,
//					LayoutParams.WRAP_CONTENT));
//			image.setPadding(2, 2, 2, 2);
//			image.setImageBitmap(bitmap);
//			layout.addView(image);
//			EditText et = new EditText(BWLEdit.this);
//			et.setTag(m.getId());
//			et.setHint("ע��");
//			et.setHintTextColor(Color.LTGRAY);
//			et.setBackgroundResource(R.drawable.bwl_input);
//			et.setText(m.getDescription());
//			
//			//����ɾ��
//			image.setOnLongClickListener(new OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View v) {
//					delete(m,layout);
//					return false;
//				}
//			});
//			
//			et.addTextChangedListener(new TextWatcher() {
//				
//				@Override
//				public void onTextChanged(CharSequence s, int start, int before, int count) {
//					
//				}
//				
//				@Override
//				public void beforeTextChanged(CharSequence s, int start, int count,
//						int after) {
//				}
//				
//				@Override
//				public void afterTextChanged(Editable s) {
//					m.setDescription(s.toString());
//					m.setState(2);
//				}
//			});
//			
//			layout.addView(et);
//			layout.setPadding(0, 5, 0, 5);
//			container.addView(layout);
//			container.setVisibility(View.VISIBLE);
//		}
////		if ("4".equals(type)) {
////			tuya.setImageBitmap(bitmap);
////			
////			tuya.setOnLongClickListener(new OnLongClickListener() {
////				
////				@Override
////				public boolean onLongClick(View v) {
////					delete(m,null);
////					return false;
////				}
////			});
////			//MultiModel.getModel().setTuYaOld(bitmap);
////		}
//		
//	}
//	
//	class LoadThread implements Runnable {
//
//		@Override
//		public void run() {
//			
//			//���ز��Ҷ�Ӧmedia
//			Bwl bwl=memorandum.getBwl();
//			int saveType=bwl.getSave_type();
//			if(saveType==1){
//				List<MediaModel> list = BwlLocalAction.getMediaId(BWLEdit.this,bwl.getXh());
//				memorandum.setMediaList(list);
//				
//				//��ʼ����Ƶ���
//				if(list != null && list.size() > 0){
//					for(final MediaModel media:memorandum.getMediaList()){
//						final String type=media.getLb();
//						
//						//ͼƬ��Ϳѻ
//						handler.post(new Runnable() {
//							
//							@Override
//							public void run() {
//								if("1".equals(type)){
//									initLocalPicView(type,media);
//								}
//								if("4".equals(type)){
//									initLocalPicView(type,media);
//								}
//								if("2".equals(type)){
//									
////									bwl_filename.setText(media.getFileName());
////									edit.setText(media.getDescription());
////									edit.setTag(media.getId());
////									edit.setBackgroundResource(R.drawable.bwl_input);
//									
//									//��ʾ¼�����
////									takeRecord(media);
//								}
//							}
//						});
//						
//					}
//				}
//				proDialog.dismiss();
//				
//			}else{
//				//��ȡ��������Ƶ�ļ�
//				List<MediaModel>  list = BwlAction.getMediaId(BWLEdit.this,bwl.getXh());
//				memorandum.setMediaList(list);
//				Message msg = new Message();
//				if (list != null && list.size() > 0) {
//					msg.arg1 = 3;
//				} else {
//					proDialog.dismiss();
//					return;
//				}
//				handler.sendMessage(msg);
//			}
//			
//		}
//
//	}
//
//	class DownloadThread implements Runnable {
//
//		@Override
//		public void run() {
//			for (MediaModel media:memorandum.getMediaList()) {
//				String path = media.getSrc()+media.getFileName();
//				InputStream in = BwlAction.downLoad(path);
//				String lb = media.getLb().toString().trim();
//				if (in == null) {
//					continue;
//				} else {
//					if (lb.equals("1")) {
//						setDownloadJPGValue(media, in);
//					}
//					if (lb.equals("4")) {
//						setDownloadJPGValue(media, in);
//					}
//					if (lb.equals("2")) {
//						setDownloadRecordValue(media, in);
//					}
//				}
//			}
//			proDialog.dismiss();
//		}
//
//		/*
//		 * ��������������ͼƬ���
//		 */
//		private void setDownloadJPGValue(MediaModel media, InputStream in) {
//			Message msg = new Message();
//			String fileName=media.getFileName();
//			
//			if (fileName!=null) {
//				// String fileNamePath = getMkDir() + "/" + fileName;
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				byte[] b = new byte[4 * 1024];
//				byte[] data = null;
//				int len = 0;
//				try {
//					while ((len = in.read(b)) != -1) {
//						bos.write(b, 0, len);
//					}
//					bos.flush();
//					data = bos.toByteArray();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} finally {
//					try {
//						if (in != null) {
//							in.close();
//						}
//						if (bos != null) {
//							bos.close();
//						}
//					} catch (IOException e2) {
//						e2.printStackTrace();
//					}
//				}
//				media.setData(data);
//				msg.obj =media;
//				msg.arg1 = 4;
//				msg.arg2 = Integer.parseInt(media.getLb().trim());
//
//				handler.sendMessage(msg);
//			} else {
//				Toast.makeText(BWLEdit.this, "����ʧ��!", 300).show();
//				proDialog.dismiss();
//			}
//		}
//
//	}
//
//	/*
//	 * ��������������¼���ļ�
//	 */
//	private void setDownloadRecordValue(MediaModel media, InputStream in) {
//		Message msg = new Message();
//		String fileName=media.getFileName();
//		
//		
//		if (fileName != null) {
//			String recordDir = getMkDir();
//			String path=recordDir+"/"+fileName;
//
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			byte[] b = new byte[4 * 1024];
//			byte[] data = null;
//			int len = 0;
//			try {
//				while ((len = in.read(b)) != -1) {
//					bos.write(b, 0, len);
//				}
//				bos.flush();
//				data = bos.toByteArray();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					if (in != null) {
//						in.close();
//					}
//					if (bos != null) {
//						bos.close();
//					}
//				} catch (IOException e2) {
//					e2.printStackTrace();
//				}
//			}
//
//			File f = new File(path);
//			FileOutputStream fos = null;
//			try {
//				fos = new FileOutputStream(f);
//				fos.write(data, 0, data.length);
//				fos.flush();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					if (fos != null) {
//						fos.close();
//						fos = null;
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			media.setFileName(fileName);
//			msg.obj = media;
//			msg.arg1 = 4;
//			msg.arg2 = Integer.parseInt(media.getLb().trim());
//			handler.sendMessage(msg);
//			proDialog.dismiss();
//		} else {
//			// Toast.makeText(BWLEdit.this, "����ʧ��!", 300).show();
//			proDialog.dismiss();
//		}
//	}
//
//	/*
//	 * ����
//	 */
//	private void takePhoto() {
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		if(sdexiting)		
//		{					
//			String mPhotoDir = getMkDir();
//			mPhotoFileName = getPhotoFileName();
//			mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
//			File mPhotoFile = new File(mPhotoPath);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
//		}					
//		startActivityForResult(intent, CAMERA_RESULT);
//	}
//
//	/*
//	 * �������ա�Ϳѻ����
//	 */
//	@SuppressLint("NewApi")
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {//bwl002
//			/* bwl002 start */
//			Bitmap bitmap = null;
//			if(sdexiting)		
//			{
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				opts.inJustDecodeBounds = true;
//				bitmap = BitmapFactory.decodeFile(mPhotoPath, opts);
//				int scale = (int) (opts.outWidth / (float) 320);
//				if (scale <= 0)
//				{
//					scale = 1;
//				}
//				opts.inSampleSize = scale;
//				opts.inJustDecodeBounds = false;
//				bitmap = BitmapFactory.decodeFile(mPhotoPath, opts);	
//			}
//			else
//			{
//				String mPhotoDir = getMkDir();
//				mPhotoFileName = getPhotoFileName();
//				mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
//				
//				Bundle bundle = data.getExtras();  
//				bitmap = (Bitmap) bundle.get("data");  
//				FileOutputStream fos = null;
//				try {
//					fos = new FileOutputStream(mPhotoPath);
//					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//					fos.flush();
//					fos.close(); 
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			/* bwl002 end */
//			if (bitmap != null) 
//			{
//				addImage(bitmap);
//			} else {
//				memorandum.clearData();
//				Intent intent = new Intent();
//				intent.setClass(BWLEdit.this, FirstActivity.class);
//				startActivity(intent);
//				finish();
//			}
//			
//		} else if (requestCode == TUYA_RESULT) {// Ϳѻ����
//			if (data != null) {
//				if (data.getExtras().getString("tuyaPath") != null) {
//					String path = data.getExtras().getString("tuyaPath");
//					Bitmap bitmap = BitmapFactory.decodeFile(path);
//					if (bitmap != null) {
//						addImage(bitmap);
//					}
//				}
//			}
//
//		}
//		/* bwl002 start */
//		else if (requestCode == ALBUM_RESULT && resultCode == RESULT_OK) {
//			Uri uri = data.getData();
//			ContentResolver cr = this.getContentResolver(); 
//			Bitmap bitmap = null;
//			String mPhotoDir = getMkDir();
//			mPhotoFileName = getPhotoFileName();
//			mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
//			
//			
//			FileOutputStream fos = null;
//			try {
//				//ͼƬ������
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				//���ͼƬ�ĳ���
//				opts.inJustDecodeBounds = true;
//				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
//				//���ѹ������
//				int scale = (int) (opts.outWidth / (float) 320);
//				if (scale <= 0)
//				{
//					scale = 1;
//				}
//				//����ѹ������
//				opts.inSampleSize = scale;
//				opts.inJustDecodeBounds = false;
//				//��ȡͼƬ
//				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
//				fos = new FileOutputStream(mPhotoPath);
//				//��ѹ����ͼƬ������nurseĿ¼
//				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//				fos.flush();
//				fos.close(); 
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if (bitmap != null) 
//			{
//				addImage(bitmap);
////				final LinearLayout layout = new LinearLayout(this);
////				layout.setOrientation(LinearLayout.VERTICAL);
////				ImageView image = new ImageView(this);
////				image.setAdjustViewBounds(true);
////				image.setBackgroundColor(Color.parseColor("#ffffff"));
////				/*image.setLayoutParams(new LayoutParams(
////						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));*/
////				image.setLayoutParams(new LayoutParams(
////						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
////				image.setPadding(2, 2, 2, 2);
////				image.setImageBitmap(bitmap);
////				
////				final MediaModel media=new MediaModel();
////				media.setLb("1");
////				media.setFileName(mPhotoFileName);
////				media.setSrc(getMkDir()+"/");
////				media.setState(1);						//����
////				memorandum.getMediaList().add(media);
////				
////				
////				image.setOnLongClickListener(new OnLongClickListener() {
////					
////					@Override
////					public boolean onLongClick(View v) {
////						delete(media,layout);
////						return false;
////					}
////				});
////				
////				EditText edit = new EditText(this);
////				edit.setBackgroundResource(R.drawable.bwl_input);
////				edit.addTextChangedListener(new TextWatcher() {
////					
////					@Override
////					public void onTextChanged(CharSequence s, int start, int before, int count) {
////						
////					}
////					
////					@Override
////					public void beforeTextChanged(CharSequence s, int start, int count,
////							int after) {
////						
////					}
////					
////					@Override
////					public void afterTextChanged(Editable s) {
////						media.setDescription(s.toString());
////					}
////				});
////				
////				layout.addView(image);
////				layout.addView(edit);
////				layout.setPadding(0, 5, 0, 5);
////				container.addView(layout);
////				container.setVisibility(View.VISIBLE);
//			}
//		}/* bwl002 end */
//	}
//	
//	//���ͼƬ  ���ա���ᡢͿѻ
//	private void addImage(Bitmap bitmap){
//		
//		ImageView image = new ImageView(this);
//		image.setAdjustViewBounds(true);
//		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		image.setPadding(2, 2, 2, 2);
//		image.setImageBitmap(bitmap);
//		
//		container.addView(image);
//		
//		final MediaModel media=new MediaModel();
//		media.setLb("1");
//		media.setFileName(mPhotoFileName);
//		media.setSrc(getMkDir()+"/");
//		media.setState(1);						//����
//		memorandum.getMediaList().add(media);
//		
//	}
//
//	/*
//	 * ��ȡ���ͼƬ
//	 */
//	private void getAlbum() {
//		
//		Intent intent = new Intent("android.intent.action.PICK");
//		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//		String mPhotoDir = getMkDir();
//		mPhotoFileName = getPhotoFileName();
//		mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
//		File mPhotoFile = new File(mPhotoPath);
//		startActivityForResult(intent, ALBUM_RESULT);						//bwl002
//	}
//
//	OnClickListener btnLinstener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			
//			if(audioRecorder.isRecording()){
//				Toast.makeText(BWLEdit.this, "����¼�����뱣�浱ǰ¼����", Toast.LENGTH_LONG).show();
//				return;
//			}
//			
//			if (v == cancleBtn) {
//				deleteFileByType(2);
//				memorandum.clearData();
//				finish();
//			}
//			if (v == submitBtn) {
//				
//				proDialog = ProgressDialog.show(BWLEdit.this, "",
//						"���ڴ������...", true, true);
//				 
//				SubmitThread submit = new SubmitThread();
//				new Thread(submit).start();
//			}
//
//		}
//
//	};
//
//	/*
//	 * ɾ����ļ�:(1)�ύ  (2)����
//	 * 1���������ύ�򷵻أ�
//	 *    ÿ�λ����أ�ȫ��ɾ��
//	 * 2�����ط��أ�
//	 *    ���أ�ֻɾ�������ġ�          
//	 * 3�������ύ��ɾ��������ڴ˴����?
//	 * 
//	 * ����type:
//	 * 1���ύ   2������
//	 */
//	private void deleteFileByType(int type) {
//		Bwl bwl=memorandum.getBwl();
//		int saveType=1;
//		if(bwl!=null){
//			saveType=bwl.getSave_type();
//		}
//		
//		List<MediaModel> mediaList=memorandum.getMediaList();
//		if(mediaList!=null&&mediaList.size()>0){
//			for(MediaModel media:mediaList){
//				String path=getMkDir()+"/"+media.getFileName();
//				if(saveType==1&&type==2&&media.getState()==1){		//���ر���¼���ز���
//					new File(path).delete();
//				}else if(saveType==2){								//����˱���¼�ύ�򷵻�
//					new File(path).delete();
//				}
//			}
//		}
//		
//	}
//
//	/*
//	 * �ύ
//	 */
//	class SubmitThread implements Runnable {
//
//		@Override
//		public void run() {
//			HTTPGetTool upload = HTTPGetTool.getTool();
//			Map<String, String> strMap = new HashMap<String, String>();
//			Map<String, File> fileMap = new HashMap<String, File>();
//			
//			Bwl bwl=memorandum.getBwl();
//
//			if (bwl!=null) {// ��������
//				
//				//���ظ���
//				if(bwl.getSave_type()==1){
//					handler.post(new BwlLocalSubmit());
//				}else{
//					//����������
//					handler.post(new BwlServerSubmit());
//					
//				}
//				
//			} else { // ���ϴ�,ֻ��ӵ�������ݿ�
//				if(bqshare.isChecked())						
//				{											
//					//���������ϴ�							
//					handler.post(new BwlServerSubmit());	
//				}										
//				else										
//				{											
//					handler.post(new BwlLocalSubmit());		
//				}											
//			}
//		}
//	}
//	
//	class BwlServerSubmit implements Runnable{
//
//		@Override
//		public void run() {
//			List <MediaModel> meidaList=memorandum.getMediaList();
//			Bwl bwl=memorandum.getBwl();
//			Map<String, File> fileMap = new HashMap<String, File>();
//			/* BWL001 start */
//			if(bwl == null)
//			{
//				int bwlid = BwlAction.addBwl(BWLEdit.this,note.getText().toString().trim(), 
//											"".trim(),"1");
//				if(meidaList.size()>0){
//					for(int i=0;i<meidaList.size();i++){
//						MediaModel media=meidaList.get(i);
//						if(media.getState()==1){	//����
//							String path=media.getSrc()+media.getFileName();
//							//ͼ��
//							if("1".equals(media.getLb())){
//								fileMap.put("photo" + i, new File(path));
//							}
//							//¼��
//							if("2".equals(media.getLb())){
//								fileMap.put("record", new File(path));
//							}
//							//Ϳѻ
//							if("4".equals(media.getLb())){
//								fileMap.put("tuya", new File(path));
//							}
//							HTTPGetTool upload = HTTPGetTool.getTool();
//							JSONObject json = upload.post(WebUtilsHOST	+ WebUtils.UPLOAD, fileMap);
//							
//							try {
//								//�ϴ��ɹ���ͬ����ݿ�
//								if(json.get("success").equals("true")){
//									//
//									if("1".equals(media.getLb())){
//										BwlAction.upLoad(BWLEdit.this,"1", "����", 
//												  "D:/nurse/photo/",
//												  bwlid+"", 
//												  media.getDescription(),
//												  media.getFileName());
//									}else if("2".equals(media.getLb())){
//										BwlAction.upLoad(BWLEdit.this,"2", "¼��", 
//												"D:/nurse/record/",
//												  bwlid+"", 
//												  media.getDescription(),
//												  media.getFileName());
//									}else if("4".equals(media.getLb())){
//										BwlAction.upLoad(BWLEdit.this,"4", "Ϳѻ", 
//												"D:/nurse/tuya/",
//												  bwlid+"", 
//												  media.getDescription(),
//												  media.getFileName());
//									}
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}else if(media.getState()==2){	//�޸�
//							BwlAction.upLoadUpdateDescription(BWLEdit.this,media.getDescription(),media.getId());
//							
//						}else if(media.getState()==3){	//ɾ��
//							BwlAction.deleteMedia(BWLEdit.this,media.getId());
//						}
//					}
//				}
//				
//				Message msg = new Message();
//				msg.arg1 = 2;
//				msg.arg2 = 1;
//				handler.sendMessage(msg);
//				proDialog.dismiss();
//			}
//			/* BWL001 end */
//			else
//			{
//				/* BWL001 start */
//				Nurse nurse=GlobalCache.getCache().getNurse();
//				if(nurse != null && !bwl.getCjhsid().trim().equals(nurse.getId().trim()))
//				{
//					Toast.makeText(BWLEdit.this, "����ʧ��,�Ǵ����߱��˲���", 400).show();
//					proDialog.dismiss();
//					return;
//				}
//				/* BWL001 end */
//				/* BWL004 start */
//				if (updateTitle||updateContent) {
//					BwlAction.updateBwl(BWLEdit.this,bwl.getContents(), 
//								   bwl.getTitle(),bwl.getXh());
//				}
//				/* BWL004 end */
//				if(meidaList.size()>0){
//					for(int i=0;i<meidaList.size();i++){
//						MediaModel media=meidaList.get(i);
//						if(media.getState()==1){	//����
//							String path=media.getSrc()+media.getFileName();
//							//ͼ��
//							if("1".equals(media.getLb())){
//								fileMap.put("photo" + i, new File(path));
//							}
//							//¼��
//							if("2".equals(media.getLb())){
//								fileMap.put("record", new File(path));
//							}
//							//Ϳѻ
//							if("4".equals(media.getLb())){
//								fileMap.put("tuya", new File(path));
//							}
//							HTTPGetTool upload = HTTPGetTool.getTool();
//							JSONObject json = upload.post(WebUtilsHOST	+ WebUtils.UPLOAD, fileMap);
//							
//							try {
//								//�ϴ��ɹ���ͬ����ݿ�
//								if(json.get("success").equals("true")){
//									//
//									if("1".equals(media.getLb())){
//										BwlAction.upLoad(BWLEdit.this,"1", "����", 
//												  "D:/nurse/photo/",
//												  bwl.getXh(), 
//												  media.getDescription(),
//												  media.getFileName());
//									}else if("2".equals(media.getLb())){
//										BwlAction.upLoad(BWLEdit.this,"2", "¼��", 
//												"D:/nurse/record/",
//												  bwl.getXh(), 
//												  media.getDescription(),
//												  media.getFileName());
//									}else if("4".equals(media.getLb())){
//										BwlAction.upLoad(BWLEdit.this,"4", "Ϳѻ", 
//												"D:/nurse/tuya/",
//												  bwl.getXh(),
//												  media.getDescription(),
//												  media.getFileName());
//									}
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}else if(media.getState()==2){	//�޸�
//							BwlAction.upLoadUpdateDescription(BWLEdit.this,media.getDescription(),media.getId());
//							
//						}else if(media.getState()==3){	//ɾ��
//							BwlAction.deleteMedia(BWLEdit.this,media.getId());
//						}
//					}
//				}
//				
//				Message msg = new Message();
//				msg.arg1 = 2;
//				msg.arg2 = 1;
//				handler.sendMessage(msg);
//				proDialog.dismiss();
//				
//			}
//		}
//		
//	}
//
//	//����¼���ر�������
//	class BwlLocalSubmit implements Runnable {
//
//		@Override
//		public void run() {
//			
//			Bwl bwl=memorandum.getBwl();
//			try {
//				// �ж�����ӻ��Ǹ���
//				if (bwl!=null) {
//					// note����title����
//					if (updateTitle||updateContent) {
//						BwlLocalAction.updateBwl(BWLEdit.this,bwl.getContents(), 
//									   bwl.getTitle(),bwl.getXh());
//					}
//					
//					//1����������  2������ 3��ɾ��
//					List <MediaModel> meidaList=memorandum.getMediaList();
//					if(meidaList.size()>0){
//						for(MediaModel media:meidaList){
//							
//							if(media.getState()==1){		//����
//								if("1".equals(media.getLb())){
//									BwlLocalAction.upLoad(BWLEdit.this,"1", "����", 
//											  getMkDir()+"/",
//											  bwl.getXh(), 
//											  media.getDescription(),
//											  media.getFileName());
//								}else if("2".equals(media.getLb())){
//									BwlLocalAction.upLoad(BWLEdit.this,"2", "¼��", 
//											  getMkDir()+"/",
//											  bwl.getXh(), 
//											  media.getDescription(),
//											  media.getFileName());
//								}else if("4".equals(media.getLb())){
//									BwlLocalAction.upLoad(BWLEdit.this,"4", "Ϳѻ", 
//											  getMkDir()+"/",
//											  bwl.getXh(),
//											  media.getDescription(),
//											  media.getFileName());
//								}
//								
//							}else if(media.getState()==2){	//�����޸�
//								BwlLocalAction.upLoadUpdateDescription(BWLEdit.this, 
//										media.getDescription(), media.getId());
//								
//								
//							}else if(media.getState()==3){	//ɾ��			
//								BwlLocalAction.deleteMedia(BWLEdit.this, media.getId());
//								File file=new File(getMkDir()+"/"+media.getFileName());
//								file.delete();
//							}
//						}
//					}
//					Message msg = new Message();
//					msg.arg1 = 2;
//					msg.arg2 = 1;
//					handler.sendMessage(msg);
//					proDialog.dismiss();
//					
//				} else {
//					int bwlid=BwlLocalAction.addBwl(BWLEdit.this,note.getText().toString().trim(), 
//	   												"".trim());
//					if(bwlid>0){
//						List <MediaModel> meidaList=memorandum.getMediaList();
//						if(meidaList.size()>0){
//							for(MediaModel media:meidaList){
//								if("1".equals(media.getLb())){
//									BwlLocalAction.upLoad(BWLEdit.this,"1", "����", 
//											  getMkDir()+"/",
//											  bwlid + "", 
//											  media.getDescription(),
//											  media.getFileName());
//								}else if("2".equals(media.getLb())){
//									BwlLocalAction.upLoad(BWLEdit.this,"2", "¼��", 
//											  getMkDir()+"/",
//											  bwlid + "", 
//											  media.getDescription(),
//											  media.getFileName());
//								}else if("4".equals(media.getLb())){
//									BwlLocalAction.upLoad(BWLEdit.this,"4", "Ϳѻ", 
//											  getMkDir()+"/",
//											  bwlid + "",
//											  media.getDescription(),
//											  media.getFileName());
//								}
//							}
//						}
//						
//						Message msg = new Message();
//						msg.arg1 = 2;
//						msg.arg2 = 1;
//						handler.sendMessage(msg);
//						proDialog.dismiss();
//					}else{
//						Toast.makeText(BWLEdit.this, "����ʧ��", 400).show();
//						Message msg = new Message();
//						msg.arg1 = 2;
//						msg.arg2 = 2;
//						handler.sendMessage(msg);
//						proDialog.dismiss();
//					}
//				}
//
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//
//	// ��ȡ�ļ���ͼƬ��
//	private String getPhotoFileName() {
//		Date date = new Date(System.currentTimeMillis());
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"'IMG'_yyyyMMdd_HHmmss");
//		return dateFormat.format(date) + ".jpg";
//	}
//
//	// ��ȡ¼���ļ���
//	private String getRecordFileName() {
//		Date date = new Date(System.currentTimeMillis());
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"'RECORD'_yyyyMMdd_HHmmss");
//		return dateFormat.format(date) + ".3gp";
//	}
//
//	// ��ȡ�ļ���
//	private String getMkDir() {
//		String saveDir = "";
//		if(sdexiting) {
//			 saveDir = Environment.getExternalStorageDirectory() + "/nurse";
//		}
//		else
//		{
//			saveDir = "/mnt/sdcard-ext/nurse";
//		}
//		File dir = new File(saveDir);
//		if (!dir.exists()) {
//			dir.mkdir(); // �����ļ���
//		}
//		return saveDir;
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		
//		//�ͷ���Ƶ������
//		for(int i = 0;i<record_container.getChildCount();i++){
//			if(record_container.getChildAt(i) instanceof AudioPlayer){
//				((AudioPlayer)record_container.getChildAt(i)).releasePlayer();
//			}
//		}					//bwl003
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
//	/*
//	 * ѹ��ͼƬ���ҽ�SDCardԭ��ͼƬ�滻
//	 */
//	private Bitmap yaSuo(Bitmap bitmap, String mPhotoPath) {
//		Matrix matrix = new Matrix();
//		matrix.postScale(0.3f, 0.25f);
//		Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//				bitmap.getHeight(), matrix, true);
//		File f = new File(mPhotoPath);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		temp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		byte[] data = baos.toByteArray();
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(f);
//			fos.write(data, 0, data.length);
//			fos.flush();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (fos != null) {
//					fos.close();
//					fos = null;
//				}
//				if (baos != null) {
//					baos.close();
//					baos = null;
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return temp;
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			deleteFileByType(2);
//			MultiModel.getModel().clearData();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	public TextWatcher addWatcher(final MediaModel media){
//		watcher=new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				media.setDescription(s.toString().trim());
//				if(media.getState()!=1){
//					media.setState(2);
//				}
//			}
//		};
//		return watcher;
//	}
//	
	
}
