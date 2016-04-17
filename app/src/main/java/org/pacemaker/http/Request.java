package org.pacemaker.http;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


/**
 * This class imploys the half synch half async pattern and is used with PacemakerAPI to obtain
 * values via rest
 */

@SuppressWarnings("rawtypes")
public abstract class Request extends AsyncTask<Object, Void, Object> {
    public Response responder;
    public ProgressDialog dialog;
    public Context context;
    public String message;
    public Exception error;

    /**
     * Default Contructor for the request
     *
     * @param context
     * @param responder
     * @param message
     */
    public Request(Context context, Response responder, String message) {
        this.responder = responder;
        this.context = context;
        this.message = message;
    }

    /**
     * Method ran before execution
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(context, 1);
        this.dialog.setMessage(message);
        this.dialog.show();
    }

    /**
     * Method ran in the background when processing request
     *
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object... params) {
        error = null;
        try {
            return doRequest(params);
        } catch (Exception e) {
            error = e;
        }
        return null;
    }

    /**
     * Method called when performing a request
     *
     * @param params
     * @return
     * @throws Exception
     */
    protected abstract Object doRequest(Object... params) throws Exception;

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (error != null) {
            responder.errorOccurred(error);
        } else {
            if (result instanceof List) {
                responder.setResponse((List) result);
            } else {
                responder.setResponse(result);
            }
        }
    }
}