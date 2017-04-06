package scripts.CTFire.Filler;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import scripts.CTFire.Tanner.Script.*;
import scripts.CTFire.Tanner.Tasks.Bank;
import scripts.CTFire.Tanner.Tasks.Run;
import scripts.CTFire.Tanner.Tasks.Tan;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Script.Manifest(
        name = "CTFill",
        properties = "author=CleeTorres; topic=######; client=4;",
        description = "Tanning, bro" )
public class Filler extends PollingScript<ClientContext> implements PaintListener {

    private ArrayList<scripts.CTFire.Tanner.Script.Task> tasks = new ArrayList<scripts.CTFire.Tanner.Script.Task>(Arrays.asList(new Fill(ctx),new FillBank(ctx)));
    public volatile static String status = "Starting";

    public void start(){

    }

    @Override
    public void poll(){
        for(scripts.CTFire.Tanner.Script.Task<?> t : tasks)
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
