package com.gcm.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.AllPermission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class SendNotification
 */
@WebServlet("/SendNotification")
public class SendNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public SendNotification() {
		super();
		// TODO Auto-generated constructor stub
	}
	// Put your Google API Server Key here
	private static final String GOOGLE_SERVER_KEY = "AIzaSyCBmRkzGAv-5ijBJ4x8W2uBHRIUTvyuK40";
	private static final String TOKKEN = "GCMGoodWillGCM";
	static final String MESSAGE_KEY = "message";	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Result result=null;
		System.out.println("SendNotification");

		String share = request.getParameter("shareRegId");

		// GCM RedgId of Android device to send push notification
		String regId = "";
		String mobile_number=request.getParameter("mobile_number");
		try {
			/*BufferedReader br = new BufferedReader(new FileReader("E:/GC Messg/GCMRegId.txt"));
			regId = br.readLine();
			br.close();*/
			String userMessage = request.getParameter("message").trim();
			System.out.println("message is "+userMessage);
			List<String> regId_list=DataBaseConnector.getDataBaseConnector().getRegId(mobile_number.trim());
			String allResult="";
			if(regId_list.size()>=1){
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				UUID uuid=UUID.randomUUID();
				String time=getCurrentTimeFormat("MM-dd HH:mm");
				String msgWithId=uuid.toString()+TOKKEN+userMessage+TOKKEN+time;
				Message message = new Message.Builder().timeToLive(5000).delayWhileIdle(true).addData(MESSAGE_KEY, msgWithId).build();
				Iterator<String> iterator=regId_list.iterator();
				int count=1;
				while(iterator.hasNext()){
					regId=iterator.next();
					System.out.println("\nMsgID "+(count)+" : "+ uuid.toString());
					System.out.println("\nregId "+(count)+" : "+ regId);
					result = sender.send(message, regId, 1);
					allResult+=result.toString()+"\n";
					System.out.println("Result "+(count)+" : "+ result.toString());
					count++;
				}
			}else{
				allResult="Mobile Number "+mobile_number+" is not registered.";
			}

			request.setAttribute("pushStatus", allResult);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			request.setAttribute("pushStatus",
					"RegId required: " + ioe.toString());
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("pushStatus", e.toString());
		}
		request.getRequestDispatcher("index.jsp")
		.forward(request, response);
	}
	
	private String getCurrentTimeFormat(String timeFormat){
		String time = "";
		SimpleDateFormat df = new SimpleDateFormat(timeFormat);
		Calendar c = Calendar.getInstance();
		time = df.format(c.getTime());
	
		return time;
		}


}
