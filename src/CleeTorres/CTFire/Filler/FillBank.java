package scripts.CTFire.Filler;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import scripts.CTFire.Tanner.Constants.TannerAreas;
import scripts.CTFire.Tanner.Constants.TannerItems;


import java.util.concurrent.Callable;


/**
 * Created by Tyler on 4/5/2017.
 */
public class FillBank extends scripts.CTFire.Tanner.Script.Task<ClientContext> {

    public static int orange = 2108;
    public static int orangeBasket = 5396;
    public static int basket = 5376;


    public FillBank(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return (ctx.inventory.select().id(orange).count() <= 4 );
    }

    @Override
    public void execute(){

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

        ctx.inventory.select().id(orangeBasket).peek().interact("Deposit-All");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() <= 4;
            }
        }, 80, 10);

        ctx.bank.select().id(basket).peek().interact("Withdraw-4");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() >= 4;
            }
        }, 80, 10);

        if(ctx.inventory.select().count() >= 4){
            ctx.bank.select().id(orange).peek().interact("Withdraw-All");

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
