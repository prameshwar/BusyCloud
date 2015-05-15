package com.gcm.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Result;

@WebServlet("/GCMNotification")
public class GCMNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Put your Google API Server Key here
	private static final String GOOGLE_SERVER_KEY = "AIzaSyCBmRkzGAv-5ijBJ4x8W2uBHRIUTvyuK40";
	static final String MESSAGE_KEY = "message";	

	public GCMNotification() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Result result=null;
		System.out.println("GCMNotification");
		
		String share = request.getParameter("shareRegId");

		// GCM RedgId of Android device to send push notification
		String regId = "";
		String mobile_number="";
		//if (share != null && !share.isEmpty()) {
			regId = request.getParameter("regId");
			mobile_number=request.getParameter("mobile_number");
			PrintWriter writer = new PrintWriter("E:/GC Messg/GCMRegId.txt");
			writer.println(regId);
			writer.println(mobile_number);
			boolean b=DataBaseConnector.getDataBaseConnector().insert(mobile_number, regId);
			writer.close();
			/*request.setAttribute("pushStatus", "GCM RegId Received.");
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);*/
	//	}
	/* else {

			try {
				BufferedReader br = new BufferedReader(new FileReader(
						"E:/GC Messg/GCMRegId.txt"));
				regId = br.readLine();
				br.close();
				String userMessage = request.getParameter("message");
				System.out.println("message is "+userMessage);
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				Message message = new Message.Builder().timeToLive(5000)
						.delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
				System.out.println("regId: " + regId);
				result = sender.send(message, regId, 1);
				request.setAttribute("pushStatus", result.toString());
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
		}*/
	}
}