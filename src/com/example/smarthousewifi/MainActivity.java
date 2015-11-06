package com.example.smarthousewifi;

//import java.util.Locale;
//import java.util.Timer;



//import com.android.CarBluetoothControl.R;
//import com.example.android.BluetoothControl.BluetoothService;
//import com.example.android.BluetoothControl.BluetoothControl.SendTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends Activity {


    
    private static final int UDP_SERVER_PORT = 6667;
    private static final int UDP_LISTEN_PORT = 6666;
    private static final int REF_TIME = 1000;
	public int curMK = 3;
	public RecivTask rt;
	public Updater ref;
	public static InetAddress IPAddress;
    public int stat[][] = new int[10][10];
    public static int online[] = new int[10];
    public String[] sets = {"000000000000000", "000000000000000", "000000000000000", "000000000000000","000000000000000","000000000000000","000000000000000","000000000000000","000000000000000","000000000000000"};
    public String[] butt = {"0000", "0000", "0000", "0000","0000","0000","0000","0000","0000","0000"};
    public String[] rebs = {"0000", "0000", "0000", "0000","0000","0000","0000","0000","0000","0000"};
    
    
    
    public static final String[] r1names 
    	= {"", "Свет Б", "Люстра", "Реле1 ", "Реле1","Реле1","Реле1","Реле1","Реле1","Реле1"};
    public static final String[] r2names 
		= {"", "Свет М", "Лампа ", "Реле2 ", "Реле2","Реле2","Реле2","Реле2","Реле2","Реле2"};
    public static final String[] r3names 
		= {"", "Реле3 ", "Реле3 ", "Реле3", "Реле3","Реле3","Реле3","Реле3","Реле3","Реле3"};
    public static final String[] r4names 
		= {"", "Реле4 ", "Реле4 ", "Свет  ", "Реле4","Реле4","Реле4","Реле4","Реле4","Реле4"};
    
    
    
    public Switch r1, r2, r3, r4, b1, b2, b3, b4, dd1, dd2, rb1, rb2, rb3, rb4, rr1, rr2, rr3, rr4, out1, out2, trig1, trig2, trig3;
    public RelativeLayout l1;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


        for(int i=0; i<10; i++)
        	for(int j=0; j<10; j++)
        		stat[i][j]=0;
        for(int i=0; i<10; i++)
        	online[i]=-1;
        rt = new RecivTask();
        rt.execute();
        sendStr("clr");
        ref = new Updater(this);
        ref.start();
	}
	
	public void changeState(int charat){
		String buf="";
		for(int i = 0; i<15; i++){
			if (i==charat)
			{
				if (sets[curMK].charAt(charat)=='1')
					buf=buf+'0';
				else buf=buf+'1';
				continue;
			}
			buf=buf+sets[curMK].charAt(i);
		}
		sets[curMK]=buf;
	}        
	
	public void onStart() {
        super.onStart();
        l1 = (RelativeLayout) findViewById(R.id.layout);
        r1 = (Switch) findViewById(R.id.switch1);
        r2 = (Switch) findViewById(R.id.switch2);
        r3 = (Switch) findViewById(R.id.switch3);
        r4 = (Switch) findViewById(R.id.switch4);
        b1 = (Switch) findViewById(R.id.switch5);
        b2 = (Switch) findViewById(R.id.switch6);
        b3 = (Switch) findViewById(R.id.switch7);
        b4 = (Switch) findViewById(R.id.switch8);
        dd1 = (Switch) findViewById(R.id.dd1);
        dd2 = (Switch) findViewById(R.id.dd2);
        rr1 = (Switch) findViewById(R.id.switch13);
        rr2 = (Switch) findViewById(R.id.switch14);
        rr3 = (Switch) findViewById(R.id.switch15);
        rr4 = (Switch) findViewById(R.id.switch16);
        rb1 = (Switch) findViewById(R.id.switch9);
        rb2 = (Switch) findViewById(R.id.switch10);
        rb3 = (Switch) findViewById(R.id.switch11);
        rb4 = (Switch) findViewById(R.id.switch12);
        out1 = (Switch) findViewById(R.id.out1);
        out2 = (Switch) findViewById(R.id.out2);
        trig1 = (Switch) findViewById(R.id.trig1);
        trig2 = (Switch) findViewById(R.id.trig2);
        trig3 = (Switch) findViewById(R.id.trig3);
        //r1.setActivated(false);
        r1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                r1.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(0)=='0') sendStr("0"+curMK+"set1222222222"); 
        		else sendStr("0"+curMK+"set0222222222");
        		//changeState(0);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        r2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		r2.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(1)=='0') sendStr("0"+curMK+"set2122222222"); 
        		else sendStr("0"+curMK+"set2022222222");
        		//changeState(1);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        r3.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		r3.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(2)=='0') sendStr("0"+curMK+"set2212222222"); 
        		else sendStr("0"+curMK+"set2202222222");
        		//changeState(2);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        r4.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		r4.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(3)=='0') sendStr("0"+curMK+"set2221222222"); 
        		else sendStr("0"+curMK+"set2220222222");
        		//changeState(3);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        dd1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		dd1.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(8)=='0') sendStr("0"+curMK+"set2222222212"); 
        		else sendStr("0"+curMK+"set2222222202");
        		//changeState(8);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        dd2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		dd2.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(9)=='0') sendStr("0"+curMK+"set2222222221"); 
        		else sendStr("0"+curMK+"set2222222220");
        		//changeState(9);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        rr1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                rr1.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(4)=='0') sendStr("0"+curMK+"set2222122222"); 
        		else sendStr("0"+curMK+"set2222022222");
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        rr2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rr2.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(5)=='0') sendStr("0"+curMK+"set2222212222"); 
        		else sendStr("0"+curMK+"set2222202222");
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        rr3.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rr3.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(6)=='0') sendStr("0"+curMK+"set2222221222"); 
        		else sendStr("0"+curMK+"set2222220222");
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        rr4.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		rr4.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(7)=='0') sendStr("0"+curMK+"set2222222122"); 
        		else sendStr("0"+curMK+"set2222222022");
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        out1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		out1.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(10)=='0') sendStr("0"+curMK+"set222222222212"); 
        		else sendStr("0"+curMK+"set222222222202");
        		//changeState(8);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        out2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		out2.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(11)=='0') sendStr("0"+curMK+"set222222222221"); 
        		else sendStr("0"+curMK+"set222222222220");
        		//changeState(9);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        trig1.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		trig1.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(12)=='0') sendStr("0"+curMK+"set2222222222221"); 
        		else sendStr("0"+curMK+"set2222222222220");
        		//changeState(9);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        trig2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		trig2.toggle();
        		// TODO Auto-vgenerated method stub
        		if (sets[curMK].charAt(13)=='0') sendStr("0"+curMK+"set22222222222221"); 
        		else sendStr("0"+curMK+"set22222222222220");
        		//changeState(9);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
        trig3.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		trig3.toggle();
        		// TODO Auto-generated method stub
        		if (sets[curMK].charAt(14)=='0') sendStr("0"+curMK+"set222222222222221"); 
        		else sendStr("0"+curMK+"set222222222222220");
        		//changeState(9);
        		//Log.i(TAG, "НАЖАЛОСЬ!!");
        	}
        });
    }
	
	
	
	
	
	public void sendStr(String str)
	{
		final String str2=str;
		//System.out.println(str2);
		new Thread(new Runnable() {
            public void run() { 
            	String tx_msg = str2 + '\n';
        	    DatagramSocket ds = null;
        	    try {
        	        ds = new DatagramSocket();
        	        //InetAddress serverAddr = InetAddress.getByName("192.168.0.6");
        	        InetAddress serverAddr = IPAddress;
        	        //System.out.println("MESSAGE RECEIVED  "+serverAddr);
        	        DatagramPacket dp;
        	        dp = new DatagramPacket(tx_msg.getBytes(), tx_msg.length(), serverAddr, UDP_SERVER_PORT);
        	        ds.send(dp);
        	    } catch (SocketException e) {
        	        e.printStackTrace();
        	    }catch (UnknownHostException e) {
        	        e.printStackTrace();
        	    } catch (IOException e) {
        	        e.printStackTrace();
        	    } catch (Exception e) {
        	        e.printStackTrace();
        	    } finally {
        	        if (ds != null) {
        	            ds.close();
        	        }
        	    }

            }
        }).start(); 
	}
	
	
    public int checkString(String string, int from) {
        if (string == null || string.length() == 0) return -1;

        int i = from;

        char c;
        StringBuilder result=new StringBuilder(10);
        for (; i < string.length(); i++) {
            c = string.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return -1;
            }
            else
            	result.append(c);
        }
        return Integer.parseInt(result.toString());
    }
    
    
    public void refreshView() 
    {
    	if (sets[curMK].length()<10) return;
    	//Log.i(TAG, sets[curMK]);
    	if (sets[curMK].charAt(0)=='1') r1.setChecked(true); else r1.setChecked(false);
    	if (sets[curMK].charAt(1)=='1') r2.setChecked(true); else r2.setChecked(false);
    	if (sets[curMK].charAt(2)=='1') r3.setChecked(true); else r3.setChecked(false);
    	if (sets[curMK].charAt(3)=='1') r4.setChecked(true); else r4.setChecked(false);
    	if (sets[curMK].charAt(8)=='1') dd1.setChecked(true); else dd1.setChecked(false);
    	if (sets[curMK].charAt(9)=='1') dd2.setChecked(true); else dd2.setChecked(false);
    	if (butt[curMK].length()<4) return;
    	//Log.i(TAG, butt[curMK]);
    	if (butt[curMK].charAt(0)=='1') b1.setChecked(true); else b1.setChecked(false);
    	if (butt[curMK].charAt(1)=='1') b2.setChecked(true); else b2.setChecked(false);
    	if (butt[curMK].charAt(2)=='1') b3.setChecked(true); else b3.setChecked(false);
    	if (butt[curMK].charAt(3)=='1') b4.setChecked(true); else b4.setChecked(false);
    	TextView cap = (TextView) findViewById(R.id.tem);
    	if ((stat[curMK][1]==stat[curMK][2])&&(stat[curMK][1]==0))
    	{
    		cap.setText("-");
    		cap = (TextView) findViewById(R.id.hum);
    		cap.setText("-");
    	}	
    	else
    	{
    		cap.setText(""+stat[curMK][1]+"C");
    		cap = (TextView) findViewById(R.id.hum);
    		cap.setText(""+stat[curMK][2]+"%");
    	}
		cap = (TextView) findViewById(R.id.gas);
		cap.setText(""+stat[curMK][0]);
    	if (rebs[curMK].length()<4) return;
    	//Log.i(TAG, butt[curMK]);
    	if (rebs[curMK].charAt(0)=='1') rb1.setChecked(true); else rb1.setChecked(false);
    	if (rebs[curMK].charAt(1)=='1') rb2.setChecked(true); else rb2.setChecked(false);
    	if (rebs[curMK].charAt(2)=='1') rb3.setChecked(true); else rb3.setChecked(false);
    	if (rebs[curMK].charAt(3)=='1') rb4.setChecked(true); else rb4.setChecked(false);
    	//Log.i(TAG, butt[curMK]);
    	if (sets[curMK].charAt(4)=='1') rr1.setChecked(true); else rr1.setChecked(false);
    	if (sets[curMK].charAt(5)=='1') rr2.setChecked(true); else rr2.setChecked(false);
    	if (sets[curMK].charAt(6)=='1') rr3.setChecked(true); else rr3.setChecked(false);
    	if (sets[curMK].charAt(7)=='1') rr4.setChecked(true); else rr4.setChecked(false);
    	if (sets[curMK].charAt(10)=='1') out1.setChecked(true); else out1.setChecked(false);
    	if (sets[curMK].charAt(11)=='1') out2.setChecked(true); else out2.setChecked(false);
    	if (sets[curMK].charAt(12)=='1') trig1.setChecked(true); else trig1.setChecked(false);
    	if (sets[curMK].charAt(13)=='1') trig2.setChecked(true); else trig2.setChecked(false);
    	if (sets[curMK].charAt(14)=='1') trig3.setChecked(true); else trig3.setChecked(false);
    	if (online[curMK]==-1) this.l1.setBackgroundColor(Color.parseColor("#c19a9a"));
    	else this.l1.setBackgroundColor(Color.parseColor("#bad9b4"));
    	r1.setText(r1names[curMK]);
    	r2.setText(r2names[curMK]);
    	r3.setText(r3names[curMK]);
    	r4.setText(r4names[curMK]);
    }
    
    
	
	public int parseStat(String str)
	{
		boolean change=false;
		//Log.i(TAG, ""+str.length());
    	if (str.length()<31) return 0;
		  int sn=checkString(str.substring(0,1), 0);
		  if (sn<=0) return 0; 
		  //парсим массив настроек
		  //Log.i(TAG, str.substring(2,12));
		  if (sets[sn]!=str.substring(2,17))
		  {  
		    sets[sn]=str.substring(2,17); //Serial.print(sets[sn]);
		   
		    change=true;
		  }
		  //парсим датчик дыма
		  str=str.substring(18,str.length()); //Serial.print(str);
		  int pos=str.indexOf('/');
		  //Log.i(TAG, str);
		  if (pos==-1) return 0;
		  //if (abs(stat[sn][0]-str.substring(0,pos).toInt())>ACCURACY ) 
		  {
		    stat[sn][0]=checkString(str.substring(0,pos), 0); //Serial.print(stat[sn][0]);
		  //  change=true;
		  }
		  //парсим массив релейных кнопо
		  str=str.substring(pos+1,str.length()); //Serial.print(str);
		  //Log.i(TAG, str);
		  pos=str.indexOf('/');
		  if (pos==-1) return 0;
		  if (rebs[sn]!=str.substring(0,4))
		  {  
		    rebs[sn]=str.substring(0,4); //Serial.print(rebs[sn]);
		    change=true;
		  }
		  //парсим температуру
		  str=str.substring(5,str.length()); //Serial.print(str);
		  //Log.i(TAG, str);
		  pos=str.indexOf('/');
		  if (pos==-1) return 0;
		  if (stat[sn][1]!=checkString(str.substring(0,pos),0)) 
		  {
		    stat[sn][1]=checkString(str.substring(0,pos), 0); //Serial.print(stat[sn][1]);
		    change=true;
		  }
		  //парсим влажность
		  str=str.substring(pos+1,str.length()); //Serial.print(str);
		  //Log.i(TAG, str);
		  pos=str.indexOf('/');
		  if (pos==-1) return 0;
		  if (stat[sn][2]!=checkString(str.substring(0,pos), 0)) 
		  {
		    stat[sn][2]=checkString(str.substring(0,pos), 0); //Serial.print(stat[sn][2]);
		    change=true;
		  }
		  //парсим массив кнопок
		  str=str.substring(pos+1,str.length()); //Serial.print(str);
		  //Log.i(TAG, str);
		  pos=str.indexOf('/');
		  if (pos==-1) return 0;
		  if (butt[sn]!=str.substring(0,4))
		  {  
		    butt[sn]=str.substring(0,4); //Serial.print(butt[sn]);
		    change=true;
		  }
		  str=str.substring(5,str.length()); //Serial.print(str);
		  System.out.println(str);
		  if (str.length()>10) {parseStat(str);}
		  //System.out.println(":JGF");
		  return sn;
		  //if (change) return sn; else return -1;
	}
	    
	    
    @Override
    public synchronized void onPause() 
    {
    	//System.out.println("PAUSE");
        super.onPause();
    }

    @Override
    public void onStop() 
    {
    	//System.out.println("STOP");
    	System.exit(0);
    	super.onStop();
    }   
	    
    @Override
    public synchronized void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.

    }

    
    
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		TextView cap = (TextView) findViewById(R.id.room);
		switch (item.getItemId()) {
		case R.id.action_1:
			curMK=1;
			cap.setText(R.string.action_1);
			refreshView();
			return true;
		case R.id.action_2:
			curMK=2;
			cap.setText(R.string.action_2);
			refreshView();
			return true;
		case R.id.action_3:
			curMK=3;
			cap.setText(R.string.action_3);
			refreshView();
			return true;
		case R.id.action_4:
			curMK=4;
			cap.setText(R.string.action_4);
			refreshView();
			return true;
		case R.id.action_5:
			curMK=5;
			cap.setText(R.string.action_5);
			refreshView();
			return true;
		case R.id.action_6:
			curMK=6;
			cap.setText(R.string.action_6);
			refreshView();
			return true;
		case R.id.item1:
			//System.out.println("EXIT1");
			sendStr("time");
			refreshView();
			return true;
		case R.id.item2:
			//System.out.println("EXIT1");
			sendStr("temp");
			refreshView();
			return true;
		//case R.id.action_send:
		//	//System.out.println("EXIT1");
		//	sendStr("clr");
		//	refreshView();
		//	return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
	private class Updater extends Thread {
	    public boolean stopped = false;
	    public Activity obj;
	    Updater(Activity obj1){
	    	obj = obj1;
	    }
	    public void run() {
	        try {
	            while (!stopped) {
	            	obj.runOnUiThread(
	                    new Runnable() {
	                        public void run() {
	                            for(int i=0; i<10; i++)
	                            {
	                            	if (online[i]!=-1) online[i]++;
	                            	if (online[i]==7) {online[i]=-1; refreshView();}
	                            	//System.out.println(i+":"+online[i]);
	                            }
	                        }
	                    }
	                );
	                try {
	                    Thread.sleep(REF_TIME);
	                } catch (Exception e) {
	                }
	            }
	        } catch (Exception e) {
	        }
	    }
	}
	
	
	class RecivTask extends AsyncTask<Void, Void, Void> {

	    public boolean stopped = false;
	    @Override
	    protected Void doInBackground(Void... er) {
	        try {
	            while (!stopped) {
	            	 DatagramSocket serverSocket = new DatagramSocket(UDP_LISTEN_PORT);
	                 byte[] receiveData = new byte[1024];
	                // byte[] sendData = new byte[1024];
	                 while(true)
	                 {
	                     //sozdaetsja mesto dlja datagrami
	                         DatagramPacket receivePacket = new     DatagramPacket(receiveData,receiveData.length);
	                         //polu4aet datagramu
	                         serverSocket.receive(receivePacket);
	                        
	                         String sentence = new String(receivePacket.getData(),0,receivePacket.getLength());
	                         //polu4aet ip, port posilatelja
	                         IPAddress = receivePacket.getAddress();
	                         //System.out.println("MESSAGE RECEIVED "+IPAddress); 
	                         //int port = receivePacket.getPort();
	                        // Log.d("nessage received", "bla");
	                         int re = parseStat(sentence);
	                         if ((re>=0)&&(re<10)) online[re]=0;
	                         if (re==curMK)
	                           	publishProgress();
	                         //System.out.println("MESSAGE RECEIVED  "+sentence+"  "+IPAddress+"         "+port);
	                //mowno dobavitj serverSocket.closed 
	                         //refreshView();
	                 }
	            }
        	    return null;
	        } catch (Exception e) {
	            return null;
	        }
	    }
	    @Override
	    protected void onProgressUpdate(Void... a) {
	        super.onProgressUpdate();
	    	//System.out.println("MESSAGE RECEIVED  ");
	    	refreshView();
	    	//System.out.println("MESSAGE RECEIVED  ");
	        // TODO: check this.exception 
	        // TODO: do something with the feed
	    }
	}
	        
    public void onDestroy() {
    	super.onDestroy();
        // Stop the Bluetooth chat services
    }

    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	};
	
}
