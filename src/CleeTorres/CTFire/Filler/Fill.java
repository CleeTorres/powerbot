package scripts.CTFire.Filler;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import scripts.CTFire.Tanner.Constants.TannerAreas;
import scripts.CTFire.Tanner.Constants.TannerItems;
import scripts.CTFire.Tanner.Script.Task;

import java.util.concurrent.Callable;

import static scripts.CTFire.Filler.FillBank.basket;
import static scripts.CTFire.Filler.FillBank.orange;
import static scripts.CTFire.Tanner.Script.CTTanner.status;

/**
 * Created by Tyler on 4/5/2017.
 */
public class Fill extends Task<ClientContext> {

    private TannerAreas areas = new TannerAreas();
    private TannerItems items = new TannerItems(1739, 1741);
    private Random rand = new Random();
    public Fill(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return ((ctx.inventory.select().id(basket).count() > 0) && (ctx.inventory.select().id(FillBank.orange).count() >= 5));
    }

    @Override
    public void execute(){
        do{
            ctx.inventory.select().id(basket).peek().interact("Fill");
            Condition.sleep(rand.nextInt(55,180));
        }while(ctx.inventory.select().id(basket).count() > 0 && ctx.inventory.select().id(orange).count() >= 5);

    }
}
