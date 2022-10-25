package pj.utils;

public class GameNameFormatter {

    private GameNameFormatter(){};

    public static String changeTitleToURLFormat(String gameName) {
        return gameName.replaceAll(" ","-").toLowerCase();
    }
}
