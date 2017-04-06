package scripts.CTFire.Tanner.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.Area;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import scripts.CTFire.Tanner.Constants.TannerAreas;
import scripts.CTFire.Tanner.Constants.TannerItems;
import scripts.CTFire.Tanner.Constants.TannerWidgets;
import scripts.CTFire.Tanner.Script.Task;

import java.util.concurrent.Callable;

import static scripts.CTFire.Tanner.Script.CTTanner.status;

/**
 * Created by Tyler on 4/5/2017.
 */
public class Tan extends Task<ClientContext>{

    private Component tanningWindow = ctx.widgets.widget(TannerWidgets.Widgets.SOFT_LEATHER.getWidgetId()).component(0);
    private Component tanButton = ctx.widgets.widget(TannerWidgets.Widgets.SOFT_LEATHER.getWidgetId()).component(TannerWidgets.Widgets.SOFT_LEATHER.getComponentId());
    private TannerItems items = new TannerItems(1739, 1741);
    private TannerAreas area = new TannerAreas();

    private int ellisId = 3231;

    public Tan(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return  (area.getTanArea().contains(ctx.players.local()) && ctx.inventory.select().id(items.getItemToTan()).count() > 0);
    }

    @Override
    public void execute(){
        status="Tanning";
        do{

            do{
                if(!ctx.npcs.select().id(ellisId).peek().inViewport()){
                    ctx.camera.turnTo(ctx.npcs.id(ellisId).peek());
                }

                ctx.npcs.id(ellisId).peek().interact("Trade");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return tanningWindow.visible();
                    }
                }, 100, 13);
            }while(!tanningWindow.visible() && (!(ctx.inventory.select().id(items.getTannedItem()).count() > 0)));



            tanButton.interact("Tan All");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(items.getItemToTan()).count() <= 0;
                }
            }, 100, 15);

        }while(!(ctx.inventory.select().id(items.getTannedItem()).count() > 0));






    }
}
