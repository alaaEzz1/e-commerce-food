package com.elmohandes.e_comercefood.notification;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyMessaging {

    //FirebaseMessaging.getInstance().subscribeToTopic("news");
    RequestQueue queue;
    String url = "https://fcm.googleapis.com/fcm/send";
    String server_key = "key=AAAAMUmBbco:APA91bHvz46mIlYWsu3ql4vZPiW5EUkB2SzZ6ysDJzZRyG8O1nNq8wjCuhhiKiS-Ntm17ALzCtrmWyNNJjuOTcYFnnpK_HFqbhQshFaZmyfd31J13Pq7IZQfKMCS9DS_1jt0JKsfv1IK";

    public void pushNotification(String topic , String title, String body, Context context){

        queue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+topic);
            JSONObject notificationObj = new JSONObject();//now json obj ready
            notificationObj.put("title",title);
            notificationObj.put("body",body);
            mainObj.put("notification",notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    mainObj,
                    response -> {
                        Log.d("notify","response");
                    }, error -> {
                        Log.d("notify","error :: " + error.getMessage());
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {

                    Map<String , String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization",server_key);

                    return params;
                }
            };
            queue.add(request);

        }catch (Exception e){
            Log.e("notify_error",e.getMessage());
        }
    }

}
