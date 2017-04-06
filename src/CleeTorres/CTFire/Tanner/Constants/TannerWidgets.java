package scripts.CTFire.Tanner.Constants;

/**
 * Created by Tyler on 4/5/2017.
 */
public class TannerWidgets {

    public enum Widgets{
        SOFT_LEATHER(108), HARD_LEATHER(109), SNAKESKIN_1(110), SNAKESKIN_2(111),
        GREEN_DRAGONHIDE(112), BLUE_DRAGONHIDE(113), RED_DRAGONHIDE(114), BLACK_DRAGONHIDE(115);

        private int widgetId = 324;
        private int componentId;

        Widgets(int componentId){
            this.componentId = componentId;
        }

        public int getComponentId(){return this.componentId;}
        public int getWidgetId(){return this.widgetId;}
    }
}
