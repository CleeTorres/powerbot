package scripts.CTFire.Tanner.Constants;

import org.powerbot.script.rt4.Item;

/**
 * Created by Tyler on 4/5/2017.
 */
public class TannerItems {

    private int itemToTan;
    private int tannedItem;

    public TannerItems(int itemToTan, int tannedItem){
        this.itemToTan = itemToTan;
        this.tannedItem = tannedItem;
    }

    public int getItemToTan() {return this.itemToTan;}
    public int getTannedItem() {return this.tannedItem;}
}
