package scripts.CTFire.Tanner.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import scripts.CTFire.Tanner.Constants.TannerAreas;
import scripts.CTFire.Tanner.Constants.TannerItems;
import scripts.CTFire.Tanner.Script.Task;

import java.util.concurrent.Callable;

import static scripts.CTFire.Tanner.Script.CTTanner.status;

/**
 * Created by Tyler on 4/5/2017.
 */
public class Run extends Task<ClientContext> {

    private TannerAreas areas = new TannerAreas();
    private TannerItems items = new TannerItems(1739, 1741);

    public Run(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        if(ctx.inventory.select().id(items.getItemToTan()).count() > 0 && areas.getBankArea().contains(ctx.players.local()))
            return true;
        if(ctx.inventory.select().id(items.getTannedItem()).count() > 0 && areas.getTanArea().contains(ctx.players.local()))
            return true;

        return false;
    }

    @Override
    public void execute(){
        status="Running";
        if(ctx.inventory.select().id(items.getItemToTan()).count() > 0){
            do{

                ctx.movement.findPath(areas.getTanArea().getRandomTile()).traverse();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.players.local().inMotion();
                    }
                }, 100, 10);
            } while(!areas.getTanArea().contains(ctx.players.local()));

        }else{
            do{

                ctx.movement.findPath(areas.getBankArea().getRandomTile()).traverse();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.players.local().inMotion();
                    }
                }, 100, 10);
            } while(!areas.getBankArea().contains(ctx.players.local()));
        }
    }
}
