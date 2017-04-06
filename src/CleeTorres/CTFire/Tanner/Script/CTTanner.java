package scripts.CTFire.Tanner.Script;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import scripts.CTFire.Tanner.Tasks.*;
/**
 * Created by Tyler on 4/5/2017.
 */
@Script.Manifest(
        name = "CTTanner",
        properties = "author=CleeTorres; topic=######; client=4;",
        description = "Tanning, bro" )
public class CTTanner extends PollingScript<ClientContext> implements PaintListener{

    private ArrayList<Task> tasks = new ArrayList<Task>(Arrays.asList(new Tan(ctx),new Bank(ctx), new Run(ctx)));
    public volatile static String status = "Starting";

    public void start(){

    }

    @Override
    public void poll(){
        for(Task<?> t : tasks)
            if(t.activate())
                t.execute();
    }

    @Override
    public void repaint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setFont(new Font("Arial", Font.BOLD, 10));
        graphics.setColor(Color.BLACK);
        graphics.fillRect(5, 5, 170, 80);
        graphics.setColor(Color.BLUE);

        graphics.drawString(status, 10, 20);

    }
}
