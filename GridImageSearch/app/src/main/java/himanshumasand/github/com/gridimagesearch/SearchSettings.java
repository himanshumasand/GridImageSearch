package himanshumasand.github.com.gridimagesearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Himanshu on 9/23/2015.
 */
public class SearchSettings {

    public int size;
    public int color;
    public int type;
    public String site;

    private String[] sizeParams = {"icon", "small", "xxlarge", "huge"};
    private String[] colorParams = {"black", "blue", "brown", "gray"};
    private String[] typeParams = {"face", "photo", "clipart", "lineart"};

    public SearchSettings() {
        this.size = 0;
        this.color = 0;
        this.type = 0;
        this.site = "";
    }

    public SearchSettings(int size, int color, int type, String site) {
        this.size = size;
        this.color = color;
        this.type = type;
        this.site = site;
    }

    public String getSizeParameter() {
        return sizeParams[size];
    }

    public String getColorParameter() {
        return colorParams[color];
    }

    public String getTypeParameter() {
        return typeParams[type];
    }
}
