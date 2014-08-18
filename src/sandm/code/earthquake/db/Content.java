package sandm.code.earthquake.db;

public class Content {

	public static String TableName="tbl_content";
	public static String ID="_id";
	public static String CONTENT="content";
	public static String PARENT="parent";
	
	public int id;
	public String content,parent;
	
	public Content(int id, String content, String parent) {
		super();
		this.id = id;
		this.content = content;
		this.parent = parent;
	}
	
	
	
}
