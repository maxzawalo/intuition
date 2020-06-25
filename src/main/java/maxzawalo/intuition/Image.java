package maxzawalo.intuition;

/**
 * Образ
 */
public class Image {
	public static int last_id = 0;
	public int id = 0;
	public String name = "";

	public Image(String name) {
		this.name = name;
		id = ++last_id;
	}

}