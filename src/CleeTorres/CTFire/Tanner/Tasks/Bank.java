package scripts.CTFire.Tanner.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import scripts.CTFire.Tanner.Constants.TannerAreas;
import scripts.CTFire.Tanner.Constants.TannerItems;
import scripts.CTFire.Tanner.Script.Task;
import org.powerbot.script.*;
import java.util.concurrent.Callable;

import static scripts.CTFire.Tanner.Script.CTTanner.status;

/**
 * Created by Tyler on 4/5/2017.
 */
public class Bank extends Task<ClientContext> {

    private TannerItems items = new TannerItems(1739, 1741);
    private TannerAreas area = new TannerAreas();



    public Bank(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return (ctx.inventory.select().id(items.getItemToTan()).count() <= 0 && area.getBankArea().contains(ctx.players.local()));
    }

    @Override
    public void execute(){
    status="Banking";
        do{
           if(ctx.bank.inViewport())
               ctx.bank.open();
            else
               ctx.camera.turnTo(ctx.bank.nearest().tile());

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, 80, 10);
        }while(!ctx.bank.opened());

        ctx.inventory.select().id(items.getTannedItem()).peek().interact("Deposit-All");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() == 1;
            }
        }, 80, 10);

        if(ctx.inventory.select().count() <= 1){
            ctx.bank.select().id(items.getItemToTan()).peek().interact("Withdraw-All");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() > 1;
                }
            }, 80, 10);

            ctx.bank.close();

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            }, 80, 10);
        }


    }
}
