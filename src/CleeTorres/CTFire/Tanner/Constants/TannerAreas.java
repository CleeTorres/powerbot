package scripts.CTFire.Tanner.Constants;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

/**
 * Created by Tyler on 4/5/2017.
 */
public class TannerAreas {

    private Area bankArea = new Area(new Tile(3269,3170,0),new Tile(3272,3162,0));
    private Area tanArea = new Area(new Tile(3270,3194,0),new Tile(3278,3189,0));

    public Area getBankArea() {return this.bankArea;}
    public Area getTanArea() {return this.tanArea;}

}
