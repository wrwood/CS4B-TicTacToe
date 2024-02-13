package cs4b.Model;

import cs4b.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;

    private Model()
    {
        this.viewFactory = new ViewFactory();
    }

    // Snychronized prevents
    public static synchronized Model getInstance()
    {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory()
    {
        return viewFactory;
    }

}
