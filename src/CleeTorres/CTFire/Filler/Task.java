package scripts.CTFire.Filler;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Tyler on 4/5/2017.
 */
public abstract class Task<T extends ClientContext> extends ClientAccessor{

    protected Task(T ctx){
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();

}
