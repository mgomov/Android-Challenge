package mvg.com.jsonprinter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

/**
 * Created by Max on 1/23/2015.
 */
public class LineEntry {

	String name;
	Bitmap icon;

	public LineEntry(String name, Bitmap icon) {
		this.name = name;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
}
