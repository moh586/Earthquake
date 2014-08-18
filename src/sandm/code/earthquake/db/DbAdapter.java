package sandm.code.earthquake.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class DbAdapter {

	public DataBaseHelper mDb;
	
	    /**
	     * Database creation SQL statement
	     */

	    private final Context mCtx;
	    public static final String TABLE_CREATE =
	            "CREATE TABLE tbl_probs (" +
	            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	            "data INTEGER, " + "name TEXT" +
	            ");"
	    ;

	    public DbAdapter(Context ctx){
	    	this.mCtx=ctx;
	    	mDb =new DataBaseHelper(ctx);
	    	try {  
		        mDb.createDataBase();
		        } catch (Exception ioe) {
		        	Toast.makeText(ctx, ioe.getMessage(), Toast.LENGTH_SHORT).show();
		        }
	    	}

	    public int SelectProbs(int id) {
	    	openDB();
	        Cursor cursor = mDb.myDataBase.query(Probs.TableName, new String[] {Probs.ID, Probs.DATA}
	        , Probs.ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
	        if (cursor != null)
	            cursor.moveToFirst();
	        
	        int data=cursor.getInt(1);
	        
	        if (cursor != null && !cursor.isClosed()) {
	             cursor.close();
	          }
	        closeDB();
	        return data;
	    }
	   
	    public void UpdateProbs(int id,int value)
	    {
	    	openDB();
	    	ContentValues cv = new ContentValues();
	    	cv.put(Probs.DATA,value); //These Fields should be your String values of actual column names
	    	
	    	mDb.myDataBase.update(Probs.TableName, cv, "_id "+"="+id, null);
	    	closeDB();
	    }
	    
	    //****************************************
	    public List<Content> SelectContent(String parent)
	    {
	    	List<Content> output=new ArrayList<Content>() ;
	    	openDB();
	    	Cursor cursor = mDb.myDataBase.query(Content.TableName, new String[] {Content.CONTENT ,Content.ID,
	    			Content.PARENT,}, Content.PARENT+ "=?",
	 	                new String[] { parent }, null, null, null, null);
	    	 if (cursor.moveToFirst()) {
		            do {
		                Content content =new Content(Integer.parseInt(cursor.getString(1)),cursor.getString(0),cursor.getString(2));
		                // Adding contact to list
		                output.add(content);
		            } while (cursor.moveToNext());
		        }
		        if (cursor != null && !cursor.isClosed()) {
		             cursor.close();
		          }
		        closeDB();
	    	return output;
	    }
	    
	    public int ContentCount(String parent)
	    {
	    	openDB();
	    	Cursor cursor = mDb.myDataBase.query(Content.TableName, new String[] {Content.CONTENT ,Content.ID,
	    			Content.PARENT,}, Content.PARENT+ "=?",
	 	                new String[] { parent }, null, null, null, null);
	    	closeDB();
	    	if(cursor!=null)
	    		return cursor.getCount();
	    	else
	    		return 0;
	    }
	    
	    
	    
	    public void openDB()
	    {
	    	try {        
 	        	mDb.openDataBase();
 	        }catch(Exception sqle){
 	        	Toast.makeText(mCtx, sqle.getMessage(), Toast.LENGTH_SHORT).show();
 	        }
	    }

	    public void closeDB()
	    {
	    	mDb.close();
	    }
}
