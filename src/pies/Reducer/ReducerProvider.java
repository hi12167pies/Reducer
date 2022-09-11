package pies.Reducer;

import pies.Reducer.api.ReducerAPI;

public class ReducerProvider {
    public static ReducerAPI getAPI() {
        return Main.getPlugin(Main.class).api;
    }
}