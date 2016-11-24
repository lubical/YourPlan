package com.lubical.android.yourplan;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Created by lubical on 2016/11/7.
 */

public class GroupIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public GroupIntentJSONSerializer(Context context, String filename){
        mContext = context;
        mFilename = filename;
    }

    public void save(ArrayList<Group> arrayList) throws JSONException, IOException  {
        JSONArray array = new JSONArray();
        for (Group t:arrayList) {
            array.put(t.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Group> load() throws IOException, JSONException {
        ArrayList<Group>  arrayList = new ArrayList<Group>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            StringBuilder jsonString = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line =  reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i++) {
                Group t = new Group(array.getJSONObject(i));
                arrayList.add(t);
            }
        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null)  {
                reader.close();
            }
        }
        return arrayList;
    }

}
